package com.example.dispositivosmoviles.ui.activities

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.dispositivosmoviles.R
import com.example.dispositivosmoviles.databinding.ActivityNotificationBinding
import com.example.dispositivosmoviles.ui.utilities.BroadcasterNotifications
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.Calendar

class NotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.btnNotification.setOnClickListener {
            //Se debe ejecutar una sola vez. ya sea al instalarse o abrirse
            //No deberia crearse cada que se se envia la notificacion
            createNotificationChannel()
            sendNotification()
        }
        binding.btnNotificationProgramada.setOnClickListener {

            val calendar= Calendar.getInstance()
            val hora=binding.timePicker.hour
            val minutes=binding.timePicker.minute

            Toast.makeText(this,
                "La notificacion se activara a las : " +
                    "$hora con $minutes",
                Toast.LENGTH_SHORT).show()
            calendar.set(Calendar.HOUR_OF_DAY, hora)
            calendar.set(Calendar.MINUTE, minutes)
            calendar.set(Calendar.SECOND,0)
            calendar.set(Calendar.MILLISECOND,0)

            Log.d("UCE", "HORA "+Calendar.HOUR_OF_DAY.toString())
            Log.d("UCE", "MINUTO "+Calendar.MINUTE.toString())
            sendNotificationTimePicker(calendar.timeInMillis)

        }
    }
    val CHANNEL : String = "Notificaciones"

    private fun sendNotificationTimePicker(time:Long) {

        val myIntent=Intent(applicationContext, BroadcasterNotifications::class.java)
        val myPendingIntent=PendingIntent.getBroadcast(
            applicationContext,
            0,
            myIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager=getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,time, AlarmManager.INTERVAL_DAY, myPendingIntent)

    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Variedades"
            val descriptionText = "Notificaciones simples de variedades"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    @SuppressLint("MissingPermission")
    fun sendNotification(){
        val intent = Intent(this, CameraActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val noti = NotificationCompat.Builder(this, CHANNEL).apply {
            setContentTitle("Notificación")
            setContentText("Tienes una notificacion...!!")
            setSmallIcon(R.drawable.baseline_heart_broken_24)
            setPriority(NotificationCompat.PRIORITY_MAX)
                .setStyle(NotificationCompat.BigTextStyle()
                    .bigText("Esta es un notificacion de la activity de notificación"))
            setContentIntent(pendingIntent)
            setAutoCancel(true)
        }

        with(NotificationManagerCompat.from(this)) {
            notify(1, noti.build())
        }

    }
}