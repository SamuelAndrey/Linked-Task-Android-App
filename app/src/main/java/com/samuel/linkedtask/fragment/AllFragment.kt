package com.samuel.linkedtask.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.samuel.linkedtask.R
import com.samuel.linkedtask.activity.*
import com.samuel.linkedtask.adapter.TaskAdapter
import com.samuel.linkedtask.adapter.TaskCompletedAdapter
import com.samuel.linkedtask.database.DatabaseClient
import com.samuel.linkedtask.database.TaskDao
import com.samuel.linkedtask.database.TaskModel
import com.samuel.linkedtask.databinding.FragmentAllBinding

class AllFragment : Fragment() {

    private lateinit var binding: FragmentAllBinding
    private lateinit var database: TaskDao
    private lateinit var taskSelected: TaskModel

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllBinding.inflate(inflater, container, false)
        database = DatabaseClient.getService( requireActivity() ).taskDao()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        binding.imageMenu.setOnClickListener {  imageMenu ->
            PopupMenu(requireActivity(), imageMenu ).apply {
                setOnMenuItemClickListener { item ->
                    when(item?.itemId) {
                        R.id.action_note -> {
                            startActivity(Intent(requireActivity(), NoteActivity::class.java))
                            true
                        }
                        R.id.action_new -> {
                            findNavController().navigate(R.id.action_allFragment_to_addFragment)
                            true
                        }
                        R.id.action_delete -> {
                            Thread { database.deleteCompleted( auth.currentUser?.uid.toString()) }.start()
                            true
                        }
                        R.id.action_delete_all -> {
                            Thread { database.deleteAll( auth.currentUser?.uid.toString()) }.start()
                            true
                        }
                        R.id.action_logout -> {
                            FirebaseAuth.getInstance().signOut()
                            startActivity(
                                Intent(requireActivity(), LoginActivity::class.java)
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            )
                            true
                        }
                            else -> false
                    }
                }
                inflate(R.menu.menu_task_all)
                show()
            }
        }
        binding.labelTaskCompleted.setOnClickListener {
            if (binding.listTaskCompleted.visibility == View.GONE) {
                binding.listTaskCompleted.visibility = View.VISIBLE
                binding.imageTaskCompleted.setImageResource(R.drawable.ic_arrow_down)
            } else {
                binding.listTaskCompleted.visibility = View.GONE
                binding.imageTaskCompleted.setImageResource(R.drawable.ic_arrow_right)
            }
        }
    }

    private fun setupData() {
        database.taskAll( auth.currentUser?.uid.toString(),false).observe(viewLifecycleOwner) {
            Log.e("taskAll", it.toString())
            taskAdapter.addList(it)
            binding.textAlert.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        }
        database.taskAll( auth.currentUser?.uid.toString(),true).observe(viewLifecycleOwner) {
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