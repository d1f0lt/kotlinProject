package com.example.weatherapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.example.weatherapp.screens.LoadingScreen
import com.example.weatherapp.screens.MainScreen
import com.example.weatherapp.ui.theme.WeatherAppTheme
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

class MainActivity : ComponentActivity() {
    private var continuation: Continuation<Boolean>? = null

    private val locationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { isGranted -> continuation?.resume(isGranted) }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)

        setContent {
            WeatherAppTheme {
                var loading by remember { mutableStateOf(true) }

                LaunchedEffect(Unit) {
                    checkOrRequestLocationPermission()
                    loading = false
                }

                when {
                    loading -> LoadingScreen()
                    else -> MainScreen(this)
                }
            }
        }
    }

    private suspend fun checkOrRequestLocationPermission(): Boolean {
        if (hasLocationPermission()) return true

        return suspendCancellableCoroutine { cont ->
            continuation = cont
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun hasLocationPermission(): Boolean =
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED
}
