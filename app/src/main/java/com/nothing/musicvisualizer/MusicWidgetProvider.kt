1
package com.nothing.musicvisualizer

import android.appwidget.AppWidgetProvider
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.media.session.PlaybackState
import android.graphics.Color

class MusicWidgetProvider : AppWidgetProvider() {

    companion object {
        const val ACTION_PLAY = "music.PLAY"
        const val ACTION_PREVIOUS = "music.PREVIOUS"
        const val ACTION_NEXT = "music.NEXT"

        fun updateAll(context: Context) {
            val manager = AppWidgetManager.getInstance(context)
            val ids = manager.getAppWidgetIds(
                android.content.ComponentName(
                    context,
                    MusicWidgetProvider::class.java
                )
            )

            ids.forEach { id ->
                updateWidget(context, manager, id)
            }
        }

        private fun updateWidget(
            context: Context,
            manager: AppWidgetManager,
            id: Int
        ) {
            val views = RemoteViews(
                context.packageName,
                R.layout.music_widget
            )

            views.setTextViewText(
                R.id.track_title,
                MusicNotificationListener.trackTitle
            )

            views.setTextViewText(
                R.id.track_artist,
                MusicNotificationListener.trackArtist
            )

            val playing = MusicNotificationListener.isPlaying

            views.setImageViewResource(
                R.id.play_button,
                if (playing)
                    android.R.drawable.ic_media_pause
                else
                    android.R.drawable.ic_media_play
            )

            views.setOnClickPendingIntent(
                R.id.play_button,
                actionIntent(context, id, ACTION_PLAY, 1)
            )

            views.setOnClickPendingIntent(
                R.id.previous_button,
                actionIntent(context, id, ACTION_PREVIOUS, 2)
            )

            views.setOnClickPendingIntent(
                R.id.next_button,
                actionIntent(context, id, ACTION_NEXT, 3)
            )

            manager.updateAppWidget(id, views)
        }

        private fun actionIntent(
            context: Context,
            id: Int,
            action: String,
            requestCode: Int
        ): android.app.PendingIntent {
            val intent = Intent(context, MusicWidgetProvider::class.java)
                .setAction(action)

            return android.app.PendingIntent.getBroadcast(
                context,
                id * 10 + requestCode,
                intent,
                android.app.PendingIntent.FLAG_UPDATE_CURRENT or
                    android.app.PendingIntent.FLAG_IMMUTABLE
            )
        }
    }

    override fun onUpdate(
        context: Context,
        manager: AppWidgetManager,
        ids: IntArray
    ) {
        ids.forEach { id ->
            updateAll(context)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        val controller = MusicNotificationListener.controller

        when (intent.action) {
            ACTION_PLAY -> {
                val state = controller?.playbackState?.state
                if (state == PlaybackState.STATE_PLAYING) {
                    controller.transportControls.pause()
                } else {
                    controller?.transportControls?.play()
                }
            }

            ACTION_PREVIOUS ->
                controller?.transportControls?.skipToPrevious()

            ACTION_NEXT ->
                controller?.transportControls?.skipToNext()
        }

        updateAll(context)
    }
}
