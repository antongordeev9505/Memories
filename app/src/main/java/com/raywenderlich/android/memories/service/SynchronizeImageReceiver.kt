package com.raywenderlich.android.memories.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

const val ACTION_IMAGES_SYNCHRONIZED = "images_synchronized"

class SynchronizeImageReceiver(private inline val onImagesSynchronized: () -> Unit) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        //if receive intent with the action images_synchronized - invoke callback function
        if(intent?.action == ACTION_IMAGES_SYNCHRONIZED) {
            onImagesSynchronized()
        }
    }
}