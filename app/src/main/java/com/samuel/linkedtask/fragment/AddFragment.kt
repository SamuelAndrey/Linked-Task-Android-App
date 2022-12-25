package com.samuel.linkedtask.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.samuel.linkedtask.database.DatabaseClient
import com.samuel.linkedtask.database.TaskDao
import com.samuel.linkedtask.database.TaskModel
import com.samuel.linkedtask.databinding.FragmentAddBinding
import com.samuel.linkedtask.util.dateToDialog
import com.samuel.linkedtask.util.dateToLong
import com.samuel.linkedtask.util.dateToString
import com.samuel.linkedtask.util.dateToday

class AddFragment : Fragment() {

    private lateinit var binding: FragmentAddBinding
    private lateinit var database: TaskDao

    private lateinit var auth: FirebaseAuth
//    private lateinit var databaseRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate( inflater, container, false)
        database = DatabaseClient.getService( requireActivity() ).taskDao()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textDate.text = dateToday()
        binding.labelDate.setOnClickListener {
            val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                binding.textDate.text = dateToString(year, month, dayOfMonth)
            }
            dateToDialog( requireActivity(), datePicker ).show()
        }

        binding.buttonSave.setOnClickListener {

            auth = FirebaseAuth.getInstance()
//            databaseRef = FirebaseDatabase.getInstance("https://linked-task-default-rtdb.asia-southeast1.firebasedatabase.app/")
//                .reference.child("Todo").child(auth.currentUser?.uid.toString())

            val task = TaskModel (
                0,
                auth.currentUser?.uid.toString(),
                binding.editTask.text.toString(),
                false,
                dateToLong(binding.textDate.text.toString())
            )
//            val task2 = RealtimeModel (
//                binding.editTask.text.toString(),
//                false,
//                dateToLong(binding.textDate.text.toString())
//            )

            Thread {
                database.insert( task )
//                databaseRef.push().setValue(task2)

                requireActivity().runOnUiThread {
                    Toast.makeText(requireActivity(), "Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
            }.start()
        }
    }

}