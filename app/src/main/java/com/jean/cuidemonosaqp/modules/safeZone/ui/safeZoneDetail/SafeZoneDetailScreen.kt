package com.jean.cuidemonosaqp.modules.safeZone.ui.safeZoneDetail

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun SafeZoneDetailScreenHost(viewModel: SafeZoneDetailViewModel, modifier: Modifier = Modifier) {
    Log.d("SafeZone", "SafeZoneDetailScreenHost: ${viewModel.run {  }} ")

    SafeZoneDetailScreen()
}

@Composable
fun SafeZoneDetailScreen(modifier: Modifier = Modifier) {

    Text("DetailScreen")
}