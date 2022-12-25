package com.samuel.linkedtask.activity

import android.os.Bundle
import com.samuel.linkedtask.databinding.ActivityNoteBinding

class NoteActivity : BaseActivity() {
    private lateinit var binding: ActivityNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteBinding.inflate( layoutInflater )
        setContentView( binding.root )

    }
}