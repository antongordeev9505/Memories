package com.raywenderlich.android.memories.service

import android.app.IntentService
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.JobIntentService
import com.raywenderlich.android.memories.utils.FileUtils
import com.raywenderlich.android.memories.utils.toast
import java.io.File

//declare service in manifest
//as IntentService is depricated - use JobIntentService
//add permissions cuz we use JobIntentService to be compatible with old and new versions of Android
class DownloadService : JobIntentService() {

    //JobIntentService should be start from static
    companion object{
        private const val JOB_ID = 10

        fun startWork(context: Context, intent: Intent) {
            enqueueWork(context, DownloadService::class.java, JOB_ID, intent)
        }
    }

    //one-off service dont bound to something

    //when we start the service with regular way - get intent with parametres
    //onHandleIntent - starter point for all IntentServices
    override fun onHandleWork(intent: Intent){
        val imagePath = intent.getStringExtra("image_path")

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