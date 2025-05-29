package com.jean.cuidemonosaqp.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jean.cuidemonosaqp.features.auth.presentation.login.LoginScreenHost
import com.jean.cuidemonosaqp.features.auth.presentation.login.LoginViewModel
import com.jean.cuidemonosaqp.features.auth.presentation.register.RegisterScreenHost
import com.jean.cuidemonosaqp.features.profile.presentation.PerfilScreen
import com.jean.cuidemonosaqp.shared.presentation.theme.CuidemonosAQPTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CuidemonosAQPTheme(dynamicColor = false) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Routes.Auth,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable<Routes.Auth> {
                            LoginScreenHost(navController)
                        }
                        composable<Routes.Profile> {
                            PerfilScreen()
                        }
                        composable<Routes.Register> {
                            RegisterScreenHost()
                        }
                    }
                }
            }
        }
    }
}

