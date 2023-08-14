package com.example.dispositivosmoviles.ui.utilities

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.dispositivosmoviles.R
import com.example.dispositivosmoviles.ui.activities.CameraActivity
import com.example.dispositivosmoviles.ui.activities.NotificationActivity

class BroadcasterNotifications : BroadcastReceiver() {
    private val CHANNEL: String = "Notificaciones"

    override fun onReceive(context: Context, intent: Intent) {
        val myIntent=Intent(context, NotificationActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val myPendingIntent: PendingIntent = PendingIntent.getActivity(
            context,
            0,
            myIntent,
            PendingIntent.FLAG_IMMUTABLE
        )



        val noti = NotificationCompat.Builder(context, CHANNEL).apply {
            setContentTitle("Primera Notificacion")
            setContentText("Notificacion Programada")
            setSmallIcon(R.drawable.baseline_heart_broken_24)
            priority = NotificationCompat.PRIORITY_DEFAULT
            setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Esta es una notificacion programada para recordar que estamos trabajando")
            )
        }


        noti.setContentIntent(myPendingIntent)
        noti.setAutoCancel(true)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, noti.build())
    }
}