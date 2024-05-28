package com.example.todoapp.model

import android.content.Context
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

data class ProductModel(
    val productId: String,
    val productName: String,
    val originalPrice: Double,
    val discountPercentage: Double,
    val discountedPrice: Double,
    val image: String
)

data class ProductModelRequest(
    val productName: String,
    val originalPrice: Double,
    val discountPercentage: Double,
    val discountedPrice: Double,
    val image: File
) {
    fun toMap(): Map<String, RequestBody> {
        val map = mutableMapOf<String, RequestBody>()
        map["productname"] = productName.toRequestBody()
        map["original_price"] = originalPrice.toString().toRequestBody()
        map["discount_percentage"] = discountPercentage.toString().toRequestBody()
        map["discounted_price"] = discountedPrice.toString().toRequestBody()
        return map
    }

    fun getImagePart(): MultipartBody.Part {
        return MultipartBody.Part.createFormData(
            "image",
            image.name,
            image.asRequestBody("image/*".toMediaType())
        )
    }
}