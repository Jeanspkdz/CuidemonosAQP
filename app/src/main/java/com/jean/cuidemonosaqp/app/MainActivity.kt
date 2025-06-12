package com.jean.cuidemonosaqp.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.jean.cuidemonosaqp.navigation.NavGraph
import com.jean.cuidemonosaqp.shared.theme.CuidemonosAQPTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CuidemonosAQPTheme(dynamicColor = false) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Llama al NavGraph y pasa el innerPadding para la pantalla
                    NavGraph(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}


