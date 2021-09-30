package com.raywenderlich.android.memories.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.raywenderlich.android.memories.utils.FileUtils
import java.io.File

class SynchronizedImagesWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {
    override fun doWork(): Result {
        //receive the list of images and download it accordingly
        val images = inputData.getStringArray("images") ?: return Result.failure()

        FileUtils.queueImagesForDownload(applicationContext, images)
        return Result.success()
    }
}