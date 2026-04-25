package com.tv.backlight

import android.R
import android.app.*
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat

class BacklightService : Service() {

    override fun onCreate() {
        super.onCreate()
        startForeground(1, notification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    private fun notification(): Notification {

        val channelId = "backlight_channel"

        val channel = NotificationChannel(
            channelId,
            "Backlight Service",
            NotificationManager.IMPORTANCE_LOW
        )

        getSystemService(NotificationManager::class.java)
            .createNotificationChannel(channel)

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Backlight running")
            .setSmallIcon(R.drawable.ic_menu_view)
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}