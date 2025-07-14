package com.jean.cuidemonosaqp.modules.safeZone.ui.createPoint

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.ui.graphics.Color
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.isGranted
import android.Manifest
import android.content.Context
import android.location.LocationManager
import androidx.core.content.ContextCompat

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPickerDialog(
    onLocationSelected: (LatLng) -> Unit,
    onDismiss: () -> Unit,
    initialLocation: LatLng = LatLng(-16.409047, -71.537451) // Arequipa por defecto
) {
    val context = LocalContext.current
    var selectedLocation by remember { mutableStateOf(initialLocation) }
    var cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialLocation, 15f)
    }

    // Permisos de ubicación
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.8f),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Seleccionar Ubicación",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    // Botón para obtener ubicación actual
                    if (locationPermissionState.status.isGranted) {
                        IconButton(
                            onClick = {
                                getCurrentLocation(context) { location ->
                                    selectedLocation = location
                                    cameraPositionState.move(
                                        CameraUpdateFactory.newLatLngZoom(location, 15f)
                                    )
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.MyLocation,
                                contentDescription = "Mi ubicación",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                // Mapa
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                ) {
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                        onMapClick = { latLng ->
                            selectedLocation = latLng
                        }
                    ) {
                        Marker(
                            state = MarkerState(position = selectedLocation),
                            title = "Ubicación seleccionada",
                            snippet = "Lat: ${String.format("%.6f", selectedLocation.latitude)}, Lng: ${String.format("%.6f", selectedLocation.longitude)}"
                        )
                    }
                }

                // Información de coordenadas
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
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
                            text = "Latitud: ${String.format("%.6f", selectedLocation.latitude)}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Longitud: ${String.format("%.6f", selectedLocation.longitude)}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                // Botones
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar")
                    }

                    Button(
                        onClick = {
                            onLocationSelected(selectedLocation)
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Confirmar")
                    }
                }
            }
        }
    }
}

private fun getCurrentLocation(context: Context, onLocationReceived: (LatLng) -> Unit) {
    try {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

            location?.let {
                onLocationReceived(LatLng(it.latitude, it.longitude))
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}