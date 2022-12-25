package com.samuel.linkedtask.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.samuel.linkedtask.adapter.NoteAdapter
import com.samuel.linkedtask.database.NoteModel
import com.samuel.linkedtask.databinding.FragmentNoteBinding

class NoteFragment : Fragment(), NoteDialogFragment.OnDialogNextBtnClickListener,
    NoteAdapter.TaskAdapterInterface {

    private val TAG = "NoteFragment"
    private lateinit var binding: FragmentNoteBinding
    private lateinit var database: DatabaseReference
    private var frag: NoteDialogFragment? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var authId: String

    private lateinit var taskAdapter: NoteAdapter
    private lateinit var toDoItemList: MutableList<NoteModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

        //get data from firebase
        getTaskFromFirebase()


        binding.addTaskBtn.setOnClickListener {

            if (frag != null)
                childFragmentManager.beginTransaction().remove(frag!!).commit()
            frag = NoteDialogFragment()
            frag!!.setListener(this)

            frag!!.show(
                childFragmentManager,
                NoteDialogFragment.TAG
            )

        }
    }

    private fun getTaskFromFirebase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                toDoItemList.clear()
                for (taskSnapshot in snapshot.children) {
                    val todoTask = taskSnapshot.key?.let { NoteModel(it, taskSnapshot.value.toString()) }

                    if (todoTask != null) {
                        toDoItemList.add(todoTask)
                    }
                }
                Log.d(TAG, "onDataChange: " + toDoItemList)
                taskAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
            }


        })
    }

    private fun init() {

        auth = FirebaseAuth.getInstance()
        authId = auth.currentUser!!.uid

        database = FirebaseDatabase.getInstance("https://linked-task-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .reference.child("Task").child(authId)

        binding.mainRecyclerView.setHasFixedSize(true)
        binding.mainRecyclerView.layoutManager = LinearLayoutManager(context)

        toDoItemList = mutableListOf()
        taskAdapter = NoteAdapter(toDoItemList)
        taskAdapter.setListener(this)
        binding.mainRecyclerView.adapter = taskAdapter
    }

    override fun saveTask(todoTask: String, todoEdit: EditText) {

        database
            .push().setValue(todoTask)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(context, "Task Added Successfully", Toast.LENGTH_SHORT).show()
                    todoEdit.text = null

                } else {
                    Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        frag!!.dismiss()
    }

    override fun updateTask(toDoData: NoteModel, todoEdit: EditText) {
        val map = HashMap<String, Any>()
        map[toDoData.taskId] = toDoData.task
        database.updateChildren(map).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
            frag!!.dismiss()
        }
    }

    override fun onDeleteItemClicked(toDoData: NoteModel, position: Int) {
        database.child(toDoData.taskId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onEditItemClicked(toDoData: NoteModel, position: Int) {
        if (frag != null)
            childFragmentManager.beginTransaction().remove(frag!!).commit()

        frag = NoteDialogFragment.newInstance(toDoData.taskId, toDoData.task)
        frag!!.setListener(this)
        frag!!.show(childFragmentManager, NoteDialogFragment.TAG)
    }

}