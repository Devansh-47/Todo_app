package com.example.todo_app_roomdb

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class MyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val title= intent?.getStringExtra("title")
        val description= intent?.getStringExtra("description")
        val intent1 = Intent(context, sendnotificationservice::class.java)
        intent1.putExtra("title",title)
        intent1.putExtra("description",description)
        Toast.makeText(context,"receeeiiiii",Toast.LENGTH_SHORT).show()
        context.startService(intent1)


    }
}