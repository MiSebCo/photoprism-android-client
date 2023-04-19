package ua.com.radiokot.photoprism.features.viewer.view.model

import android.net.Uri
import android.view.View
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.audio.AudioSink
import com.google.android.exoplayer2.decoder.DecoderException
import com.mikepenz.fastadapter.FastAdapter
import ua.com.radiokot.photoprism.R
import ua.com.radiokot.photoprism.databinding.PagerItemMediaViewerVideoBinding
import ua.com.radiokot.photoprism.extension.checkNotNull
import ua.com.radiokot.photoprism.features.gallery.data.model.GalleryMedia
import ua.com.radiokot.photoprism.features.viewer.view.VideoPlayerCache

class VideoViewerPage(
    previewUrl: String,
    val isLooped: Boolean,
    val needsVideoControls: Boolean,
    thumbnailUrl: String,
    source: GalleryMedia?,
) : MediaViewerPage(thumbnailUrl, source) {
    val previewUri: Uri = Uri.parse(previewUrl)

    override val type: Int
        get() = R.id.pager_item_media_viewer_video

    override val layoutRes: Int
        get() = R.layout.pager_item_media_viewer_video

    override fun getViewHolder(v: View): ViewHolder =
        ViewHolder(v)

    class ViewHolder(itemView: View) : FastAdapter.ViewHolder<VideoViewerPage>(itemView) {
        val view = PagerItemMediaViewerVideoBinding.bind(itemView)
        var playerCache: VideoPlayerCache? = null
        var fatalPlaybackErrorListener: (VideoViewerPage) -> Unit = {}

        fun bindToLifecycle(lifecycle: Lifecycle) {
            lifecycle.addObserver(object : DefaultLifecycleObserver {
                override fun onPause(owner: LifecycleOwner) {
                    val player = view.videoView.player
                        ?: return

                    view.videoView.post {
                        // Only pause if the owner is not destroyed.
                        if (owner.lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)
                            && player.isPlaying
                        ) {
                            player.pause()
                        }
                    }
                }
            })
        }

        override fun attachToWindow(item: VideoViewerPage) {
            view.videoView.useController = item.needsVideoControls

            val playerCache = this.playerCache.checkNotNull {
                "Player cache must be set"
            }

            val player = playerCache.getPlayer(
                mediaSourceUri = item.previewUri,
                context = view.videoView.context,
            )

            with(player) {
                repeatMode =
                    if (item.isLooped)
                        Player.REPEAT_MODE_ONE
                    else
                        Player.REPEAT_MODE_OFF
                // Only play automatically on init.
                if (!isPlaying && playbackState == Player.STATE_IDLE) {
                    playWhenReady = true
                }
                prepare()

                val decoderExceptionListener = TheOnlyPlayerFatalPlaybackExceptionListener {
                    fatalPlaybackErrorListener(item)
                }
                player.removeListener(decoderExceptionListener)
                player.addListener(decoderExceptionListener)

                view.videoView.player = this
            }
        }

        override fun detachFromWindow(item: VideoViewerPage) {
            view.videoView.player?.apply {
                stop()
                seekToDefaultPosition()
            }
        }

        // Video player must be set up only once it is attached.
        override fun bindView(item: VideoViewerPage, payloads: List<Any>) {
        }

        override fun unbindView(item: VideoViewerPage) {
        }

        private class TheOnlyPlayerFatalPlaybackExceptionListener(
            private val onError: (Throwable) -> Unit,
        ) : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                when (val cause = error.cause) {
                    is DecoderException,
                    is AudioSink.InitializationException ->
                        onError(cause)
                }
            }

            override fun equals(other: Any?): Boolean {
                return other is TheOnlyPlayerFatalPlaybackExceptionListener
            }

            override fun hashCode(): Int {
                return 333
            }
        }
    }
}