package com.example.todoapp

import android.app.AlertDialog
import android.icu.text.UnicodeSet.SpanCondition
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.api.TaskServices
import com.example.todoapp.databinding.FragmentHomeBinding
import com.example.todoapp.model.TaskModel
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }

        binding.btnLogout.setOnClickListener {
            val authStore = AuthStore(requireContext())
            authStore.clearToken()
            authStore.clearIsAdmin()
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }

        val adapter = RecycleAdapter(listOf())

        binding.rvTasks.adapter = adapter
        binding.rvTasks.layoutManager = LinearLayoutManager(requireContext())

        val authStore = AuthStore(requireContext())
        val taskService = TaskServices.getInstance()
        if (authStore.getToken() != null) {

            binding.btnGetTasks.setOnClickListener {
            val call = taskService.getTasks(authStore.getToken()!!)
                call.enqueue(object : Callback<List<TaskModel>> {
                    override fun onResponse(
                        p0: Call<List<TaskModel>>,
                        p1: Response<List<TaskModel>>
                    ) {
                        if (p1.isSuccessful) {
                            val tasks = p1.body()
                            if (tasks != null) {
                                adapter.list = tasks
                                adapter.notifyDataSetChanged()
                            }
                        } else {
                            if (p1.code() == 401) {
                                findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
                            }

                            val error = p1.errorBody()?.string()
                            val json = Gson().fromJson(error, Map::class.java)
                            val message = json["message"]
                            AlertDialog.Builder(requireContext())
                                .setTitle("Error")
                                .setMessage(message.toString())
                                .setPositiveButton("OK", null)
                                .show()

                        }
                    }

                    override fun onFailure(p0: Call<List<TaskModel>>, p1: Throwable) {
                        p1.printStackTrace()
                    }

                })
            }
        }


    }
}