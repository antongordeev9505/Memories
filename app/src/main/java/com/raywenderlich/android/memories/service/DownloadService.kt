package com.raywenderlich.android.memories.service

import android.app.IntentService
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.raywenderlich.android.memories.utils.FileUtils
import com.raywenderlich.android.memories.utils.toast
import java.io.File

const val SERVICE_NAME = "Download image service"
//declare service in manifest
//require name
class DownloadService : IntentService(SERVICE_NAME) {

    //one-off service dont bound to something

    //when we start the service with regular way - get intent with parametres
    //onHandleIntent - starter point for all IntentServices
    override fun onHandleIntent(intent: Intent?){
        val imagePath = intent?.getStringExtra("image_path")

        if (imagePath != null) {
            downloadImage(imagePath)
        } else {
            Log.d("Missing image path", "Stop service")
            //stop service
            stopSelf()
        }
    }

    private fun downloadImage(imagePath: String) {
        //intent service run in back thread by default
        val file = File(applicationContext.externalMediaDirs.first(), imagePath)

        FileUtils.downloadImage(file, imagePath)

    }

    override fun onDestroy() {
        applicationContext?.toast("Stop service")
        super.onDestroy()
    }
}