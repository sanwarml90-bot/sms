package com.messagelock

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    private lateinit var tvCurrentNumber: TextView
    private lateinit var etTrustedNumber: TextInputEditText
    private lateinit var btnSaveNumber: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // UI elements initialize karna
        tvCurrentNumber = findViewById(R.id.tvCurrentNumber)
        etTrustedNumber = findViewById(R.id.etTrustedNumber)
        btnSaveNumber = findViewById(R.id.btnSaveNumber)

        // Button click logic
        btnSaveNumber.setOnClickListener {
            val number = etTrustedNumber.text.toString().trim()
            
            if (number.isNotEmpty()) {
                tvCurrentNumber.text = number
                etTrustedNumber.text = null
                Toast.makeText(this, "Number saved successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter a valid number", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
