package com.samuel.linkedtask.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.samuel.linkedtask.R
import com.samuel.linkedtask.activity.AllActivity
import com.samuel.linkedtask.activity.EditActivity
import com.samuel.linkedtask.activity.NoteActivity
import com.samuel.linkedtask.adapter.TaskAdapter
import com.samuel.linkedtask.adapter.TaskCompletedAdapter
import com.samuel.linkedtask.database.DatabaseClient
import com.samuel.linkedtask.database.TaskDao
import com.samuel.linkedtask.database.TaskModel
import com.samuel.linkedtask.databinding.FragmentHomeBinding
import com.samuel.linkedtask.util.dateToLong
import com.samuel.linkedtask.util.dateToday
import com.samuel.linkedtask.util.dateTodayFormat

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var database: TaskDao
    private lateinit var taskSelected: TaskModel

    private lateinit var auth: FirebaseAuth
//    private lateinit var databaseRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        database = DatabaseClient.getService( requireActivity() ).taskDao()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textToday.text = dateTodayFormat()
        auth = FirebaseAuth.getInstance()
        setupList()
        setupListener()
        setupData()
    }

    override fun onStart() {
        super.onStart()
        setupData()
    }

    private fun setupList() {
        binding.listTask.adapter = taskAdapter
        binding.listTaskCompleted.adapter = taskCompletedAdapter
    }

    private fun setupListener() {
        binding.labelTaskCompleted.setOnClickListener {
            if (binding.listTaskCompleted.visibility == View.GONE) {
                binding.listTaskCompleted.visibility = View.VISIBLE
                binding.imageTaskCompleted.setImageResource(R.drawable.ic_arrow_down)
            } else {
                binding.listTaskCompleted.visibility = View.GONE
                binding.imageTaskCompleted.setImageResource(R.drawable.ic_arrow_right)
            }
        }
        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addFragment)
        }
        binding.textTask.setOnClickListener {
            startActivity(Intent(requireActivity(), AllActivity::class.java))
        }
        binding.labelNote.setOnClickListener {
            startActivity((Intent(requireActivity(), NoteActivity::class.java)))
        }
    }

    private fun setupData() {
        database.taskAll(
            uid = auth.currentUser?.uid.toString(),
            completed = false,
            date = dateToLong( dateToday() )
        ).observe(viewLifecycleOwner) {
            Log.e("taskAll", it.toString())
            taskAdapter.addList(it)
            binding.textAlert.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        }
        database.taskAll(
            uid = auth.currentUser?.uid.toString(),
            completed = true
        ).observe(viewLifecycleOwner) {
            Log.e("taskAllCompleted", it.toString())
            taskCompletedAdapter.addList(it)
            binding.labelTaskCompleted.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
            binding.imageTaskCompleted.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
        }
    }

    private val taskAdapter by lazy {

        TaskAdapter(arrayListOf(), object : TaskAdapter.AdapterListener {
            override fun onUpdate(taskModel: TaskModel) {
                taskSelected = taskModel
                taskSelected.completed = true
                Thread { database.update( taskSelected ) }.start()
            }
            override fun onDetail(taskModel: TaskModel) {
                startActivity(
                    Intent(requireActivity(), EditActivity::class.java)
                        .putExtra("intent_task", taskModel)
                )
            }
        })
    }

    private val taskCompletedAdapter by lazy {
        TaskCompletedAdapter(arrayListOf(), object : TaskCompletedAdapter.AdapterListener {
            override fun onClick(taskModel: TaskModel) {
                taskSelected = taskModel
                taskSelected.completed = false
                Thread { database.update( taskSelected ) }.start()
            }

            override fun onDetail(taskModel: TaskModel) {
                startActivity(
                    Intent(requireActivity(), EditActivity::class.java)
                        .putExtra("intent_task", taskModel)
                )
            }
        })
    }
}