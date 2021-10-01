package com.raywenderlich.android.memories.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.raywenderlich.android.memories.utils.FileUtils
import com.raywenderlich.android.memories.utils.toast
import java.io.File

//declare service in manifest
class DownloadService : Service() {
    //we dont need bound service - so we return null
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    //when we start the service with regular way - get intent with parametres
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val imagePath = intent?.getStringExtra("image_path")

        if (imagePath != null) {
            downloadImage(imagePath)
        } else {
            Log.d("Missing image path", "Stop service")
            //stop service
            stopSelf()
        }

        //when originating process will be killed - the service will be removed from the starter state
        return START_NOT_STICKY
    }

    private fun downloadImage(imagePath: String) {
        //service run in main thread, therefore we go to BG to download image
        Thread(Runnable {
            val file = File(applicationContext.externalMediaDirs.first(),imagePath)

            FileUtils.downloadImage(file, imagePath)
        }).start()
    }

    override fun onDestroy() {
        applicationContext?.toast("Stop service")
        super.onDestroy()
    }
}