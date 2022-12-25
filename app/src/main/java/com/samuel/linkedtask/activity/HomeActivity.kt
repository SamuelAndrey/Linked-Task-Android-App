package com.samuel.linkedtask.activity

import android.os.Bundle
import com.samuel.linkedtask.databinding.ActivityHomeBinding

class HomeActivity : BaseActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate( layoutInflater )
        setContentView( binding.root )

    }
}