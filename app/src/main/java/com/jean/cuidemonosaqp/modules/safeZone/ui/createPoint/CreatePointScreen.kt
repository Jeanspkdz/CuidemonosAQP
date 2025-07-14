package com.jean.cuidemonosaqp.modules.safeZone.ui.createPoint

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.ui.Alignment
import com.google.android.gms.maps.model.LatLng
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.isGranted
import android.Manifest

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CreateSafeZoneScreen(viewModel: CreateSafeZoneViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var showLocationPicker by remember { mutableStateOf(false) }

    // Permisos de ubicación
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    val imagePickerLauncher = rememberLauncherForActivityResult(GetContent()) { uri: Uri? ->
        viewModel.onImageUriChange(uri)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = state.name,
            onValueChange = viewModel::onNameChange,
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.justification,
            onValueChange = viewModel::onJustificationChange,
            label = { Text("Justificación") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Botón para seleccionar ubicación
        OutlinedButton(
            onClick = {
                if (locationPermissionState.status.isGranted) {
                    showLocationPicker = true
                } else {
                    locationPermissionState.launchPermissionRequest()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Seleccionar ubicación"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                if (state.latitude.isNotBlank() && state.longitude.isNotBlank()) {
                    "Ubicación: ${String.format("%.4f", state.latitude.toDoubleOrNull() ?: 0.0)}, ${String.format("%.4f", state.longitude.toDoubleOrNull() ?: 0.0)}"
                } else {
                    "Seleccionar Ubicación"
                }
            )
        }

        // Mostrar coordenadas si están disponibles
        if (state.latitude.isNotBlank() && state.longitude.isNotBlank()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = "Coordenadas seleccionadas:",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = "Latitud: ${state.latitude}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Longitud: ${state.longitude}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.statusId,
            onValueChange = viewModel::onStatusIdChange,
            label = { Text("ID de Estado") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.userIds,
            onValueChange = viewModel::onUserIdsChange,
            label = { Text("User IDs (ej: 1,2,3)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.rating,
            onValueChange = viewModel::onRatingChange,
            label = { Text("Calificación") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campos opcionales
        OutlinedTextField(
            value = state.category,
            onValueChange = viewModel::onCategoryChange,
            label = { Text("Categoría") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.description,
            onValueChange = viewModel::onDescriptionChange,
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { imagePickerLauncher.launch("image/*") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Seleccionar Imagen")
        }

        state.imageUri?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = "Preview de imagen",
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.submit(context.contentResolver)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading
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

        if (state.success) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(
                    text = "Zona creada exitosamente ✅",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        if (state.error != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = "Error: ${state.error}",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }

    // Mostrar el selector de ubicación
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
                LatLng(-16.409047, -71.537451) // Arequipa por defecto
            }
        )
    }
}
