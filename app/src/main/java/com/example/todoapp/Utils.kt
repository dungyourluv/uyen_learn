package com.example.todoapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

object Utils {
    fun loadImageFromUrl(url: String, imageView: ImageView) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            try {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.connect()
                val input = connection.inputStream
                val bitmapDrawable =
                    BitmapDrawable(imageView.resources, BitmapFactory.decodeStream(input))
                withContext(Dispatchers.Main) {
                    imageView.setImageDrawable(bitmapDrawable)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                throw e
            }
        }
    }

    fun drawableToFile(drawable: Drawable): File {
        val bitmap = (drawable as BitmapDrawable).bitmap
        val file = File.createTempFile("image", ".png")
        file.outputStream().use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }
        return file
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}