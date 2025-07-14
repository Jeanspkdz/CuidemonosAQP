package com.jean.cuidemonosaqp.app

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jean.cuidemonosaqp.navigation.NavGraph
import com.jean.cuidemonosaqp.navigation.Routes
import com.jean.cuidemonosaqp.shared.components.BottomNavigationBar
import com.jean.cuidemonosaqp.shared.preferences.SessionViewModel
import com.jean.cuidemonosaqp.shared.theme.CuidemonosAQPTheme
import dagger.hilt.android.AndroidEntryPoint


import android.net.Uri
import javax.inject.Inject
import com.jean.cuidemonosaqp.modules.safeZone.test.runner.SafeZoneTestRunner
import com.jean.cuidemonosaqp.shared.preferences.TokenManager

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var tokenManager: TokenManager
    @Inject
    lateinit var runner: SafeZoneTestRunner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*// 🔐 GUARDAR TOKEN MANUALMENTE
        val jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MTcsImVtYWlsIjoiam9yZHlAZ21haWwuY29tIiwiZG5pIjoiNzI2NzQ2NzIiLCJpYXQiOjE3NTIwODI1MTEsImV4cCI6MTc1MjA4NjExMX0.qiYDKjK_ovvzveEVh6IFN5D8abuKZezCQk1GB60KB64" // token válido de prueba
        tokenManager.saveAccessToken(jwt)

        // 📦 VERIFICAR
        Log.d("TOKEN", "Token guardado: ${tokenManager.getAccessToken()}")

        // 🧪 PROBAR CREACIÓN DE ZONA (requiere token válido)
        runner.createTestZone(contentResolver, null)*/

        enableEdgeToEdge()
        setContent {
            CuidemonosAQPTheme(dynamicColor = false) {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val currentNavBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentDestination = currentNavBackStackEntry?.destination

    val currentHierarchy :  Sequence<NavDestination>? = currentDestination?.hierarchy
    val showBottomBar = currentHierarchy?.none() {
        it.hasRoute(Routes.Auth.Login::class) || it.hasRoute(Routes.Auth.Register::class)
    } ?: false

    val sessionViewModel = hiltViewModel<SessionViewModel>()
    val userId by sessionViewModel.userId.collectAsStateWithLifecycle(null)

    LaunchedEffect(userId) {
        Log.d("TESTING!!!!!", "Launched_MainScreen: USER_ID ${userId} ")
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    navController = navController,
                    currentDestination = currentDestination,
                    userId= userId!!
                )
            }
        }
    ) { innerPadding ->
        NavGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}


