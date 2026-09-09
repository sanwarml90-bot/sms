package com.messagelock

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.messagelock.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPrefsHelper: SharedPrefsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPrefsHelper = SharedPrefsHelper(applicationContext)
        setupUI()
        showSavedTrustedNumber()
    }

    private fun setupUI() {
        // Set click listeners
        binding.btnSaveNumber.setOnClickListener {
            saveNumberToTrustedList()
        }
    }
    
    private fun saveNumberToTrustedList() {
        val trustedNumber = binding.etTrustedNumber.text.toString().trim()
        if (trustedNumber.isEmpty()) {
            binding.tilTrustedNumber.error = getString(R.string.empty_number_error)
            return
        }

        sharedPrefsHelper.saveTrustedNumber(trustedNumber)
        binding.tilTrustedNumber.error = null
        binding.tvCurrentNumber.text = trustedNumber
        binding.etTrustedNumber.text?.clear()
        Toast.makeText(this, R.string.number_saved_msg, Toast.LENGTH_SHORT).show()
    }

    private fun showSavedTrustedNumber() {
        binding.tvCurrentNumber.text =
            sharedPrefsHelper.getTrustedNumber() ?: getString(R.string.no_number_set)
    }
}
