package com.example.todoapp.model

data class UserLogin(val username: String, val password: String)

data class UserModel(
    val id: String,
    val username: String,
    val password: String,
    val isAdmin: Boolean
)