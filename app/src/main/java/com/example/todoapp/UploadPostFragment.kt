package com.example.todoapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.example.todoapp.api.TaskServices
import com.example.todoapp.databinding.FragmentUploadPostBinding
import com.example.todoapp.model.TaskModel
import com.example.todoapp.model.TaskRequest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UploadPostFragment : AuthFragment() {
    private lateinit var binding: FragmentUploadPostBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUploadPostBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val authStore = AuthStore(requireContext())
        val selectImageLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                binding.ivImage.setImageURI(uri)
            }

        binding.btnSelectImage.setOnClickListener {
            selectImageLauncher.launch("image/*")
        }

        binding.btnUpload.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val description = binding.etDesc.text.toString()
            val image = binding.ivImage.drawable
            val file = Utils.drawableToFile(image)
            val token = authStore.getToken()!!
            val taskRequest = TaskRequest(title, description, listOf(file))
            val taskService = TaskServices.getInstance()

            val call = taskService.postTask(token, taskRequest.toMapPart(), taskRequest.images.map {
                MultipartBody.Part.createFormData(
                    "images[]",
                    it.name,
                    it.asRequestBody("image/*".toMediaType())
                )
            })

            it.isEnabled = false
            call.enqueue(object : Callback<TaskModel> {
                override fun onResponse(p0: Call<TaskModel>, p1: Response<TaskModel>) {
                    it.isEnabled = true
                    if (p1.isSuccessful) {
                        val task = p1.body()!!
                        Utils.showToast(requireContext(), "Task uploaded successfully")
                    } else {
                        Utils.showToast(requireContext(), "Failed to upload task")
                    }
                }

                override fun onFailure(p0: Call<TaskModel>, p1: Throwable) {
                    it.isEnabled = true
                    Utils.showToast(requireContext(), "Failed to upload task")
                }
            })
        }

    }
}