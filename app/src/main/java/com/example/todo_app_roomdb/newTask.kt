package com.example.todo_app_roomdb

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.android.synthetic.main.activity_new_task.*
import java.text.SimpleDateFormat
import java.util.*


class newTask : AppCompatActivity() {
    var datePicker =
        MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()


    val timepicker =
        MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(10)
            .build()

    var flagfortimepicker=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_task)

        task_date.setOnClickListener{
            Toast.makeText(this,"fsds",Toast.LENGTH_SHORT).show()
            datePicker.show(supportFragmentManager,"aa")

        }
        task_time.setOnClickListener{
            if(flagfortimepicker==true)
            timepicker.show(supportFragmentManager,"sa")
        }
        var time=0L;
        var hour=0
        var minute=0
        timepicker.addOnPositiveButtonClickListener{
            Log.d("AAA","time=="+timepicker.hour)
            task_timee.hint=timepicker.hour.toString()+":"+timepicker.minute

            hour=timepicker.hour
            minute=timepicker.minute

            time= (timepicker.hour*3600000+timepicker.minute*60*3600).toLong()
        }
        var date=0L;
        datePicker.addOnPositiveButtonClickListener {
            val formatter = SimpleDateFormat("dd-MM-yyyy");
            val dateString = formatter.format (Date(it))
            Log.d("AAA","date=="+dateString+"ittt==="+it)
            task_datee.hint=dateString
            flagfortimepicker=true
            task_timee.visibility=View.VISIBLE
            date=it

        }
                savetask_btn.setOnClickListener{

                val db=DBHelper(this,null)
                    db.insert(task_title.editText?.text.toString(),
                        task_descp.editText?.text.toString(),task_datee.hint.toString(),task_timee.hint.toString())


                    val calendar = Calendar.getInstance()
                    calendar.time = Date(date) // Set your date object here
                    calendar.set(Calendar.HOUR_OF_DAY, hour)
                    calendar.set(Calendar.MINUTE, minute)
                    Log.d("DDD","=="+calendar.timeInMillis)
                    Log.d("DDD","==+++==="+(calendar.timeInMillis-System.currentTimeMillis()))
                    val milis=calendar.timeInMillis-System.currentTimeMillis()
                    val hrs=(milis/1000)/3600;
                    val min=((milis/1000)%3600)/60
                    Log.d("DDD","difff==${hrs}:${min}")



                    val notifyIntent = Intent(this, MyReceiver::class.java)
                    notifyIntent.putExtra("title",task_title.editText?.text.toString())
                    notifyIntent.putExtra("description",task_descp.editText?.text.toString())
                    val p_int: PendingIntent = PendingIntent.getBroadcast(this@newTask,
                        System.currentTimeMillis().toInt(),notifyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT)
                    val alarm_manager=getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarm_manager.set(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime()+milis,p_int)

                    val i = Intent(this, MainActivity::class.java)
// set the new task and clear flags
                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(i)
                    finish()
        }


    }
}