package com.example.todoapp.model

import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

object TaskStatus {
    const val PENDING = "pending"
    const val DONE = "done"
}

data class TaskModel(
    val id: String,
    val title: String,
    val description: String,
    val status: String,
    val user: UserModel,
    val images: List<String>
)

data class TaskRequest(val title: String, val description: String, val images: List<File>) {
    fun toMapPart(): Map<String, RequestBody> {
        val map = mutableMapOf<String, RequestBody>()
        map["title"] = title.toRequestBody()
        map["description"] = description.toRequestBody()
        return map
    }
}