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
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.Color
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.isGranted
import android.Manifest
import android.content.Context
import android.location.LocationManager
import androidx.core.content.ContextCompat
import kotlin.math.*

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPickerDialog(
    onLocationSelected: (LatLng) -> Unit,
    onDismiss: () -> Unit,
    initialLocation: LatLng = LatLng(-16.409047, -71.537451), // Arequipa por defecto
    userLocation: LatLng? = null, // Ubicación del usuario
    maxDistanceMeters: Double = 100.0 // Radio máximo en metros
) {
    val context = LocalContext.current
    var selectedLocation by remember { mutableStateOf(initialLocation) }
    var cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userLocation ?: initialLocation, 19f) // Zoom más cercano
    }

    // Permisos de ubicación
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    // Función para calcular la distancia
    fun calculateDistance(location1: LatLng, location2: LatLng): Double {
        val earthRadius = 6371000.0 // Radio de la Tierra en metros

        val lat1Rad = Math.toRadians(location1.latitude)
        val lat2Rad = Math.toRadians(location2.latitude)
        val deltaLatRad = Math.toRadians(location2.latitude - location1.latitude)
        val deltaLngRad = Math.toRadians(location2.longitude - location1.longitude)

        val a = sin(deltaLatRad / 2).pow(2) +
                cos(lat1Rad) * cos(lat2Rad) * sin(deltaLngRad / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadius * c
    }

    // Verificar si la ubicación seleccionada está dentro del radio permitido
    val isWithinRadius = userLocation?.let { userLoc ->
        calculateDistance(userLoc, selectedLocation) <= maxDistanceMeters
    } ?: true

    val currentDistance = userLocation?.let { userLoc ->
        calculateDistance(userLoc, selectedLocation)
    } ?: 0.0

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.98f)
                .fillMaxHeight(0.85f),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header mejorado
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(
                        topStart = 12.dp,
                        topEnd = 12.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Seleccionar Ubicación",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "Máximo ${maxDistanceMeters.toInt()}m de tu ubicación",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                            )
                        }

                        // Botón para obtener ubicación actual
                        if (locationPermissionState.status.isGranted) {
                            FloatingActionButton(
                                onClick = {
                                    getCurrentLocation(context) { location ->
                                        selectedLocation = location
                                        cameraPositionState.move(
                                            CameraUpdateFactory.newLatLngZoom(location, 19f)
                                        )
                                    }
                                },
                                modifier = Modifier.size(48.dp),
                                containerColor = MaterialTheme.colorScheme.primary
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MyLocation,
                                    contentDescription = "Mi ubicación",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                }

                // Información de estado mejorada
                if (userLocation != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isWithinRadius)
                                Color(0xFF4CAF50).copy(alpha = 0.1f) // Verde suave
                            else
                                Color(0xFFF44336).copy(alpha = 0.1f) // Rojo suave
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isWithinRadius) Color(0xFF4CAF50) else Color(0xFFF44336)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = if (isWithinRadius) Color(0xFF4CAF50) else Color(0xFFF44336),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = if (isWithinRadius)
                                        "✅ Zona válida para crear punto seguro"
                                    else
                                        "⚠️ Fuera del área permitida",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (isWithinRadius) Color(0xFF2E7D32) else Color(0xFFC62828)
                                )
                                Text(
                                    text = "Distancia: ${currentDistance.toInt()}m de ${maxDistanceMeters.toInt()}m permitidos",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                // Mapa mejorado
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
                        },
                        properties = MapProperties(
                            mapType = MapType.HYBRID, // Mostrar vista híbrida para mejor contexto
                            isMyLocationEnabled = locationPermissionState.status.isGranted
                        ),
                        uiSettings = MapUiSettings(
                            zoomControlsEnabled = true,
                            myLocationButtonEnabled = false // Lo deshabilitamos porque tenemos nuestro botón personalizado
                        )
                    ) {
                        // Marcador de la ubicación del usuario (si existe)
                        userLocation?.let { userLoc ->
                            Marker(
                                state = MarkerState(position = userLoc),
                                title = "Tu ubicación actual",
                                snippet = "Desde aquí puedes crear zonas seguras",
                                icon = com.google.android.gms.maps.model.BitmapDescriptorFactory.defaultMarker(
                                    com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_BLUE
                                )
                            )

                            // Círculo que muestra el área permitida - MÁS VISIBLE
                            Circle(
                                center = userLoc,
                                radius = maxDistanceMeters,
                                fillColor = Color(0xFF4CAF50).copy(alpha = 0.15f), // Verde más visible
                                strokeColor = Color(0xFF2E7D32).copy(alpha = 0.8f), // Borde verde más fuerte
                                strokeWidth = 3f
                            )

                            // Círculo interior para mejor visualización
                            Circle(
                                center = userLoc,
                                radius = maxDistanceMeters * 0.7,
                                fillColor = Color(0xFF4CAF50).copy(alpha = 0.08f),
                                strokeColor = Color.Transparent,
                                strokeWidth = 0f
                            )
                        }

                        // Marcador de la ubicación seleccionada - MEJORADO
                        Marker(
                            state = MarkerState(position = selectedLocation),
                            title = if (isWithinRadius) "✅ Ubicación válida" else "❌ Fuera del área",
                            snippet = "Lat: ${String.format("%.6f", selectedLocation.latitude)}, Lng: ${String.format("%.6f", selectedLocation.longitude)}",
                            icon = com.google.android.gms.maps.model.BitmapDescriptorFactory.defaultMarker(
                                if (isWithinRadius)
                                    com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_GREEN
                                else
                                    com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_RED
                            )
                        )
                    }
                }

                // Información de coordenadas mejorada
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "📍 Coordenadas seleccionadas",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Latitud",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = String.format("%.6f", selectedLocation.latitude),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            Column {
                                Text(
                                    text = "Longitud",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = String.format("%.6f", selectedLocation.longitude),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }

                // Botones mejorados
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
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
                        modifier = Modifier.weight(1f),
                        enabled = isWithinRadius,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isWithinRadius)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Text(
                            if (isWithinRadius) "✅ Confirmar Ubicación" else "⚠️ Fuera del área",
                            color = if (isWithinRadius)
                                MaterialTheme.colorScheme.onPrimary
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
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