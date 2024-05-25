package com.example.todoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.model.TaskModel

class RecycleAdapter(var list: List<TaskModel>) :
    RecyclerView.Adapter<RecycleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = list[position]
        holder.title.text = task.title
        holder.desc.text = task.description
        if (task.images.isNotEmpty()) {
            holder.img.visibility = View.VISIBLE
            val image = task.images[0]
            Utils.loadImageFromUrl(image, holder.img)
        } else {
            holder.img.visibility = View.GONE
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.findViewById<TextView>(R.id.tv_title)
        val desc = view.findViewById<TextView>(R.id.tv_description)
        val img = view.findViewById<ImageView>(R.id.iv_image)
    }
}