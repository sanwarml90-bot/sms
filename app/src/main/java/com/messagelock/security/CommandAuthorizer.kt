package com.messagelock.security

import java.util.Locale

/** Parses only complete SMS command bodies and authorizes known owners. */
object CommandAuthorizer {
    private val trustedNumbers = setOf("7568324805", "8112289897")

    fun parseCommand(body: String?): SmsCommand? = when (body?.trim()?.uppercase(Locale.ROOT)) {
        "LOCK" -> SmsCommand.LOCK
        "FIND" -> SmsCommand.FIND
        "STOP" -> SmsCommand.STOP
        else -> null
    }

    fun isTrustedSender(address: String?): Boolean {
        val digits = address.orEmpty().filter(Char::isDigit)
        // SMS providers may include the country code; compare the configured 10-digit number.
        val localNumber = digits.takeLast(10)
        return localNumber in trustedNumbers
    }

    fun trustedNumbers(): List<String> = trustedNumbers.toList().sorted()
}
