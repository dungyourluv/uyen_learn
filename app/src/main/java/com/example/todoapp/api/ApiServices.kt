package com.example.todoapp.api

import com.google.gson.GsonBuilder
import retrofit2.converter.gson.GsonConverterFactory

object ApiServices {
    val gson = GsonBuilder().setLenient().create()
    val retrofit = retrofit2.Retrofit.Builder()
        .baseUrl("http://192.168.43.36:8080/api/v1/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}