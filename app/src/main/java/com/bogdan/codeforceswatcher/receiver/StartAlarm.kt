package com.bogdan.codeforceswatcher.receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.SystemClock

class StartAlarm : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val intentReceiver = Intent(context, RatingUpdateReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intentReceiver, 0)
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), AlarmManager.INTERVAL_DAY, pendingIntent)
    }

}