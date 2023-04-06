package ua.com.radiokot.photoprism.di

import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier._q
import org.koin.dsl.bind
import org.koin.dsl.module
import ua.com.radiokot.photoprism.api.util.KeyChainClientCertificateKeyManager
import ua.com.radiokot.photoprism.api.util.SessionAwarenessInterceptor
import ua.com.radiokot.photoprism.api.util.SessionRenewalInterceptor
import ua.com.radiokot.photoprism.base.data.storage.ObjectPersistence
import ua.com.radiokot.photoprism.env.data.model.EnvAuth
import ua.com.radiokot.photoprism.env.data.model.EnvConnectionParams
import ua.com.radiokot.photoprism.env.data.model.EnvSession
import ua.com.radiokot.photoprism.env.logic.PhotoPrismSessionCreator
import ua.com.radiokot.photoprism.env.logic.SessionCreator
import ua.com.radiokot.photoprism.extension.checkNotNull

class EnvHttpClientParams(
    val sessionAwareness: SessionAwareness?,
    val clientCertificateAlias: String?,
) : SelfParameterHolder() {
    class SessionAwareness(
        val sessionIdProvider: () -> String,
        val renewal: Renewal?,
    ) {
        class Renewal(
            val authProvider: () -> EnvAuth,
            val sessionCreator: SessionCreator,
            val onSessionRenewed: ((newId: String) -> Unit)?,
        )
    }
}

class EnvSessionCreatorParams(
    val envConnectionParams: EnvConnectionParams,
) : SelfParameterHolder()

val envModules = listOf(
    module {
        includes(ioModules)

        // The factory must have a qualifier,
        // otherwise it is overridden by the scoped.
        factory(_q<EnvHttpClientParams>()) { envParams ->
            envParams as EnvHttpClientParams
            val builder = get<OkHttpClient.Builder>()

            if (envParams.sessionAwareness != null) {
                val sessionAwareness = envParams.sessionAwareness

                if (sessionAwareness.renewal != null) {
                    val renewal = sessionAwareness.renewal

                    builder.addInterceptor(
                        SessionRenewalInterceptor(
                            authProvider = renewal.authProvider,
                            onSessionRenewed = renewal.onSessionRenewed,
                            sessionCreator = renewal.sessionCreator,
                        )
                    )
                }

                builder.addInterceptor(
                    SessionAwarenessInterceptor(
                        sessionIdProvider = sessionAwareness.sessionIdProvider,
                        sessionIdHeaderName = "X-Session-ID"
                    )
                )
            }

            if (envParams.clientCertificateAlias != null) {
                val clientCertKeyManager = KeyChainClientCertificateKeyManager(
                    context = get(),
                    alias = envParams.clientCertificateAlias,
                )
                val platformTrustManager = Platform.get().platformTrustManager()
                val sslContext = Platform.get().newSSLContext()
                sslContext.init(
                    arrayOf(clientCertKeyManager),
                    arrayOf(platformTrustManager),
                    null,
                )
                builder.sslSocketFactory(sslContext.socketFactory, platformTrustManager)
            }

            builder
                .addInterceptor(get<HttpLoggingInterceptor>())
                .build()
        } bind HttpClient::class

        factory(_q<EnvSessionCreatorParams>()) { params ->
            params as EnvSessionCreatorParams
            PhotoPrismSessionCreator(
                sessionService = get(_q<EnvPhotoPrismSessionServiceParams>()) {
                    EnvPhotoPrismSessionServiceParams(
                        envConnectionParams = params.envConnectionParams,
                    )
                },
            )
        } bind SessionCreator::class

        scope<EnvSession> {
            scoped {
                val session = get<EnvSession>()
                val authPersistence = getOrNull<ObjectPersistence<EnvAuth>>(_q<EnvAuth>())
                val sessionPersistence = getOrNull<ObjectPersistence<EnvSession>>(_q<EnvSession>())

                val renewal: EnvHttpClientParams.SessionAwareness.Renewal? =
                    if (authPersistence != null && authPersistence.hasItem())
                        EnvHttpClientParams.SessionAwareness.Renewal(
                            authProvider = {
                                authPersistence.loadItem().checkNotNull {
                                    "There must be an auth data in order to renew the session"
                                }
                            },
                            sessionCreator = get(_q<EnvSessionCreatorParams>()) {
                                EnvSessionCreatorParams(
                                    envConnectionParams = session.envConnectionParams,
                                )
                            },
                            onSessionRenewed = {
                                session.id = it
                                sessionPersistence?.saveItem(session)
                            }
                        )
                    else
                        null

                get<HttpClient>(_q<EnvHttpClientParams>()) {
                    EnvHttpClientParams(
                        sessionAwareness = EnvHttpClientParams.SessionAwareness(
                            sessionIdProvider = session::id,
                            renewal = renewal,
                        ),
                        clientCertificateAlias = session.envConnectionParams.clientCertificateAlias,
                    )
                }
            }.bind(HttpClient::class)
        }
    }
)