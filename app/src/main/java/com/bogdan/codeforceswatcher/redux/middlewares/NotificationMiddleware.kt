package com.bogdan.codeforceswatcher.redux.middlewares

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.bogdan.codeforceswatcher.CwApp
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.activity.MainActivity
import com.bogdan.codeforceswatcher.feature.users.redux.request.UsersRequests
import org.rekotlin.Middleware
import org.rekotlin.StateType

val notificationMiddleware: Middleware<StateType> = { _, _ ->
    { next ->
        { action ->

            if (action is UsersRequests.FetchUsers.Success) {
                if (!action.isUser) {
                    var notificationText = ""
                    var flag = 0

                    for (ratingChange in action.result) {
                        if (flag == 1) {
                            notificationText += "\n"
                        }
                        flag = 1
                        notificationText += ratingChange.first + " " + if (ratingChange.second < 0) {
                            "${ratingChange.second}"
                        } else {
                            "+${ratingChange.second}"
                        }
                    }

                    if (notificationText.isNotEmpty()) {
                        showNotification(CwApp.app, notificationText)
                    }
                }
            }

            next(action)
        }
    }
}

const val CHANNEL_ID = "1234"

fun showNotification(context: Context, text: String) {
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val intentReceiver = Intent(context, MainActivity::class.java)
    intentReceiver.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

    val pendingIntent = PendingIntent.getActivity(
        context, 0, intentReceiver,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        createNotificationChannel(context, notificationManager)
    }

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle(context.getString(R.string.ratings_have_been_updated))
        .setContentText(text)
        .setContentIntent(pendingIntent)
        .setStyle(NotificationCompat.BigTextStyle().bigText(text))
        .setAutoCancel(true)

    val notification = builder.build()

    notificationManager.notify(1, notification)
}

@RequiresApi(android.os.Build.VERSION_CODES.O)
private fun createNotificationChannel(
    context: Context,
    notificationManager: NotificationManager
) {

    val channel = NotificationChannel(
        CHANNEL_ID, context.getString(R.string.app_name),
        NotificationManager.IMPORTANCE_HIGH
    )
    channel.description = context.getString(R.string.ratings_update)
    channel.enableLights(true)
    channel.lightColor = Color.RED
    channel.enableVibration(false)
    notificationManager.createNotificationChannel(channel)
}