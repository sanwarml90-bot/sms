package com.messagelock

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.messagelock.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupUI()
    }

    private fun setupUI() {
        // Initialize your UI here
        binding.textMessage.text = "Welcome to MessageLock"
    }
}