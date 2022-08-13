package com.example.todo_app_roomdb

import android.R
import android.app.*
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class sendnotificationservice : IntentService("MyNewIntentService") {
    override fun onHandleIntent(intent: Intent?) {

        val nm: NotificationManager =this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val not_channel: NotificationChannel =(NotificationChannel("first","default",
            NotificationManager.IMPORTANCE_HIGH))

        not_channel.apply {
            enableLights(true)
            enableVibration(true)
        }
        nm.createNotificationChannel(not_channel)
        val i=Intent(this@sendnotificationservice,MainActivity::class.java)
        val pi=PendingIntent.getActivity(this,123,i,PendingIntent.FLAG_UPDATE_CURRENT)
        val simplenotf= NotificationCompat.Builder(this,"first").setContentTitle(intent?.getStringExtra("title"))
            .setContentText(intent?.getStringExtra("description"))
            .addAction(com.example.todo_app_roomdb.R.drawable.ic_launcher_foreground,"pinch me",pi)
            .setContentIntent(pi)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setSmallIcon(com.example.todo_app_roomdb.R.drawable.ic_launcher_foreground).build()
        nm.notify(1,simplenotf)

       Log.d("AAA","servvv===")
    }

    companion object {
        private const val NOTIFICATION_ID = 3
    }
}