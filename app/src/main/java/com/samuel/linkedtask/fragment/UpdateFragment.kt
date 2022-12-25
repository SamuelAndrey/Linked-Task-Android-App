package com.samuel.linkedtask.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.samuel.linkedtask.R
import com.samuel.linkedtask.database.DatabaseClient
import com.samuel.linkedtask.database.TaskDao
import com.samuel.linkedtask.database.TaskModel
import com.samuel.linkedtask.databinding.FragmentUpdateBinding
import com.samuel.linkedtask.util.dateToDialog
import com.samuel.linkedtask.util.dateToLong
import com.samuel.linkedtask.util.dateToString

class UpdateFragment : Fragment() {

    private lateinit var binding: FragmentUpdateBinding
    private lateinit var database: TaskDao
    private lateinit var detail: TaskModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateBinding.inflate(inflater, container, false)
        database = DatabaseClient.getService( requireActivity() ).taskDao()
        detail = requireArguments().getSerializable("argument_task") as TaskModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupData()
        setupListener()
    }

    private fun setupData() {
        binding.editTask.setText(detail.task)
        binding.textDate.text = dateToString(detail.date)
    }

    private fun setupListener() {
        binding.labelDate.setOnClickListener {
            val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                binding.textDate.text = dateToString(year, month, dayOfMonth)
            }
            dateToDialog( requireActivity(), datePicker ).show()
        }
        binding.buttonSave.setOnClickListener {

            detail.task = binding.editTask.text.toString()
            detail.date = dateToLong( binding.textDate.text.toString() )
            Thread {
                database.update(detail)
                requireActivity().runOnUiThread {
                    Toast.makeText(requireActivity(), "Perubahan Disimpan", Toast.LENGTH_SHORT).show()
                    requireActivity().finish()
                }

            }.start()
        }
        binding.buttonDelete.setOnClickListener {
            Thread {
                database.delete(detail)
                requireActivity().runOnUiThread {
                    Toast.makeText(requireActivity(), "Berhasil Dihapus", Toast.LENGTH_SHORT).show()
                    requireActivity().finish()
                }

            }.start()
        }
    }

}