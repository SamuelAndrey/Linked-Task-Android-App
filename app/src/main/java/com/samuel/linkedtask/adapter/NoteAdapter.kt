package com.samuel.linkedtask.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.samuel.linkedtask.database.NoteModel
import com.samuel.linkedtask.databinding.AdapterNoteBinding


class NoteAdapter(private val list: MutableList<NoteModel>) : RecyclerView.Adapter<NoteAdapter.TaskViewHolder>() {

    private val TAG = "NoteAdapter"
    private var listener: TaskAdapterInterface? = null
    fun setListener(listener: TaskAdapterInterface) {
        this.listener = listener
    }

    class TaskViewHolder(val binding: AdapterNoteBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = AdapterNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        with(holder) {
            with(list[position]) {
                binding.todoTask.text = this.task

                Log.d(TAG, "onBindViewHolder: " + this)
                holder.itemView.setOnClickListener {
                    listener?.onEditItemClicked(this, position)
                }

                binding.deleteTask.setOnClickListener {
                    listener?.onDeleteItemClicked(this, position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface TaskAdapterInterface {
        fun onDeleteItemClicked(toDoData: NoteModel, position: Int)
        fun onEditItemClicked(toDoData: NoteModel, position: Int)
    }
}
