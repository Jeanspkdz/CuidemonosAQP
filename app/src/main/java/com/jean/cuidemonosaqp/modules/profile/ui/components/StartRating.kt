package com.jean.cuidemonosaqp.modules.profile.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun StartRating(modifier: Modifier = Modifier) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        repeat(5) {
            Text("⭐", fontSize = 20.sp)
        }
        repeat(5 - 5) {
            Text("☆", fontSize = 23.sp, color = Color.Gray)
        }
    }

    Text(
        text = "5.0/5.0",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )
}