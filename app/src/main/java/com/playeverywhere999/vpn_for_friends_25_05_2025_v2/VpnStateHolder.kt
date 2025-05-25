package com.playeverywhere999.vpn_for_friends_25_05_2025_v2

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class VpnState {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    ERROR
}

object VpnStateHolder {
    private val _vpnState = MutableStateFlow(VpnState.DISCONNECTED)
    val vpnState = _vpnState.asStateFlow()

    fun updateState(newState: VpnState) {
        _vpnState.value = newState
    }
}
