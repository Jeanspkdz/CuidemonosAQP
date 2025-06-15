package com.jean.cuidemonosaqp.modules.map.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.maps.android.compose.GoogleMap

@Composable
fun MapScreen(modifier: Modifier = Modifier) {
    Text(text = "Desde HOME MAP")
    GoogleMap(modifier = Modifier.fillMaxSize())
}