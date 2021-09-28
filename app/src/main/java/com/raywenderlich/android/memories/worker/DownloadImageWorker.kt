package com.raywenderlich.android.memories.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class DownloadImageWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {
    override fun doWork(): Result {
        //create inputStream and download the image in local file
        val imageDownloadPath = inputData.getString("image_path") ?: return Result.failure()
        val imageUrl = URL(imageDownloadPath)

        val connection = imageUrl.openConnection() as HttpURLConnection
        connection.doInput = true
        connection.connect()

        val imagePath = "${System.currentTimeMillis()}.jpg"
        val inputStream = connection.inputStream
        val file = File(applicationContext.externalMediaDirs.first(), imagePath)

        val outputStream = FileOutputStream(file)
        outputStream.use { output ->
            //buffer need to slowly read the file
            val buffer = ByteArray(4 * 1024)

            //read bytes from input stream and store it in buffer
            var byteCount = inputStream.read(buffer)

            while (byteCount > 0) {
                //Writes bytes from byte array to this file output stream
                output.write(buffer, 0, byteCount)

                byteCount = inputStream.read(buffer)
            }

            output.flush()
        }
        //created object from pairs
        //this will help to load image later as you dont have to guess the image path
        val output = workDataOf("image_path" to file.absolutePath)

        return Result.success(output)

    }
}