package com.samuel.linkedtask.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.samuel.linkedtask.database.NoteModel
import com.samuel.linkedtask.databinding.FragmentNoteDialogBinding


class NoteDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentNoteDialogBinding
    private var listener: OnDialogNextBtnClickListener? = null
    private var toDoData: NoteModel? = null

    fun setListener(listener: OnDialogNextBtnClickListener) {
        this.listener = listener
    }

    companion object {
        const val TAG = "NoteDialogFragment"

        @JvmStatic
        fun newInstance(taskId: String, task: String) =
            NoteDialogFragment().apply {
                arguments = Bundle().apply {
                    putString("taskId", taskId)
                    putString("task", task)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoteDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            toDoData = NoteModel(
                arguments?.getString("taskId").toString(),
                arguments?.getString("task").toString()
            )
            binding.todoEt.setText(toDoData?.task)
        }

//        binding.todoClose.setOnClickListener {
//            dismiss()
//        }

        binding.todoNextBtn.setOnClickListener {

            val todoTask = binding.todoEt.text.toString()
            if (todoTask.isNotEmpty()) {
                if (toDoData == null) {
                    listener?.saveTask(todoTask, binding.todoEt)
                } else {
                    toDoData!!.task = todoTask
                    listener?.updateTask(toDoData!!, binding.todoEt)
                }
            }
        }
    }

    interface OnDialogNextBtnClickListener {
        fun saveTask(todoTask: String, todoEdit: EditText)
        fun updateTask(toDoData: NoteModel, todoEdit: EditText)
    }

}