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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jean.cuidemonosaqp.modules.profile.ui.ProfileViewModel
import com.jean.cuidemonosaqp.navigation.NavGraph
import com.jean.cuidemonosaqp.navigation.Routes
import com.jean.cuidemonosaqp.shared.components.BottomNavigationBar
import com.jean.cuidemonosaqp.shared.preferences.SessionViewModel
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
    val currentHierarchy :  Sequence<NavDestination>? = currentDestination?.hierarchy
    val showBottomBar = currentHierarchy?.none() {
        it.hasRoute(Routes.Auth.Login::class) || it.hasRoute(Routes.Auth.Register::class)
    } ?: false

    //ESTO!!!!
    val sessionViewModel = hiltViewModel<SessionViewModel>()
    val userId by sessionViewModel.userId.collectAsStateWithLifecycle(null)

    //SnackBar
    val snackBarHostState = remember {
        SnackbarHostState()
    }

    ObserveAsEvents(flow = SnackBarController.events) { e ->
        snackBarHostState.currentSnackbarData?.dismiss()

        val result = snackBarHostState.showSnackbar(
            message =  e.message,
            actionLabel = e.snackBarAction?.name,
            duration = SnackbarDuration.Short
        )

        if(result == SnackbarResult.ActionPerformed){
            e.snackBarAction?.action?.invoke()
        }
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
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState
            )
        }
    ) { innerPadding ->
        NavGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}


