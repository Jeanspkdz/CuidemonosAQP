package com.jean.cuidemonosaqp.modules.safeZone.ui.createPoint

import android.Manifest
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.LatLng
import com.jean.cuidemonosaqp.modules.user.domain.model.User
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CreateSafeZoneScreen(
    viewModel: CreateSafeZoneViewModel = hiltViewModel(),
    onSafeZoneCreated: (String) -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    var showLocationPicker by remember { mutableStateOf(false) }
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val imagePickerLauncher = rememberLauncherForActivityResult(GetContent()) { uri: Uri? ->
        viewModel.onImageUriChange(uri)
    }

    LaunchedEffect(state.success, state.createdSafeZoneId) {
        if (state.success && state.createdSafeZoneId != null) {
            Toast.makeText(context, "¡Zona segura creada exitosamente!", Toast.LENGTH_SHORT).show()
            delay(1000)
            onSafeZoneCreated(state.createdSafeZoneId.toString())
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header con título
            Text(
                text = "Crear Punto Seguro",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Campos básicos con diseño mejorado
            ModernInputSection {
                OutlinedTextField(
                    value = state.name,
                    onValueChange = viewModel::onNameChange,
                    label = { Text("Nombre de la zona segura", fontSize = 14.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    )
                )
            }

            ModernInputSection {
                OutlinedTextField(
                    value = state.justification,
                    onValueChange = viewModel::onJustificationChange,
                    label = { Text("¿Por qué es seguro este punto?", fontSize = 14.sp) },
                    placeholder = { Text("Explica por qué consideras que este es un punto seguro...", fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 4,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    )
                )
            }

            // Ubicación con diseño mejorado
            LocationSection(
                state = state,
                isLocationPermissionGranted = locationPermissionState.status.isGranted,
                onLocationPermissionRequest = { locationPermissionState.launchPermissionRequest() },
                onLocationPickerShow = { showLocationPicker = true },
                viewModel = viewModel
            )

            // Vigilantes con diseño mejorado
            VigilantesSelectorSection(
                state = state,
                onSearchChange = viewModel::onUserSearchChange,
                onUserSelected = viewModel::onUserSelected,
                onUserRemoved = viewModel::onUserRemoved,
                onClearSearch = viewModel::clearSearch
            )

            // Categoría con diseño mejorado
            ModernInputSection {
                ExposedDropdownMenuBox(
                    expanded = state.isCategoryDropdownExpanded,
                    onExpandedChange = { viewModel.onCategoryDropdownToggle() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = state.category,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Categoría (opcional)", fontSize = 14.sp) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.isCategoryDropdownExpanded) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = state.isCategoryDropdownExpanded,
                        onDismissRequest = viewModel::onCategoryDismiss
                    ) {
                        state.availableCategories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category) },
                                onClick = { viewModel.onCategorySelect(category) },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        }
                    }
                }
            }

            ModernInputSection {
                OutlinedTextField(
                    value = state.description,
                    onValueChange = viewModel::onDescriptionChange,
                    label = { Text("Descripción (opcional)", fontSize = 14.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 4,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    )
                )
            }

            // Imagen con diseño mejorado
            ImageSection(
                imageUri = state.imageUri,
                onImageSelect = { imagePickerLauncher.launch("image/*") }
            )

            // Botón de crear con diseño mejorado
            CreateButton(
                isLoading = state.isLoading,
                canCreate = viewModel.canCreateZone(),
                onClick = { viewModel.submit(context.contentResolver) }
            )

            // Mensajes de estado
            StatusMessages(success = state.success, error = state.error)
        }
    }

    if (showLocationPicker) {
        LocationPickerDialog(
            onLocationSelected = { latLng ->
                viewModel.onLatitudeChange(latLng.latitude.toString())
                viewModel.onLongitudeChange(latLng.longitude.toString())
            },
            onDismiss = { showLocationPicker = false },
            initialLocation = if (state.latitude.isNotBlank() && state.longitude.isNotBlank()) {
                LatLng(state.latitude.toDoubleOrNull() ?: -16.409047, state.longitude.toDoubleOrNull() ?: -71.537451)
            } else {
                state.currentUserLocation ?: LatLng(-16.409047, -71.537451)
            },
            userLocation = state.currentUserLocation,
            maxDistanceMeters = 100.0
        )
    }
}

@Composable
private fun ModernInputSection(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            content()
        }
    }
}

@Composable
private fun LocationSection(
    state: SafeZoneUiState,
    isLocationPermissionGranted: Boolean,
    onLocationPermissionRequest: () -> Unit,
    onLocationPickerShow: () -> Unit,
    viewModel: CreateSafeZoneViewModel
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Ubicación",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            if (state.isLoadingUserLocation) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Obteniendo tu ubicación...",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                Button(
                    onClick = {
                        if (isLocationPermissionGranted) {
                            if (state.currentUserLocation != null) onLocationPickerShow()
                        } else onLocationPermissionRequest()
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    enabled = !state.isLoadingUserLocation && state.currentUserLocation != null,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (state.latitude.isNotBlank() && state.longitude.isNotBlank())
                            MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Icon(Icons.Default.LocationOn, "Seleccionar ubicación")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        if (state.latitude.isNotBlank() && state.longitude.isNotBlank()) {
                            "Ubicación seleccionada (${
                                state.currentUserLocation?.let { userLoc ->
                                    val selectedLocation = LatLng(
                                        state.latitude.toDoubleOrNull() ?: 0.0,
                                        state.longitude.toDoubleOrNull() ?: 0.0
                                    )
                                    "${viewModel.calculateDistance(userLoc, selectedLocation).toInt()}m"
                                } ?: "Error"
                            })"
                        } else "Seleccionar Ubicación (máx. 100m)",
                        fontSize = 14.sp
                    )
                }
            }

            LocationValidation(state, viewModel)
            LocationError(state)
        }
    }
}

