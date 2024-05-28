package com.example.todoapp

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.example.todoapp.api.ProductServices
import com.example.todoapp.databinding.FragmentVideoUploadBinding
import com.example.todoapp.model.VideoModel
import com.example.todoapp.model.VideoRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class VideoUploadFragment : Fragment() {
    private lateinit var binding: FragmentVideoUploadBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentVideoUploadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var videoUri: Uri? = null
        val pickVideoLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                // Handle the returned Uri
                videoUri = uri!!
                binding.videoView.setVideoURI(uri)
                binding.videoView.start()
            }

        binding.btnSelectVideo.setOnClickListener {
            pickVideoLauncher.launch("video/mp4")
        }

        binding.btnUpload.setOnClickListener {
            if (videoUri != null) {
                val file = Utils.uriToFile(requireContext(), videoUri!!)
                val videoReq = VideoRequest(title = "Abc", video = file)
                val call = ProductServices.getInstance()
                    .uploadVideo(videoReq.toMap(), videoReq.getVideoPart())
                call.enqueue(object : Callback<VideoModel> {
                    override fun onResponse(p0: Call<VideoModel>, p1: Response<VideoModel>) {
                        if (p1.isSuccessful) {
                            val video = p1.body()
                            if (video != null) {
                                Utils.showToast(requireContext(), "Video uploaded successfully")
                            }
                        } else {
                            Utils.showToast(requireContext(), "Failed to upload video")
                        }

                    }

                    override fun onFailure(p0: Call<VideoModel>, p1: Throwable) {
                        p1.printStackTrace()
                        Utils.showToast(requireContext(), "Failed to upload video 2")
                    }
                })
            }
        }
    }

}