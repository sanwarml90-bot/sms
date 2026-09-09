package com.messagelock

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    private lateinit var prefsHelper: SharedPrefsHelper
    private lateinit var tvCurrentNumber: TextView
    private lateinit var etTrustedNumber: TextInputEditText
    private lateinit var btnSaveNumber: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefsHelper = SharedPrefsHelper(this)

        // UI elements link karna
        tvCurrentNumber = findViewById(R.id.tvCurrentNumber)
        etTrustedNumber = findViewById(R.id.etTrustedNumber)
        btnSaveNumber = findViewById(R.id.btnSaveNumber)

        // Pehle se save kiya hua number dikhana
        updateCurrentNumberDisplay()

        // Button click logic
        btnSaveNumber.setOnClickListener {
            val number = etTrustedNumber.text.toString().trim()
            
            if (number.isNotEmpty()) {
                prefsHelper.saveTrustedNumber(number)
                updateCurrentNumberDisplay()
                Toast.makeText(this, getString(R.string.number_saved_msg), Toast.LENGTH_SHORT).show()
                etTrustedNumber.text?.clear() // Text box clear karna
            } else {
                Toast.makeText(this, getString(R.string.empty_number_error), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateCurrentNumberDisplay() {
        val savedNumber = prefsHelper.getTrustedNumber()
        if (savedNumber != null && savedNumber.isNotEmpty()) {
            tvCurrentNumber.text = savedNumber
        } else {
            tvCurrentNumber.text = getString(R.string.no_number_set)
        }
    }
}
