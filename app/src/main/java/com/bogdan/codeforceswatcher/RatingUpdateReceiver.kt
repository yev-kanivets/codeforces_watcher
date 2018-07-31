package com.bogdan.codeforceswatcher

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.bogdan.codeforceswatcher.activity.MainActivity

class RatingUpdateReceiver : BroadcastReceiver() {

    var notificationText = ""

    override fun onReceive(context: Context?, intent: Intent?) {
        onRefresh()
        Log.d("MeTag", "onReceive")
        if (notificationText != "") {
            val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val intentReceiver = Intent(context, MainActivity::class.java)
            intentReceiver.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

            val pendingIntent = PendingIntent.getActivity(context, 0, intentReceiver, PendingIntent.FLAG_UPDATE_CURRENT)

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
                    .setContentTitle(context.getString(R.string.ratings_have_been_updated))
                    .setContentText(notificationText)
                    .setContentIntent(pendingIntent)

            val notification = builder.build()

            notificationManager.notify(1, notification)
        }
    }

    private fun onRefresh() {
        var handles = ""
        for (element in MainActivity.it) {
            handles += element.handle + ";"
        }
        UserLoaded.loadUsers(handles) { it ->
            if (it != null) {
                var flag = 0
                for (element in it) {
                    if (flag == 1) {
                        notificationText += "\n"
                    }
                    flag = 1
                    notificationText += element.first + " " + if (element.second < 0) {
                        "-${element.second}"
                    } else {
                        "+${element.second}"
                    }
                }
            }
        }
    }

}