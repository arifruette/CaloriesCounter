package ru.ari.caloriescounter

import android.os.Bundle
import android.os.SystemClock
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import ru.ari.caloriescounter.core.ui.theme.CaloriesCounterTheme
import ru.ari.caloriescounter.core.navigation.CaloriesCounterRoot
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var startupResolved = false

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashStartTime = SystemClock.elapsedRealtime()
        installSplashScreen().setKeepOnScreenCondition {
            val minimumDurationPassed = SystemClock.elapsedRealtime() - splashStartTime >= 500L
            !startupResolved || !minimumDurationPassed
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CaloriesCounterTheme {
                CaloriesCounterRoot(
                    onStartupResolved = { startupResolved = true },
                )
            }
        }
    }
}
