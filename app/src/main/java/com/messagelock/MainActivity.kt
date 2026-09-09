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
        // Set click listeners
        binding.btnSaveNumber.setOnClickListener {
            saveNumberToTrustedList()
        }
    }
    
    private fun saveNumberToTrustedList() {
        val trustedNumber = binding.etTrustedNumber.text.toString().trim()
        if (trustedNumber.isNotEmpty()) {
            binding.tvCurrentNumber.text = trustedNumber
            binding.etTrustedNumber.text = null
        } else {
            binding.tvCurrentNumber.text = getString(R.string.no_number_set)
        }
    }
}
