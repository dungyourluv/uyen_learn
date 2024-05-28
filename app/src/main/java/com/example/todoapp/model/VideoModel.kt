package com.example.todoapp.model

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

data class VideoModel(val uuid: String, val title: String, val url: String)

data class VideoRequest(val title: String, val video: File) {
    fun toMap(): Map<String, RequestBody> {
        val map = mutableMapOf<String, RequestBody>()
        map["title"] = title.toRequestBody()
        return map
    }

    fun getVideoPart(): MultipartBody.Part {
        return MultipartBody.Part.createFormData(
            "video",
            video.name + ".mp4",
            video.asRequestBody("video/mp4".toMediaType())
        )
    }
}