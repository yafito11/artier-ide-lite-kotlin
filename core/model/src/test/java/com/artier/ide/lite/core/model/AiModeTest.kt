package com.artier.ide.lite.core.model

import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for AiMode model
 */
class AiModeTest {

    @Test
    fun agentMode_hasCorrectPermissions() {
        val permissions = AiPermissions.forMode(AiMode.AGENT)
        assertTrue(permissions.read)
        assertTrue(permissions.edit)
        assertTrue(permissions.bash)
        assertTrue(permissions.mcpRead)
        assertTrue(permissions.mcpWrite)
    }

    @Test
    fun gatherMode_hasCorrectPermissions() {
        val permissions = AiPermissions.forMode(AiMode.GATHER)
        assertTrue(permissions.read)
        assertFalse(permissions.edit)
        assertFalse(permissions.bash)
        assertTrue(permissions.mcpRead)
        assertFalse(permissions.mcpWrite)
    }

    @Test
    fun chatMode_hasNoPermissions() {
        val permissions = AiPermissions.forMode(AiMode.CHAT)
        assertFalse(permissions.read)
        assertFalse(permissions.edit)
        assertFalse(permissions.bash)
        assertFalse(permissions.mcpRead)
        assertFalse(permissions.mcpWrite)
    }

    @Test
    fun aiMode_values_areCorrect() {
        val modes = AiMode.entries
        assertEquals(3, modes.size)
        assertTrue(modes.contains(AiMode.AGENT))
        assertTrue(modes.contains(AiMode.GATHER))
        assertTrue(modes.contains(AiMode.CHAT))
    }
}
