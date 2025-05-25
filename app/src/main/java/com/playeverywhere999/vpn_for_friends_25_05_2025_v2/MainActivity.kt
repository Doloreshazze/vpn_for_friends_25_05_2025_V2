package com.playeverywhere999.vpn_for_friends_25_05_2025_v2

import android.app.Activity
import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.playeverywhere999.vpn_for_friends_25_05_2025_v2.ui.theme.Vpn_for_friends_25_05_2025_V2Theme // Ensure correct theme name
import kotlinx.coroutines.launch // Required for collecting flow

class MainActivity : ComponentActivity() {

    private val vpnPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            startVpnService()
        } else {
            // Handle permission denied
            VpnStateHolder.updateState(VpnState.ERROR)
            // Optionally, show a message to the user
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Vpn_for_friends_25_05_2025_V2Theme { // Ensure this matches your theme name
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    VpnControlScreen(
                        onConnectClick = { requestVpnPermission() },
                        onDisconnectClick = { stopVpnService() }
                    )
                }
            }
        }
    }

    private fun requestVpnPermission() {
        VpnStateHolder.updateState(VpnState.CONNECTING)
        val intent = VpnService.prepare(this)
        if (intent != null) {
            vpnPermissionLauncher.launch(intent)
        } else {
            // Permission already granted or not required
            startVpnService()
        }
    }

    private fun startVpnService() {
        val intent = Intent(this, MyVpnService::class.java).setAction(MyVpnService.ACTION_CONNECT)
        startService(intent)
        // Ideally, MyVpnService would update VpnStateHolder.
        // For now, we'll assume it will become CONNECTED or ERROR.
        // VpnStateHolder.updateState(VpnState.CONNECTED) // This will be handled by the service later
    }

    private fun stopVpnService() {
        val intent = Intent(this, MyVpnService::class.java).setAction(MyVpnService.ACTION_DISCONNECT)
        startService(intent)
        VpnStateHolder.updateState(VpnState.DISCONNECTED) // Assume immediate disconnect for now
    }
}

@Composable
fun VpnControlScreen(onConnectClick: () -> Unit, onDisconnectClick: () -> Unit) {
    val vpnState by VpnStateHolder.vpnState.collectAsState()
    // val coroutineScope = rememberCoroutineScope() // Not strictly needed here for now

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "VPN Status: ${vpnState.name}",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        when (vpnState) {
            VpnState.DISCONNECTED, VpnState.ERROR -> {
                Button(onClick = onConnectClick) {
                    Text("Connect VPN")
                }
            }
            VpnState.CONNECTING -> {
                Button(onClick = {}, enabled = false) { // Disable button while connecting
                    Text("Connecting...")
                }
            }
            VpnState.CONNECTED -> {
                Button(onClick = onDisconnectClick) {
                    Text("Disconnect VPN")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Vpn_for_friends_25_05_2025_V2Theme { // Ensure this matches your theme name
        VpnControlScreen(onConnectClick = {}, onDisconnectClick = {})
    }
}