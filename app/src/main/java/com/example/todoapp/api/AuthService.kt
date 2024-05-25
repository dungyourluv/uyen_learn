package com.example.todoapp.api

import com.example.todoapp.model.UserLogin
import com.example.todoapp.model.UserModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("users/login")
    fun login(@Body info: UserLogin): Call<UserModel>

    companion object {
        fun getInstance(): AuthService {
            return ApiServices.retrofit.create(AuthService::class.java)
        }
    }
}