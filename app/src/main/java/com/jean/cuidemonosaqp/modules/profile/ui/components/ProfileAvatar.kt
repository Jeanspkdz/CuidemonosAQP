package com.jean.cuidemonosaqp.modules.profile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.jean.cuidemonosaqp.R

@Composable
fun ProfileAvatar(profilePhotoUrl: String,  modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Box {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(profilePhotoUrl)
                .placeholder(R.drawable.default_profile)
                .error(R.drawable.default_profile)
                .crossfade(true)
                .build(),
            contentScale = ContentScale.Crop,
            contentDescription = "User Photo Profile",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.White)
        )

        // Verification Icon
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .align(Alignment.BottomEnd),
            contentAlignment = Alignment.Center
        ) {
            Text("✓", color = Color.White, fontSize = 16.sp)
        }
    }

}