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

@Composable
fun CreateSafeZoneScreen(viewModel: CreateSafeZoneViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(GetContent()) { uri: Uri? ->
        viewModel.onImageUriChange(uri)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(value = state.name, onValueChange = viewModel::onNameChange, label = { Text("Nombre") })
        OutlinedTextField(value = state.justification, onValueChange = viewModel::onJustificationChange, label = { Text("Justificación") })
        OutlinedTextField(value = state.latitude, onValueChange = viewModel::onLatitudeChange, label = { Text("Latitud") })
        OutlinedTextField(value = state.longitude, onValueChange = viewModel::onLongitudeChange, label = { Text("Longitud") })
        OutlinedTextField(value = state.statusId, onValueChange = viewModel::onStatusIdChange, label = { Text("ID de Estado") })
        OutlinedTextField(value = state.userIds, onValueChange = viewModel::onUserIdsChange, label = { Text("User IDs (ej: 1,2,3)") })
        OutlinedTextField(value = state.rating, onValueChange = viewModel::onRatingChange, label = { Text("Calificación") })

        // Campos opcionales
        OutlinedTextField(value = state.category, onValueChange = viewModel::onCategoryChange, label = { Text("Categoría") })
        OutlinedTextField(value = state.description, onValueChange = viewModel::onDescriptionChange, label = { Text("Descripción") })

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { imagePickerLauncher.launch("image/*") }) {
            Text("Seleccionar Imagen")
        }

        state.imageUri?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = "Preview de imagen",
                modifier = Modifier.height(200.dp).fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            viewModel.submit(context.contentResolver)
        }) {
            Text("Crear Zona Segura")
        }

        if (state.isLoading) Text("Cargando...")
        if (state.success) Text("Zona creada exitosamente ✅")
        if (state.error != null) Text("Error: ${state.error}")
    }
}

