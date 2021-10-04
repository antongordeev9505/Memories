package com.raywenderlich.android.memories.service

import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.JobIntentService
import com.raywenderlich.android.memories.App
import com.raywenderlich.android.memories.model.result.Success
import com.raywenderlich.android.memories.utils.FileUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class UploadImageService: JobIntentService() {

    private val remoteApi by lazy { App.remoteApi }

    companion object{
        private const val JOB_ID = 20

        fun startWork(context: Context, intent: Intent) {
            enqueueWork(context, UploadImageService::class.java, JOB_ID, intent)
        }
    }
    override fun onHandleWork(intent: Intent) {
        val filePath = intent.getStringExtra("image_path")
        if (filePath != null) {
            uploadImage(filePath)
        }
    }

    private fun uploadImage(filePath: String) {
        GlobalScope.launch {
            val result = remoteApi.uploadImage(File(filePath))

            val intent = Intent()
            //putBoolean
            intent.putExtra("is_uploaded", result.message == "Success!")
            intent.action = ACTION_IMAGE_UPLOAD

            //3. broadcast intent with selected action
            sendBroadcast(intent)
        }
    }
}