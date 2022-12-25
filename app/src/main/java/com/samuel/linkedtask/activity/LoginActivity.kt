package com.samuel.linkedtask.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.samuel.linkedtask.R
import com.samuel.linkedtask.databinding.ActivityEditBinding
import com.samuel.linkedtask.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}