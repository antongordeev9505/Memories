package com.raywenderlich.android.memories.model.response

import kotlinx.serialization.Serializable

//response from the server that we uploaded image
@Serializable
class UploadResponse(val message: String = "", val url: String = "") {
}