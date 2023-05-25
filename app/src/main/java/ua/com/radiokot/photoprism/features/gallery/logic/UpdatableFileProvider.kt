package ua.com.radiokot.photoprism.features.gallery.logic

import android.content.ContentValues
import android.net.Uri
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.provider.MediaStore.MediaColumns
import androidx.core.content.FileProvider
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import okhttp3.internal.closeQuietly
import okio.sink
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.inject
import org.koin.core.scope.Scope
import ua.com.radiokot.photoprism.R
import ua.com.radiokot.photoprism.di.DI_SCOPE_SESSION
import ua.com.radiokot.photoprism.di.HttpClient
import ua.com.radiokot.photoprism.extension.kLogger
import java.io.IOException

/**
 * A [FileProvider] that allows custom MIME-type updates for created URIs.
 */
class UpdatableFileProvider : FileProvider(R.xml.file_provider_paths), KoinScopeComponent {
    override val scope: Scope
        get() = getKoin().getScope(DI_SCOPE_SESSION)
    private val httpClient: HttpClient by inject()
    private val log = kLogger("UpdatableFileProvider")

    private val mimeTypeMap = mutableMapOf<Uri, String>()
    private val urlMap = mutableMapOf<Uri, String>()

    override fun openFile(
        uri: Uri,
        mode: String,
    ): ParcelFileDescriptor? {
        log.debug {
            "openFile(): enqueueing_call:" +
                    "\nuri=$uri"
        }
        val pipe = ParcelFileDescriptor.createPipe()
        val pipeIn = pipe[1]
        val url = "https://sample-videos.com/img/Sample-jpg-image-30mb.jpg"//urlMap[uri]!!
        httpClient.newCall(
            Request.Builder().get().url(url).build()
        )
            .apply {
//                signal?.setOnCancelListener {
//                    cancel()
//                    pipeIn.closeQuietly()
//                }
            }
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    log.error(e) {
                        "openFile(): call_failed"
                    }
                    pipeIn.closeWithError(e.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    ParcelFileDescriptor.AutoCloseOutputStream(pipeIn).use { output ->
                        response.body?.source()?.readAll(output.sink())
                        log.debug { "openFile(): written_response" }
                    }
                }
            })
        return pipe[0]
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return if (values != null && values.containsKey(MediaColumns.MIME_TYPE)) {
            val mimeType = values.getAsString(MediaColumns.MIME_TYPE)
            mimeTypeMap[uri] = mimeType

            log.debug {
                "update(): updated_mime_type:" +
                        "\nuri=$uri," +
                        "\nmimeType=$mimeType"
            }

            val url = values.getAsString("url")
            if (url != null) {
                urlMap[uri] = url

                log.debug {
                    "update(): updated_url:" +
                            "\nuri=$uri," +
                            "\nurl=$url"
                }
            }

            1
        } else {
            0
        }
    }

    override fun getType(uri: Uri): String? {
        val updatedMimeType = mimeTypeMap[uri]

        if (updatedMimeType != null) {
            log.debug {
                "getType(): return_updated_mime_type:" +
                        "\nuri=$uri," +
                        "\nmimeType=$updatedMimeType"
            }

            return updatedMimeType
        } else {
            log.debug {
                "getType(): no_updated_mime_type_found:" +
                        "\nuri=$uri"
            }

            return super.getType(uri)
        }
    }
}