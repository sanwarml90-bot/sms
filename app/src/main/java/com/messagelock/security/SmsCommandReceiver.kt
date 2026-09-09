package com.messagelock.security

import android.app.admin.DevicePolicyManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.provider.Telephony

class SmsCommandReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != "android.provider.Telephony.SMS_RECEIVED") return
        val pendingResult = goAsync()
        try {
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            messages.forEach { message ->
                if (!CommandAuthorizer.isTrustedSender(message.originatingAddress)) return@forEach
                when (CommandAuthorizer.parseCommand(message.messageBody)) {
                    SmsCommand.LOCK -> lockDevice(context)
                    SmsCommand.FIND -> SirenService.start(context)
                    SmsCommand.STOP -> SirenService.stop(context)
                    null -> Unit
                }
            }
        } finally {
            pendingResult.finish()
        }
    }

    private fun lockDevice(context: Context) {
        val manager = context.getSystemService(DevicePolicyManager::class.java)
        val admin = ComponentName(context, MessageLockAdminReceiver::class.java)
        if (manager.isAdminActive(admin)) manager.lockNow()
    }
}
