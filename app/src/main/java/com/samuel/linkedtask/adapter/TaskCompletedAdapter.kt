package com.samuel.linkedtask.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.samuel.linkedtask.database.TaskModel
import com.samuel.linkedtask.databinding.AdapterTaskCompletedBinding
import com.samuel.linkedtask.util.dateToString

class TaskCompletedAdapter(
    var items: ArrayList<TaskModel>,
    var listener: AdapterListener
): RecyclerView.Adapter<TaskCompletedAdapter.ViewHolder>() {

    class ViewHolder(val binding: AdapterTaskCompletedBinding) : RecyclerView.ViewHolder( binding.root )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        AdapterTaskCompletedBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.textTask.text = item.task
        holder.binding.textTask.paintFlags = holder.binding.textTask.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        holder.binding.textDate.text = dateToString(item.date)
        holder.binding.imageTask.setOnClickListener {
            listener.onClick( item )
        }
        holder.itemView.setOnClickListener {
            listener.onDetail( item )
        }
    }

    override fun getItemCount() = items.size

    fun addList(list: List<TaskModel>) {
        items.clear()
        items.addAll( list )
        notifyDataSetChanged()
    }

    interface AdapterListener {
        fun onClick( taskModel: TaskModel )
        fun onDetail( taskModel: TaskModel )
    }


}