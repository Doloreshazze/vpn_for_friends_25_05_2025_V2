package com.playeverywhere999.vpn_for_friends_25_05_2025_v2

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class VpnStateHolderTest {

    @Test
    fun `initial state is DISCONNECTED`() = runTest {
        assertEquals(VpnState.DISCONNECTED, VpnStateHolder.vpnState.first())
    }

    @Test
    fun `updateState changes the state`() = runTest {
        // Initial state check (optional, good for sanity)
        assertEquals(VpnState.DISCONNECTED, VpnStateHolder.vpnState.first())

        VpnStateHolder.updateState(VpnState.CONNECTING)
        assertEquals(VpnState.CONNECTING, VpnStateHolder.vpnState.first())

        VpnStateHolder.updateState(VpnState.CONNECTED)
        assertEquals(VpnState.CONNECTED, VpnStateHolder.vpnState.first())

        VpnStateHolder.updateState(VpnState.ERROR)
        assertEquals(VpnState.ERROR, VpnStateHolder.vpnState.first())

        VpnStateHolder.updateState(VpnState.DISCONNECTED)
        assertEquals(VpnStateHolder.vpnState.first(), VpnState.DISCONNECTED) // Fixed assertion order
    }

    @Test
    fun `updateState with same state`() = runTest {
        VpnStateHolder.updateState(VpnState.CONNECTED) // Set a known state
        assertEquals(VpnState.CONNECTED, VpnStateHolder.vpnState.first())

        VpnStateHolder.updateState(VpnState.CONNECTED) // Update with the same state
        assertEquals(VpnState.CONNECTED, VpnStateHolder.vpnState.first())
    }
}
