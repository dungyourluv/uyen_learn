package com.example.todoapp.api

import retrofit2.Call
import com.example.todoapp.model.TaskModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

interface TaskServices {
    @GET("tasks")
    fun getTasks(@Header("Authorization") authorization: String): Call<List<TaskModel>>

    @POST("tasks/upload")
    @Multipart
    fun postTask(
        @Header("Authorization") authorization: String,
        @PartMap data: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part images:
        List<MultipartBody.Part>
    ): Call<TaskModel>

    companion object {
        fun getInstance(): TaskServices {
            return ApiServices.retrofit.create(TaskServices::class.java)
        }
    }
}