package com.jean.cuidemonosaqp.shared.components

import android.Manifest
import android.location.Location
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.jean.cuidemonosaqp.R
import com.jean.cuidemonosaqp.modules.safeZone.data.dto.SafeZoneResponseDTO
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CurrentLocationMap(points: List<SafeZoneResponseDTO> = emptyList(), modifier: Modifier = Modifier) {


    val context = LocalContext.current
    // Estados para la ubicación y el mapa
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    // Posición inicial del mapa (Arequipa, Perú)
    val defaultLocation = LatLng(-16.4090, -71.5375)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            currentLocation ?: defaultLocation,
            15f
        )
    }

    // Permisos de ubicación
    val locationPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    // Cliente de ubicación
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    // Función para obtener la ubicación actual
    suspend fun getCurrentLocation(): Location? = suspendCancellableCoroutine { continuation ->
        if (!locationPermissions.allPermissionsGranted) {
            continuation.resume(null)
            return@suspendCancellableCoroutine
        }

        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            10000L
        ).build()

        try {
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                null
            ).addOnSuccessListener { location ->
                continuation.resume(location)
            }.addOnFailureListener {
                continuation.resume(null)
            }
        } catch (e: SecurityException) {
            continuation.resume(null)
        }
    }

    // Efecto para obtener la ubicación cuando se otorgan los permisos
    LaunchedEffect(locationPermissions.allPermissionsGranted) {
        if (locationPermissions.allPermissionsGranted) {
            isLoading = true
            try {
                val location = getCurrentLocation()
                location?.let {
                    val newLocation = LatLng(it.latitude, it.longitude)
                    currentLocation = newLocation
                    // Animar la cámara a la nueva ubicación
                    cameraPositionState.animate(
                        CameraUpdateFactory.newLatLngZoom(newLocation, 18f),
                        1000
                    )
                }
            } catch (e: Exception) {
                // Manejar error
            } finally {
                isLoading = false
            }
        }
    }


    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            isMyLocationEnabled = locationPermissions.allPermissionsGranted,
            mapType = MapType.NORMAL
        )
    ) {
        // Marcador en la ubicación actual
        currentLocation?.let { location ->
            Marker(
                state = MarkerState(position = location),
                title = "Mi ubicación",
                snippet = "Estás aquí"
            )
        }

        // Marcadores de puntos seguros
        points.forEach { point ->
            val context = LocalContext.current
            val icon: BitmapDescriptor =
                BitmapDescriptorFactory.fromResource(R.drawable.secure_point_icon)

            Marker(
                state = MarkerState(position = LatLng(point.latitude, point.longitude)),
                title = point.name,
                snippet = point.category ?: "Seguro",
                icon = icon
            )
        }
    }

    // Dialog para solicitar permisos
    if (!locationPermissions.allPermissionsGranted) {
        LocationPermissionDialog(
            locationPermissions = locationPermissions
        )
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionDialog(
    locationPermissions: MultiplePermissionsState
) {
    if (locationPermissions.shouldShowRationale || !locationPermissions.allPermissionsGranted) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = {},
            title = {
                Text("Permisos de Ubicación Requeridos")
            },
            text = {
                Text(
                    "Para mostrar tu ubicación actual en el mapa, necesitamos acceso a tu ubicación. Por favor, acepta los permisos."
                )
            },
            confirmButton = {
                androidx.compose.material3.TextButton(
                    onClick = {
                        locationPermissions.launchMultiplePermissionRequest()
                    }
                ) {
                    Text("Permitir")
                }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(onClick = {}) {
                    Text("Cancelar")
                }
            }
        )
    }
}