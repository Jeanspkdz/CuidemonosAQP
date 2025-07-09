package com.jean.cuidemonosaqp.modules.safeZone.ui.safeZoneDetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.jean.cuidemonosaqp.R
import com.jean.cuidemonosaqp.modules.profile.ui.components.ProfileAvatar

@Composable
fun MiniProfileAvatar(
    profilePhotoUrl: String,
    onNavigateToUserProfile : () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(profilePhotoUrl)
            .placeholder(R.drawable.default_profile)
            .error(R.drawable.default_profile)
            .crossfade(true)
            .build(),
        contentDescription = "User Photo Profile",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(30.dp)
            .clip(CircleShape)
            .border(2.dp, Color.White, CircleShape)
            .background(Color.LightGray)
            .clickable {onNavigateToUserProfile() }
    )
}

@Preview
@Composable
private fun MiniProfileAvatarPreview() {
    MiniProfileAvatar(
        profilePhotoUrl = "localhost:3000/fakeUrl.jpg",
        onNavigateToUserProfile = {}
    )
}