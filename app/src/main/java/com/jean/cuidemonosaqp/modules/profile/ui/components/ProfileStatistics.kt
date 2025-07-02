package com.jean.cuidemonosaqp.modules.profile.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


data class ProfileStat(
    val icon: String,
    val value: String,
    val label: String
)

@Composable
fun ProfileStatistics(stats: List<ProfileStat> ,modifier: Modifier = Modifier) {


    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
       stats.forEach() {
           StatItem(
               icon = it.icon,
               value = it.value,
               label = it.label
           )
       }
    }
}