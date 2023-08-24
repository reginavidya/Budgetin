package org.d3ifcool.budgetin.notify

import android.app.NotificationManager
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.d3ifcool.budgetin.R

class FcmService : FirebaseMessagingService() {
    private var broadcaster: LocalBroadcastManager? = null
    companion object {
        const val KEY_URL = "url"
    }

    override fun onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this)
    }

    override fun onNewToken(token: String) {
        Log.d("FCM", "Token baru: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {

        val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val r = RingtoneManager.getRingtone(applicationContext, notification)
        r.play()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            r.isLooping = false
        }

        handleMessage(message)

        val title = message.notification?.title ?: return
        val body = message.notification?.body ?: return
        val url = message.data[KEY_URL] ?: return
        val notificationManager = ContextCompat.getSystemService( applicationContext, NotificationManager::class.java ) as NotificationManager
        notificationManager.sendNotification(applicationContext, title, body, url)
    }

    private fun handleMessage(message: RemoteMessage) {
        //1
        val handler = Handler(Looper.getMainLooper())

        //2
        handler.post {
            Toast.makeText(
                baseContext, getString(R.string.handle_notification_now),
                Toast.LENGTH_LONG
            ).show()
            message.notification?.let {
                val intent = Intent("MyData")
                intent.putExtra("message", it.body)
                broadcaster?.sendBroadcast(intent)
            }
        }
    }
}