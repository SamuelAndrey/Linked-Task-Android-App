
package com.samuel.linkedtask.activity

import android.os.Bundle
import com.samuel.linkedtask.databinding.ActivityAllBinding

class AllActivity : BaseActivity() {

    private lateinit var binding: ActivityAllBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}