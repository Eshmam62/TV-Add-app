package com.example.smarttvadplayer.media.player

import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

/**
 * Composable ExoPlayer video player for zone content and company advertisement.
 *
 * Manages ExoPlayer lifecycle correctly with Compose — creates player on composition,
 * handles pause/resume with lifecycle, and releases on disposal.
 *
 * @param uri The content URI (can be file://, content://, or android.resource://)
 * @param modifier Layout modifier
 * @param loop Whether to loop the video
 * @param onVideoEnd Callback when video reaches end (only called if not looping)
 * @param onError Callback when playback error occurs
 */
@OptIn(UnstableApi::class)
@Composable
fun ZoneVideoPlayer(
    uri: Uri,
    modifier: Modifier = Modifier,
    loop: Boolean = false,
    onVideoEnd: (() -> Unit)? = null,
    onError: ((Exception) -> Unit)? = null
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val exoPlayer = remember(uri.toString()) {
        createExoPlayer(context, uri, loop, onVideoEnd, onError)
    }

    // Lifecycle management — pause when activity pauses, resume when it resumes
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> exoPlayer.pause()
                Lifecycle.Event.ON_RESUME -> exoPlayer.play()
                Lifecycle.Event.ON_DESTROY -> exoPlayer.release()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            exoPlayer.release()
        }
    }

    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
                useController = false
                setShowBuffering(PlayerView.SHOW_BUFFERING_NEVER)
            }
        },
        modifier = modifier,
        update = { playerView ->
            playerView.player = exoPlayer
        }
    )
}

/**
 * Creates and configures an ExoPlayer instance.
 */
private fun createExoPlayer(
    context: Context,
    uri: Uri,
    loop: Boolean,
    onVideoEnd: (() -> Unit)?,
    onError: ((Exception) -> Unit)?
): ExoPlayer {
    return ExoPlayer.Builder(context).build().apply {
        val mediaItem = MediaItem.fromUri(uri)
        setMediaItem(mediaItem)
        repeatMode = if (loop) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_OFF
        playWhenReady = true

        addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_ENDED && !loop) {
                    onVideoEnd?.invoke()
                }
            }

            override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                onError?.invoke(Exception(error.message ?: "Playback error"))
            }
        })

        prepare()
    }
}
