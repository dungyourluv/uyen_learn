package com.example.todoapp.api

import com.example.todoapp.model.ProductModel
import com.example.todoapp.model.VideoModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

interface ProductServices {

    @POST("product/add")
    @Multipart
    fun uploadProduct(
        @Header("Authorization") authorization: String,
        @PartMap data: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part image: MultipartBody.Part
    ): Call<ProductModel>

    @POST("api/v1/videos")
    @Multipart
    fun uploadVideo(
        @PartMap data: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part video: MultipartBody.Part
    ): Call<VideoModel>

    companion object {
        fun getInstance(): ProductServices {
            return ApiServices.uRetrofit.create(ProductServices::class.java)
        }
    }
}