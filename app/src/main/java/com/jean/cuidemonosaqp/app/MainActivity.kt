package com.jean.cuidemonosaqp.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
import com.jean.cuidemonosaqp.shared.viewmodel.SharedViewModel
import com.jean.cuidemonosaqp.shared.theme.CuidemonosAQPTheme
import com.jean.cuidemonosaqp.shared.utils.ObserveAsEvents
import com.jean.cuidemonosaqp.shared.utils.SnackBarController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val sharedViewModel = hiltViewModel<SharedViewModel>()

            CuidemonosAQPTheme(dynamicColor = false) {
                MainScreen(
                    sharedViewModel
                )
            }
        }
    }
}

@Composable
fun MainScreen(
    sharedViewModel: SharedViewModel
) {
    val navController = rememberNavController()
    val currentNavBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentDestination = currentNavBackStackEntry?.destination
    val currentHierarchy: Sequence<NavDestination>? = currentDestination?.hierarchy
    val showBottomBar = currentHierarchy?.none() {
        it.hasRoute(Routes.Auth.Login::class) || it.hasRoute(Routes.Auth.Register::class)
    } ?: false

    val userId by sharedViewModel.userId.collectAsStateWithLifecycle()

    val snackBarHostState = remember {
        SnackbarHostState()
    }

    ObserveAsEvents(flow = SnackBarController.events) { e ->
        snackBarHostState.currentSnackbarData?.dismiss()
        val result = snackBarHostState.showSnackbar(
            message = e.message,
            actionLabel = e.snackBarAction?.name,
            duration = SnackbarDuration.Short
        )

        if (result == SnackbarResult.ActionPerformed) {
            e.snackBarAction?.action?.invoke()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar && userId !== null) {
                BottomNavigationBar(
                    navController = navController,
                    currentDestination = currentDestination,
                    userId = userId ?: ""
                )
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState
            )
        }
    ) { innerPadding ->
        NavGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            sharedViewModel= sharedViewModel
        )
    }
}


