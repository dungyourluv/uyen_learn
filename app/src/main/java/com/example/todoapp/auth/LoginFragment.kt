package com.example.todoapp.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.example.todoapp.AuthStore
import com.example.todoapp.R
import com.example.todoapp.api.AuthService
import com.example.todoapp.databinding.ActivityMainBinding
import com.example.todoapp.databinding.FragmentLoginBinding
import com.example.todoapp.model.UserLogin
import com.example.todoapp.model.UserModel
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnGotoRegister.setOnClickListener {
            findNavController().navigate("register")
        }

        val authStore = AuthStore(requireContext())

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            val userLogin = UserLogin(
                username,
                password
            )
            val call = AuthService.getInstance().login(
                userLogin
            )
            call.enqueue(object : Callback<UserModel> {
                override fun onResponse(p0: Call<UserModel>, p1: Response<UserModel>) {
                    if (p1.isSuccessful) {
                        val userModel = p1.body()
                        if (userModel != null) {
                            val base64Encode = android.util.Base64.encodeToString(
                                "$username:$password".toByteArray(),
                                android.util.Base64.NO_WRAP
                            )
                            authStore.saveToken("Basic $base64Encode")
                            authStore.setIsAdmin(userModel.isAdmin)

                            AlertDialog.Builder(requireContext())
                                .setTitle("Success")
                                .setMessage("Login success")
                                .setPositiveButton("OK") { _, _ ->
                                    findNavController().navigate(R.id.homeFragment)
                                }
                                .show()
                        } else {
                            AlertDialog.Builder(requireContext())
                                .setTitle("Error")
                                .setMessage("Login failed 1")
                                .setPositiveButton("OK", null)
                                .show()
                        }
                    } else {
                        AlertDialog.Builder(requireContext())
                            .setTitle("Error")
                            .setMessage("Login failed 2")
                            .setPositiveButton("OK", null)
                            .show()
                    }
                }

                override fun onFailure(p0: Call<UserModel>, p1: Throwable) {
                    p1.printStackTrace()
                    AlertDialog.Builder(requireContext())
                        .setTitle("Error")
                        .setMessage("Login failed")
                        .setPositiveButton("OK", null)
                        .show()
                }
            })

        }
    }

}