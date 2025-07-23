package com.jean.cuidemonosaqp.modules.safeZone.ui.createPoint

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.LatLng
import com.jean.cuidemonosaqp.modules.user.domain.model.User


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CreateSafeZoneScreen(viewModel: CreateSafeZoneViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val errorMsg = state.error
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var showLocationPicker by remember { mutableStateOf(false) }
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    val imagePickerLauncher = rememberLauncherForActivityResult(GetContent()) { uri: Uri? ->
        viewModel.onImageUriChange(uri)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Nombre
        OutlinedTextField(
            value = state.name,
            onValueChange = viewModel::onNameChange,
            label = { Text("Nombre de la zona segura") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Justificación
        OutlinedTextField(
            value = state.justification,
            onValueChange = viewModel::onJustificationChange,
            label = { Text("Justificación") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
            maxLines = 4
        )

        // Selección de ubicación
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Indicador de carga de ubicación del usuario
                if (state.isLoadingUserLocation) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Obteniendo tu ubicación...",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    OutlinedButton(
                        onClick = {
                            if (locationPermissionState.status.isGranted) {
                                if (state.currentUserLocation != null) {
                                    showLocationPicker = true
                                } else {
                                    // Mostrar error si no se pudo obtener la ubicación del usuario
                                    viewModel.onLatitudeChange("")
                                    viewModel.onLongitudeChange("")
                                }
                            } else {
                                locationPermissionState.launchPermissionRequest()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !state.isLoadingUserLocation && state.currentUserLocation != null
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Seleccionar ubicación"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            if (state.latitude.isNotBlank() && state.longitude.isNotBlank()) {
                                "Ubicación seleccionada (${
                                    if (state.currentUserLocation != null) {
                                        val selectedLocation = LatLng(
                                            state.latitude.toDoubleOrNull() ?: 0.0,
                                            state.longitude.toDoubleOrNull() ?: 0.0
                                        )
                                        val distance = viewModel.calculateDistance(state.currentUserLocation!!, selectedLocation)
                                        "${distance.toInt()}m"
                                    } else "Error"
                                })"
                            } else {
                                "Seleccionar Ubicación (máx. 100m)"
                            }
                        )
                    }
                }

                if (state.latitude.isNotBlank() && state.longitude.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))

                    // Validación visual de ubicación
                    val selectedLocation = LatLng(
                        state.latitude.toDoubleOrNull() ?: 0.0,
                        state.longitude.toDoubleOrNull() ?: 0.0
                    )
                    val isWithinRadius = state.currentUserLocation?.let { userLoc ->
                        viewModel.isLocationWithinRadius(selectedLocation)
                    } ?: false

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isWithinRadius)
                                MaterialTheme.colorScheme.primaryContainer
                            else
                                MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(
                                text = if (isWithinRadius)
                                    "✅ Ubicación válida"
                                else
                                    "⚠️ Ubicación fuera del área permitida",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isWithinRadius)
                                    MaterialTheme.colorScheme.onPrimaryContainer
                                else
                                    MaterialTheme.colorScheme.onErrorContainer,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Lat: ${String.format("%.6f", state.latitude.toDoubleOrNull() ?: 0.0)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isWithinRadius)
                                    MaterialTheme.colorScheme.onPrimaryContainer
                                else
                                    MaterialTheme.colorScheme.onErrorContainer
                            )
                            Text(
                                text = "Lng: ${String.format("%.6f", state.longitude.toDoubleOrNull() ?: 0.0)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isWithinRadius)
                                    MaterialTheme.colorScheme.onPrimaryContainer
                                else
                                    MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }

                // Mostrar error si no se pudo cargar la ubicación del usuario
                if (!state.isLoadingUserLocation && state.currentUserLocation == null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "⚠️ No se pudo obtener tu ubicación. Necesitas estar ubicado para crear zonas seguras.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        // Sección de Vigilantes
        VigilantesSelectorSection(
            state = state,
            onSearchChange = viewModel::onUserSearchChange,
            onUserSelected = viewModel::onUserSelected,
            onUserRemoved = viewModel::onUserRemoved,
            onClearSearch = viewModel::clearSearch
        )

        // Status ID
        OutlinedTextField(
            value = state.statusId,
            onValueChange = viewModel::onStatusIdChange,
            label = { Text("ID de Estado") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Campos opcionales
        OutlinedTextField(
            value = state.category,
            onValueChange = viewModel::onCategoryChange,
            label = { Text("Categoría (opcional)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = state.description,
            onValueChange = viewModel::onDescriptionChange,
            label = { Text("Descripción (opcional)") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
            maxLines = 4
        )

        // Selección de imagen
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Button(
                    onClick = { imagePickerLauncher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Seleccionar Imagen")
                }

                state.imageUri?.let { uri ->
                    Spacer(modifier = Modifier.height(12.dp))
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = "Preview de imagen",
                        modifier = Modifier
                            .height(150.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        // Botón de crear
        Button(
            onClick = {
                viewModel.submit(context.contentResolver)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = viewModel.canCreateZone()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text("Crear Zona Segura")
        }

        // Mensajes de estado
        if (state.success) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "✅",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Zona creada exitosamente",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        errorMsg?.let { msg ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Text(
                    text = msg,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }

    // Selector de ubicación
    if (showLocationPicker) {
        LocationPickerDialog(
            onLocationSelected = { latLng ->
                viewModel.onLatitudeChange(latLng.latitude.toString())
                viewModel.onLongitudeChange(latLng.longitude.toString())
            },
            onDismiss = { showLocationPicker = false },
            initialLocation = if (state.latitude.isNotBlank() && state.longitude.isNotBlank()) {
                LatLng(
                    state.latitude.toDoubleOrNull() ?: -16.409047,
                    state.longitude.toDoubleOrNull() ?: -71.537451
                )
            } else {
                state.currentUserLocation ?: LatLng(-16.409047, -71.537451)
            },
            userLocation = state.currentUserLocation,
            maxDistanceMeters = 100.0
        )
    }
}

@Composable
private fun VigilantesSelectorSection(
    state: SafeZoneUiState,
    onSearchChange: (String) -> Unit,
    onUserSelected: (User) -> Unit,
    onUserRemoved: (User) -> Unit,
    onClearSearch: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Título y contador
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Vigilantes requeridos",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Se necesitan mínimo 3 vigilantes para activar este punto seguro",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Indicadores de progreso
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(3) { index ->
                    val isSelected = index < state.selectedUsers.size
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.surfaceVariant
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (index + 1).toString(),
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Buscador
            OutlinedTextField(
                value = state.userSearchQuery,
                onValueChange = onSearchChange,
                label = { Text("Invitar vigilantes por nombre o correo") },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                trailingIcon = {
                    if (state.userSearchQuery.isNotEmpty()) {
                        IconButton(onClick = onClearSearch) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Limpiar búsqueda"
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = state.selectedUsers.size < 3
            )

            // Sugerencias de usuarios
            if (state.userSuggestions.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column {
                        state.userSuggestions.forEach { user ->
                            UserSuggestionItem(
                                user = user,
                                onClick = { onUserSelected(user) }
                            )
                        }
                    }
                }
            }

            // Usuarios seleccionados
            if (state.selectedUsers.isNotEmpty()) {
                Text(
                    text = "Vigilantes seleccionados:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    state.selectedUsers.forEach { user ->
                        SelectedUserItem(
                            user = user,
                            onRemove = { onUserRemoved(user) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun UserSuggestionItem(
    user: User,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar placeholder o imagen de perfil
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            if (user.profilePhotoUrl != null) {
                Image(
                    painter = rememberAsyncImagePainter(user.profilePhotoUrl),
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(
                    text = "${user.firstName.firstOrNull()}${user.lastName.firstOrNull()}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = user.fullName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = user.email,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SelectedUserItem(
    user: User,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                if (user.profilePhotoUrl != null) {
                    Image(
                        painter = rememberAsyncImagePainter(user.profilePhotoUrl),
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        text = "${user.firstName.firstOrNull()}${user.lastName.firstOrNull()}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = user.fullName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Quitar vigilante",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}