package com.jean.cuidemonosaqp.modules.safeZone.ui.safeZoneDetail

import android.util.Log
import android.widget.Space
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jean.cuidemonosaqp.modules.profile.ui.components.ProfileAvatar
import com.jean.cuidemonosaqp.modules.safeZone.data.dto.SafeZoneResponseDTO
import com.jean.cuidemonosaqp.modules.safeZone.data.dto.StatusResponseDTO
import com.jean.cuidemonosaqp.modules.safeZone.ui.safeZoneDetail.components.MiniProfileAvatar
import com.jean.cuidemonosaqp.modules.user.data.dto.UserResponseDto
import com.jean.cuidemonosaqp.shared.theme.CuidemonosAQPTheme


@Composable
fun SafeZoneDetailScreenHost(
    viewModel: SafeZoneDetailViewModel,
    modifier: Modifier = Modifier,
    onNavigateToUserProfile: (userId: String) -> Unit
) {
    Log.d("SafeZone", "SafeZoneDetailScreenHost: ${viewModel.run { }} ")
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val safeZone by viewModel.safeZone.collectAsStateWithLifecycle()

    SafeZoneDetailScreen(
        isLoading = isLoading,
        safeZone = safeZone,
        onNavigateToUserProfile = onNavigateToUserProfile
    )
}

@Composable
fun SafeZoneDetailScreen(
    safeZone: SafeZoneResponseDTO?,
    isLoading: Boolean,
    onNavigateToUserProfile: (userId: String) -> Unit,
    modifier: Modifier = Modifier
) {

    if (isLoading) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator()
        }
        return
    }
    if (safeZone == null) {
        Box(contentAlignment = Alignment.Center) {
            Text("No se Pudo Cargar la Zona Segura")
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box() {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = safeZone.name, style = MaterialTheme.typography.headlineMedium)
                Text(text = safeZone.status?.description ?: "Estado desconocido")
            }
        }

        HorizontalDivider(Modifier.padding(vertical = 15.dp))

        Box(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        Box() {
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(text = "Vigilantes", style = MaterialTheme.typography.titleLarge)
                LazyRow(horizontalArrangement = Arrangement.spacedBy((-9).dp)) {
                    itemsIndexed(safeZone.users) { index, user ->
                        MiniProfileAvatar(
                            modifier = Modifier
                                .size(40.dp)
                                .zIndex(index.toFloat()), // el último queda encima
                            profilePhotoUrl = user.profilePhotoUrl.orEmpty(),
                            onNavigateToUserProfile = {onNavigateToUserProfile(user.id.toString())}
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        Box() {
            Column {
                Text(text = "Sobre la Zona", style = MaterialTheme.typography.titleLarge)
                Text(text = safeZone.description ?: "Not Available")
            }
        }


    }
}

@Preview
@Composable
private fun SafeZoneDetailScreenPreview() {
    CuidemonosAQPTheme {
        SafeZoneDetailScreen(
            isLoading = false,
            safeZone = SafeZoneResponseDTO(
                id = 6,
                name = "Parque los Tulipanes",
                description = "En la interseccion de las calles de los tlipanes y los lirios",
                category = "Parque",
                justification = "Tenemos acceso a muchas camaras de vigilancia y la presencial policial es concurrida",
                photoUrl = "http://localhost:3000/uploads/safezones/1749964846746-photo_url.jpeg",
                assumesResponsibility = true,
                longitude = -71.550602,
                latitude = -16.399044,
                statusId = 4,
                isActive = true,
                rating = null,
                createdAt = "2025-06-15T02:10:20.747Z",
                status = StatusResponseDTO(
                    id = 4,
                    status = "SAFE",
                    description = "Punto seguro",
                    createdAt = "2025-05-22T18:12:46.958Z",
                    updatedAt = "2025-05-22T18:12:46.958Z"
                ),
                users = listOf<UserResponseDto>(
                    UserResponseDto(
                        id = 1,
                        dni = "12345678",
                        dniExtension = "PE",
                        firstName = "Juan",
                        lastName = "Perez",
                        phone = "987654321",
                        email = "juan.perez@example.com",
                        address = "Av. Los Héroes 123",
                        addressLatitude = -16.39889,
                        addressLongitude = -71.5365,
                        dniPhotoUrl = "https://via.placeholder.com/100x140.png?text=DNI+1",
                        profilePhotoUrl = "https://via.placeholder.com/100.png?text=JP",
                        reputationScore = 5,
                        reputationStatusId = 1,
                        isActive = true,
                        refreshToken = null, // No incluir tokens reales en previews
                        createdAt = "2025-06-01T10:00:00Z",
                        updatedAt = "2025-07-01T15:30:00Z"
                    ),
                    UserResponseDto(
                        id = 2,
                        dni = "87654321",
                        dniExtension = "LP",
                        firstName = "Lucía",
                        lastName = "Ramirez",
                        phone = "912345678",
                        email = "lucia.ramirez@example.com",
                        address = "Jr. Las Flores 456",
                        addressLatitude = -16.40123,
                        addressLongitude = -71.5287,
                        dniPhotoUrl = "https://via.placeholder.com/100x140.png?text=DNI+2",
                        profilePhotoUrl = "https://via.placeholder.com/100.png?text=LR",
                        reputationScore = 4,
                        reputationStatusId = 1,
                        isActive = true,
                        refreshToken = null, // No incluir tokens reales en previews
                        createdAt = "2025-06-10T09:15:00Z",
                        updatedAt = "2025-07-05T11:45:00Z"
                    ),
                    UserResponseDto(
                        id = 3,
                        dni = "11223344",
                        dniExtension = "CJ",
                        firstName = "Pedro",
                        lastName = "López",
                        phone = "998877665",
                        email = "pedro.lopez@example.com",
                        address = "Calle Falsa 789",
                        addressLatitude = -16.39654,
                        addressLongitude = -71.5412,
                        dniPhotoUrl = "https://via.placeholder.com/100x140.png?text=DNI+3",
                        profilePhotoUrl = "https://via.placeholder.com/100.png?text=PL",
                        reputationScore = 3,
                        reputationStatusId = 2,
                        isActive = false,
                        refreshToken = null, // No incluir tokens reales en previews
                        createdAt = "2025-06-15T14:20:00Z",
                        updatedAt = "2025-07-08T08:00:00Z"
                    )
                )
            ),
            onNavigateToUserProfile = {}
        )
    }
}