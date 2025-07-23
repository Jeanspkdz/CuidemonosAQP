package com.jean.cuidemonosaqp.modules.map.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jean.cuidemonosaqp.R
import com.jean.cuidemonosaqp.modules.profile.ui.UserUI
import com.jean.cuidemonosaqp.modules.safeZone.data.dto.SafeZoneResponseDTO
import com.jean.cuidemonosaqp.modules.safeZone.ui.safeZoneDetail.components.MiniProfileAvatar
import com.jean.cuidemonosaqp.shared.components.CurrentLocationMap
import com.jean.cuidemonosaqp.shared.viewmodel.SharedViewModel
import com.jean.cuidemonosaqp.shared.theme.CuidemonosAQPTheme

@Composable
fun MapScreenHost(
    sharedViewModel: SharedViewModel,
    viewModel: MapViewModel = hiltViewModel(),
    onNavigateToCreateSafeZone: () -> Unit,
    onNavigateToSafeZoneDetail: (id: String) -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToProfile: () -> Unit = {},
    onLogout : () -> Unit = {}
) {
    val safeZones by viewModel.safeZones.collectAsStateWithLifecycle()
    val user by sharedViewModel.user.collectAsStateWithLifecycle()

    user?.let {
        MapScreen(
            user = it,
            safeZones = safeZones,
            onNavigateToCreateSafeZone = onNavigateToCreateSafeZone,
            onNavigateToSafeZoneDetail = onNavigateToSafeZoneDetail,
            onNavigateToNotifications = onNavigateToNotifications,
            onNavigateToProfile = onNavigateToProfile,
            onLogout = onLogout
        )
    }

}

@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    user: UserUI,
    safeZones: List<SafeZoneResponseDTO>,
    onNavigateToCreateSafeZone: () -> Unit = {},
    onNavigateToSafeZoneDetail: (id: String) -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onLogout : () -> Unit = {}
) {
    var showDropdownMenu by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = {
                        onNavigateToNotifications()
                    }) {
                        BadgedBox(
                            badge = {
                                Badge {
                                    Text(text = "2")
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Notifications,
                                contentDescription = "Notificaciones"
                            )
                        }
                    }
                    // Profile Avatar con Dropdown
                    Box {
                        MiniProfileAvatar(
                            profilePhotoUrl = user.profilePhotoUrl,
                            onNavigateToUserProfile = {
                                showDropdownMenu = true
                            }
                        )

                        DropdownMenu(
                            expanded = showDropdownMenu,
                            onDismissRequest = { showDropdownMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text("Mi Perfil")
                                },
                                onClick = {
                                    showDropdownMenu = false
                                    onNavigateToProfile()
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Outlined.Person,
                                        contentDescription = "Perfil"
                                    )
                                }
                            )

                            DropdownMenuItem(
                                text = {
                                    Text("Cerrar Sesión")
                                },
                                onClick = {
                                    showDropdownMenu = false
                                    onLogout()
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Outlined.Logout,
                                        contentDescription = "Logout"
                                    )
                                }
                            )
                        }
                    }
                },
                modifier = Modifier.padding()
            )


            Box(modifier = Modifier.weight(1f)) {
                CurrentLocationMap(
                    safeZones = safeZones,
                    modifier = Modifier.fillMaxSize(),
                    onNavigateToSafeZoneDetail = onNavigateToSafeZoneDetail
                )
                IconButton(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .size(64.dp),
                    onClick = { onNavigateToCreateSafeZone() }
                ) {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "Agregar PuntoSeguro",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

        }
    }
}

@Preview
@Composable
private fun MapScreenPreview() {
    CuidemonosAQPTheme {
        MapScreen(
            safeZones = emptyList(),
            user = UserUI(
                id = 1,
                dni = "12345678",
                firstName = "Jean",
                lastName = "Chua",
                phone = "+51 999 888 777",
                email = "jean.chua@example.com",
                address = "Av. Los Héroes 123, Arequipa",
                profilePhotoUrl = "https://example.com/profile.jpg",
                memberSince = "2022-03-15",
                rating = 4.5,
                monitoredPoints = 5,
                surveillanceHours = 18,
                reliability = 95
            )
        )
    }
}