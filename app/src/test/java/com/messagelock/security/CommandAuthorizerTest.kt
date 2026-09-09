package com.messagelock.security

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CommandAuthorizerTest {
    @Test fun `commands are case insensitive`() {
        assertEquals(SmsCommand.LOCK, CommandAuthorizer.parseCommand("lOcK"))
        assertEquals(SmsCommand.FIND, CommandAuthorizer.parseCommand("find"))
        assertEquals(SmsCommand.STOP, CommandAuthorizer.parseCommand("Stop"))
    }

    @Test fun `only complete exact command spellings are accepted`() {
        assertNull(CommandAuthorizer.parseCommand("please LOCK"))
        assertNull(CommandAuthorizer.parseCommand("LOCK now"))
        assertNull(CommandAuthorizer.parseCommand("FIND!"))
        assertNull(CommandAuthorizer.parseCommand("STOPP"))
    }

    @Test fun `only trusted numbers are authorized`() {
        assertTrue(CommandAuthorizer.isTrustedSender("7568324805"))
        assertTrue(CommandAuthorizer.isTrustedSender("+91 81122-89897"))
        assertFalse(CommandAuthorizer.isTrustedSender("7568324806"))
        assertFalse(CommandAuthorizer.isTrustedSender(null))
    }
}
