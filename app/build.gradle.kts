
package com.nothing.musicvisualizer

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.media.session.MediaSession
import android.media.session.MediaController
import android.media.MediaMetadata
import android.content.ComponentName
import android.os.Build

class MusicNotificationListener : NotificationListenerService() {

    companion object {
        var controller: MediaController? = null
            private set

        var trackTitle: String = "Nothing playing"
        var trackArtist: String = "Artist"
        var isPlaying: Boolean = false

        fun refreshWidget(context: android.content.Context) {
            MusicWidgetProvider.updateAll(context)
        }
    }

    override fun onListenerConnected() {
        super.onListenerConnected()

        activeNotifications?.forEach {
            readNotification(it)
        }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        readNotification(sbn)
    }

    private fun readNotification(sbn: StatusBarNotification) {
        val extras = sbn.notification.extras

        val token = if (Build.VERSION.SDK_INT >= 33) {
            extras.getParcelable(
                "android.mediaSession",
                MediaSession.Token::class.java
            )
        } else {
            @Suppress("DEPRECATION")
            extras.getParcelable("android.mediaSession") as? MediaSession.Token
        } ?: return

        val mediaController = try {
            MediaController(this, token)
        } catch (_: Exception) {
            return
        }

        controller = mediaController
        updateMetadata(mediaController.metadata)
        isPlaying = mediaController.playbackState?.state ==
            android.media.session.PlaybackState.STATE_PLAYING

        refreshWidget(this)
    }

    private fun updateMetadata(metadata: MediaMetadata?) {
        trackTitle = metadata?.getString(
            MediaMetadata.METADATA_KEY_TITLE
        ) ?: "Unknown track"

        trackArtist = metadata?.getString(
            MediaMetadata.METADATA_KEY_ARTIST
        ) ?: "Unknown artist"
    }
}
