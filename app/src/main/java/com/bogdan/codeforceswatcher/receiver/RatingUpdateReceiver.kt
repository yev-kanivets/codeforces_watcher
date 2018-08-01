package com.bogdan.codeforceswatcher.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import com.bogdan.codeforceswatcher.CwApp
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.util.UserLoader
import com.bogdan.codeforceswatcher.activity.MainActivity

class RatingUpdateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val roomUserList = CwApp.app.userDao.getAll()

        UserLoader.loadUsers(roomUserList) { it ->
            var notificationText = ""
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

            showNotification(context, notificationText)
        }
    }

    private fun showNotification(context: Context, text: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intentReceiver = Intent(context, MainActivity::class.java)
        intentReceiver.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

        val pendingIntent = PendingIntent.getActivity(context, 0, intentReceiver,
                PendingIntent.FLAG_UPDATE_CURRENT)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            createNotificationChannel(context, notificationManager)
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.ratings_have_been_updated))
                .setContentText(text)
                .setContentIntent(pendingIntent)

        val notification = builder.build()

        notificationManager.notify(1, notification)
    }

    @RequiresApi(android.os.Build.VERSION_CODES.O)
    private fun createNotificationChannel(context: Context, notificationManager: NotificationManager) {
        val channel = NotificationChannel(CHANNEL_ID, context.getString(R.string.app_name),
                NotificationManager.IMPORTANCE_HIGH)
        channel.description = context.getString(R.string.ratings_update)
        channel.enableLights(true)
        channel.lightColor = Color.RED
        channel.enableVibration(false)
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        private const val CHANNEL_ID = "1234"
    }

}
