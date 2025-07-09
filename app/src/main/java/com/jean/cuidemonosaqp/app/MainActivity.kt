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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jean.cuidemonosaqp.navigation.NavGraph
import com.jean.cuidemonosaqp.navigation.Routes
import com.jean.cuidemonosaqp.shared.components.BottomNavigationBar
import com.jean.cuidemonosaqp.shared.theme.CuidemonosAQPTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CuidemonosAQPTheme(dynamicColor = false) {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen(
    context: Context = LocalContext.current
) {
    val navController = rememberNavController()
    val currentNavBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentDestination = currentNavBackStackEntry?.destination
    val routeString = currentDestination?.route

    val currentHierarchy = currentDestination?.hierarchy
    currentHierarchy?.forEach {
        Log.d("HIERARCHY", "$it")
    }
    val showBottomBar = currentHierarchy?.none() {
        it.hasRoute(Routes.Auth.Login::class) || it.hasRoute(Routes.Auth.Register::class)
    } ?: false



    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    navController = navController,
                    currentDestination = currentDestination,
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


