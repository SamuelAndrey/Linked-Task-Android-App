package com.samuel.linkedtask.activity

import android.os.Bundle
import com.samuel.linkedtask.databinding.ActivityEditBinding

class EditActivity : BaseActivity() {
    private lateinit var binding: ActivityEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}