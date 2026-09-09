package com.messagelock

import android.Manifest
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.messagelock.databinding.ActivityMainBinding
import com.messagelock.security.CommandAuthorizer
import com.messagelock.security.MessageLockAdminReceiver
import com.messagelock.security.SirenService

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val permissionRequest = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { updateStatus() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvTrustedNumbers.text = CommandAuthorizer.trustedNumbers().joinToString(separator = "\n")
        binding.btnDeviceAdmin.setOnClickListener { requestDeviceAdmin() }
        binding.btnTestLock.setOnClickListener { testLock() }
        binding.btnTestSiren.setOnClickListener { SirenService.start(this); updateStatus() }
        binding.btnStopSiren.setOnClickListener { SirenService.stop(this); updateStatus() }
        requestRequiredPermissions()
    }

    override fun onResume() { super.onResume(); updateStatus() }

    private fun requestRequiredPermissions() {
        val permissions = buildList {
            if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) add(Manifest.permission.RECEIVE_SMS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) add(Manifest.permission.POST_NOTIFICATIONS)
        }
        if (permissions.isNotEmpty()) permissionRequest.launch(permissions.toTypedArray())
    }

    private fun requestDeviceAdmin() {
        val admin = ComponentName(this, MessageLockAdminReceiver::class.java)
        startActivity(Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, admin).putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, getString(R.string.device_admin_explanation)))
    }

    private fun testLock() {
        val manager = getSystemService(DevicePolicyManager::class.java)
        val admin = ComponentName(this, MessageLockAdminReceiver::class.java)
        if (manager.isAdminActive(admin)) manager.lockNow() else Toast.makeText(this, R.string.device_admin_required, Toast.LENGTH_LONG).show()
    }

    private fun updateStatus() {
        val smsGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED
        binding.tvMonitoringStatus.text = getString(if (smsGranted) R.string.monitoring_active else R.string.monitoring_permission_needed)
        val manager = getSystemService(DevicePolicyManager::class.java)
        binding.tvLockStatus.text = getString(if (manager.isAdminActive(ComponentName(this, MessageLockAdminReceiver::class.java))) R.string.lock_ready else R.string.lock_not_ready)
        binding.tvSirenStatus.text = getString(if (SharedPrefsHelper(this).isSirenActive()) R.string.siren_active else R.string.siren_stopped)
    }
}
