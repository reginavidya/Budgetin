package org.d3ifcool.budgetin.notify

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import org.d3ifcool.budgetin.MainActivity
import org.d3ifcool.budgetin.R

private const val NOTIFICATION_ID = 0
private const val PENGUMUMAN_ID = 1

@SuppressLint("UnspecifiedImmutableFlag")
fun NotificationManager.sendNotification(context: Context) {
    val intent = Intent(context, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(context,
        NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT)

    val builder = NotificationCompat.Builder(
        context,
        context.getString(R.string.notif_channel_id)
    )
        .setSmallIcon(R.mipmap.ic_launcher_round)
        .setContentTitle(context.getString(R.string.notif_title))
        .setContentText(context.getString(R.string.notif_message))
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    notify(NOTIFICATION_ID, builder.build())
}

@SuppressLint("UnspecifiedImmutableFlag")
fun NotificationManager.sendNotification(context: Context, title: String, body: String, url: String) {
    val intent = Intent(context, MainActivity::class.java)
    intent.putExtra(FcmService.KEY_URL, url)
    val pendingIntent = PendingIntent.getActivity(context,
        PENGUMUMAN_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT)

    val builder = NotificationCompat.Builder(
        context,
        context.getString(R.string.news_channel_id)
    )
        .setSmallIcon(R.mipmap.ic_launcher_round)
        .setContentTitle(title)
        .setContentText(body)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    notify(PENGUMUMAN_ID, builder.build())
}

fun createChannel(context: Context, idRes:Int, nameRes: Int, descRes: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel = NotificationChannel(
            context.getString(idRes),
            context.getString(nameRes),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            setShowBadge(false)
            enableLights(true)
            lightColor = Color.RED
            enableVibration(true)
            description = context.getString(descRes)
        }

        val manager = context.getSystemService(NotificationManager::class.java)
        manager?.createNotificationChannel(notificationChannel)
    }
}