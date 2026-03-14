package ru.ari.caloriescounter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.setContent
import ru.ari.caloriescounter.core.ui.theme.CaloriesCounterTheme
import ru.ari.caloriescounter.core.navigation.CaloriesCounterRoot
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CaloriesCounterTheme {
                CaloriesCounterRoot()
            }
        }
    }
}