@Composable
private fun LocationValidation(state: SafeZoneUiState, viewModel: CreateSafeZoneViewModel) {
    if (state.latitude.isNotBlank() && state.longitude.isNotBlank()) {
        Spacer(modifier = Modifier.height(12.dp))
        val selectedLocation = LatLng(
            state.latitude.toDoubleOrNull() ?: 0.0,
            state.longitude.toDoubleOrNull() ?: 0.0
        )
        val isWithinRadius = viewModel.isLocationWithinRadius(selectedLocation)

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isWithinRadius)
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f)
                else MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.8f)
            )
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    if (isWithinRadius) Icons.Default.CheckCircle else Icons.Default.Warning,
                    contentDescription = null,
                    tint = if (isWithinRadius) Color(0xFF4CAF50) else MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = if (isWithinRadius) "Ubicación válida" else "Ubicación fuera del área permitida",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = if (isWithinRadius) Color(0xFF2E7D32) else MaterialTheme.colorScheme.error
                    )
                    Text(
                        "Lat: ${String.format("%.6f", state.latitude.toDoubleOrNull() ?: 0.0)} | " +
                                "Lng: ${String.format("%.6f", state.longitude.toDoubleOrNull() ?: 0.0)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun LocationError(state: SafeZoneUiState) {
    if (!state.isLoadingUserLocation && state.currentUserLocation == null) {
        Spacer(modifier = Modifier.height(12.dp))
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.8f))
        ) {
            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "No se pudo obtener tu ubicación. Necesitas estar ubicado para crear zonas seguras.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    fontSize = 12.sp
                )
            }
        }
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
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            // Header mejorado
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    "Vigilantes requeridos",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "Se necesitan mínimo 3 vigilantes para activar este punto seguro",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
            }

            // Progress indicators mejorados
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                repeat(3) { index ->
                    val isSelected = index < state.selectedUsers.size
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) {
                                    Brush.radialGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary,
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                                        )
                                    )
                                } else {
                                    Brush.radialGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.surfaceVariant,
                                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                                        )
                                    )
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            (index + 1).toString(),
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Search field mejorado
            OutlinedTextField(
                value = state.userSearchQuery,
                onValueChange = onSearchChange,
                label = { Text("Invitar vigilantes por nombre o correo", fontSize = 14.sp) },
                leadingIcon = {
                    Icon(Icons.Default.Search, "Buscar", tint = MaterialTheme.colorScheme.primary)
                },
                trailingIcon = {
                    if (state.userSearchQuery.isNotEmpty()) {
                        IconButton(onClick = onClearSearch) {
                            Icon(Icons.Default.Close, "Limpiar búsqueda")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = state.selectedUsers.size < 3,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                )
            )

            // User suggestions mejoradas
            if (state.userSuggestions.isNotEmpty()) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column {
                        state.userSuggestions.forEach { user ->
                            UserSuggestionItem(user = user, onClick = { onUserSelected(user) })
                        }
                    }
                }
            }

            // Selected users mejorados
            if (state.selectedUsers.isNotEmpty()) {
                Text(
                    "Vigilantes seleccionados:",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    state.selectedUsers.forEach { user ->
                        SelectedUserItem(user = user, onRemove = { onUserRemoved(user) })
                    }
                }
            }
        }
    }
}

@Composable
private fun UserSuggestionItem(user: User, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        UserAvatar(user = user, size = 44)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                user.fullName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                user.email,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun SelectedUserItem(user: User, onRemove: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f)
        )
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            UserAvatar(user = user, size = 36, isPrimary = true)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    user.fullName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    user.email,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
            }
            IconButton(onClick = onRemove, modifier = Modifier.size(28.dp)) {
                Icon(
                    Icons.Default.Close,
                    "Quitar vigilante",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun UserAvatar(user: User, size: Int, isPrimary: Boolean = false) {
    Box(
        modifier = Modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(
                if (isPrimary) {
                    Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                        )
                    )
                } else {
                    Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer,
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f)
                        )
                    )
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        if (user.profilePhotoUrl != null) {
            Image(
                painter = rememberAsyncImagePainter(user.profilePhotoUrl),
                contentDescription = "Foto de perfil",
                modifier = Modifier.size(size.dp).clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Text(
                "${user.firstName.firstOrNull()}${user.lastName.firstOrNull()}",
                style = if (size > 35) MaterialTheme.typography.labelMedium else MaterialTheme.typography.labelSmall,
                color = if (isPrimary) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ImageSection(imageUri: Uri?, onImageSelect: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Añadir evidencia de seguridad",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Button(
                onClick = onImageSelect,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text("Seleccionar Imagen", fontSize = 14.sp)
            }

            imageUri?.let { uri ->
                Spacer(modifier = Modifier.height(16.dp))
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = "Preview de imagen",
                    modifier = Modifier
                        .height(160.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
private fun CreateButton(isLoading: Boolean, canCreate: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(56.dp),
        enabled = canCreate,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp
            )
            Spacer(modifier = Modifier.width(12.dp))
        }
        Text(
            "Crear Punto Seguro",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun StatusMessages(success: Boolean, error: String?) {
    if (success) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF4CAF50).copy(alpha = 0.1f)
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "Zona creada exitosamente",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF2E7D32)
                )
            }
        }
    }

    error?.let { msg ->
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.8f)
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    msg,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}