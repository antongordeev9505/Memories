package com.raywenderlich.android.memories.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.JobIntentService
import androidx.core.app.NotificationCompat
import androidx.work.ListenableWorker
import com.raywenderlich.android.memories.App
import com.raywenderlich.android.memories.model.result.Success
import com.raywenderlich.android.memories.ui.main.MainActivity
import com.raywenderlich.android.memories.utils.FileUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

const val NOTIFICATION_CHANNEL_NAME = "Synchronize service channel"
const val NOTIFICATION_CHANNEL_ID = "Synchronize ID"

//foreground service
class SynchronizeImageService: Service() {

    private val remoteApi by lazy { App.remoteApi }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        showNotification()
        clearStorage()
        fetchImages()

        return START_NOT_STICKY
    }

    private fun showNotification() {
        //in case Android version 26+ cuz notif channel exist only in 26+ API
        createNotificationChannel()

        //update MAin activity if clicked
        val notificationIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        //create notification
        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Synchronization service")
            .setContentText("Downloading image")
            .setContentIntent(pendingIntent)
            .build()

        //when start notification - it will be displayed untill user kill the app or we stop foreground service
        startForeground(1, notification)
    }

    private fun createNotificationChannel() {
        //create only when 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }


    private fun clearStorage() {
        FileUtils.clearLocalStorage(applicationContext)
    }

    private fun fetchImages() {
        GlobalScope.launch {
            val result = remoteApi.getImages()

            if (result is Success){
                val imagesArray = result.data.map { it.imagePath }.toTypedArray()

                FileUtils.queueImagesForDownload(applicationContext, imagesArray)
            }
        }
    }
}