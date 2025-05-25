package com.playeverywhere999.vpn_for_friends_25_05_2025_v2

import android.app.PendingIntent // Required for setConfigureIntent
import android.content.Intent
import android.net.VpnService
import android.os.ParcelFileDescriptor
import android.util.Log
// Ensure VpnStateHolder is imported if not in the same immediate package if it were moved
// import com.playeverywhere999.vpn_for_friends_25_05_2025_v2.VpnStateHolder 
// import com.playeverywhere999.vpn_for_friends_25_05_2025_v2.VpnState

class MyVpnService : VpnService() {

    private var vpnInterface: ParcelFileDescriptor? = null
    private var isTunnelRunning = false // Basic flag for tunnel status

    companion object {
        const val ACTION_CONNECT = "com.playeverywhere999.vpn_for_friends_25_05_2025_v2.CONNECT"
        const val ACTION_DISCONNECT = "com.playeverywhere999.vpn_for_friends_25_05_2025_v2.DISCONNECT"
        private const val TAG = "MyVpnService"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: intent action: ${intent?.action}")
        when (intent?.action) {
            ACTION_CONNECT -> {
                Log.i(TAG, "Action connect received")
                VpnStateHolder.updateState(VpnState.CONNECTING) // Update state
                if (!isTunnelRunning) {
                    startWireGuardTunnel()
                } else {
                    Log.w(TAG, "Tunnel already running, not starting again.")
                    VpnStateHolder.updateState(VpnState.CONNECTED) // Reflect it's already connected
                }
            }
            ACTION_DISCONNECT -> {
                Log.i(TAG, "Action disconnect received")
                if (isTunnelRunning) {
                    stopWireGuardTunnel()
                } else {
                    Log.w(TAG, "Tunnel not running, nothing to stop.")
                    VpnStateHolder.updateState(VpnState.DISCONNECTED) // Reflect it's already disconnected
                }
            }
        }
        return START_STICKY
    }

    private fun startWireGuardTunnel() {
        Log.i(TAG, "Attempting to start WireGuard tunnel (Placeholder)...")
        // TODO: Replace with actual WireGuard tunnel setup using the chosen library.
        // This will involve:
        // 1. Loading WireGuard configuration (keys, endpoint, peers, etc.)
        // 2. Using the WireGuard library to establish the tunnel.
        // 3. Configuring the VpnService.Builder with parameters from the tunnel.

        // Placeholder: Simulate tunnel setup
        val builder = Builder()
        try {
            // Basic VpnService configuration
            // These would normally come from the WireGuard configuration.
            builder.setSession(getString(R.string.app_name)) // Using app_name string resource
                .addAddress("10.0.0.2", 24) // Dummy address
                .addRoute("0.0.0.0", 0)    // Route all traffic

            // For a real service, you might want a configuration activity
            val configureIntent = Intent(this, MainActivity::class.java) // Or a dedicated config activity
            val configurePendingIntent = PendingIntent.getActivity(this, 0, configureIntent, PendingIntent.FLAG_IMMUTABLE)
            builder.setConfigureIntent(configurePendingIntent)

            vpnInterface = builder.establish()

            if (vpnInterface != null) {
                Log.d(TAG, "VPN interface established (Placeholder).")
                isTunnelRunning = true
                VpnStateHolder.updateState(VpnState.CONNECTED)
                // TODO: Start native WireGuard process here if the library requires it
            } else {
                Log.e(TAG, "Failed to establish VPN interface.")
                VpnStateHolder.updateState(VpnState.ERROR)
                stopSelf()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error starting WireGuard tunnel (Placeholder)", e)
            VpnStateHolder.updateState(VpnState.ERROR)
            stopSelf() // Stop service on critical error
        }
    }

    private fun stopWireGuardTunnel() {
        Log.i(TAG, "Attempting to stop WireGuard tunnel (Placeholder)...")
        // TODO: Replace with actual WireGuard tunnel teardown.
        // This will involve telling the WireGuard library to close the tunnel.

        isTunnelRunning = false
        vpnInterface?.close()
        vpnInterface = null
        Log.d(TAG, "WireGuard tunnel stopped (Placeholder).")
        VpnStateHolder.updateState(VpnState.DISCONNECTED)
        stopSelf() // Stop the service after disconnecting
    }

    override fun onDestroy() {
        Log.i(TAG, "onDestroy called")
        // Ensure tunnel is stopped if service is destroyed unexpectedly
        if (isTunnelRunning) {
            stopWireGuardTunnel()
        }
        super.onDestroy()
    }
}
