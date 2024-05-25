package com.example.todoapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

open class AuthFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val authStore = AuthStore(requireContext())
        val token = authStore.getToken()
        if (token == null) {
            findNavController().navigate(R.id.loginFragment)
        }
        super.onCreate(savedInstanceState)
    }
}