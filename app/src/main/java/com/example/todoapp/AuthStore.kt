package com.example.todoapp

import android.content.Context

class AuthStore(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    fun saveToken(token: String) {
        editor.putString("token", token)
        editor.apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString("token", null)
    }

    fun clearToken() {
        editor.remove("token")
        editor.apply()
    }

    fun setIsAdmin(isAdmin: Boolean) {
        editor.putBoolean("isAdmin", isAdmin)
        editor.apply()
    }

    fun isAdmin(): Boolean {
        return sharedPreferences.getBoolean("isAdmin", false)
    }

    fun clearIsAdmin() {
        editor.remove("isAdmin")
        editor.apply()
    }
}