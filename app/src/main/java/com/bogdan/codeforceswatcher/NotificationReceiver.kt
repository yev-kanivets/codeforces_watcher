package com.bogdan.codeforceswatcher

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v4.app.NotificationCompat
import com.bogdan.codeforceswatcher.activity.MainActivity

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        onRefresh()
        if (LoadUser.textNotification != "") {
            val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val intentReceiver = Intent(context, MainActivity::class.java)
            intentReceiver.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

            val pIntent = PendingIntent.getActivity(context, 0, intentReceiver, PendingIntent.FLAG_UPDATE_CURRENT)

            val channelId = "1234"

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val channel = NotificationChannel(channelId, "My channel",
                        NotificationManager.IMPORTANCE_HIGH)
                channel.description = "My channel description"
                channel.enableLights(true)
                channel.lightColor = Color.RED
                channel.enableVibration(false)
                notificationManager.createNotificationChannel(channel)
            }

            val builder = NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Title")
                    .setContentText(LoadUser.textNotification)
                    .setContentIntent(pIntent)

            val notification = builder.build()

            notificationManager.notify(1, notification)
        }
    }

    private fun onRefresh() {
        var handles = ""
        for (element in MainActivity.it) {
            handles += element.handle + ";"
        }
        LoadUser.loadUser(handles, null)
    }
}