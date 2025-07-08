package com.jean.cuidemonosaqp.shared.components

import android.Manifest
import android.location.Location
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
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
fun CurrentLocationMap(
    modifier: Modifier = Modifier,
    safeZones: List<SafeZoneResponseDTO> = emptyList(),
    onNavigateToSafeZoneDetail: (id:String) -> Unit,
) {

    val isPreview = LocalInspectionMode.current
    if (isPreview) {
        Box(
            modifier = modifier.background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text("Mapa (Preview)", color = Color.Black)
        }
        return
    }

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
        safeZones.forEach { safeZone ->
            val icon: BitmapDescriptor =
                BitmapDescriptorFactory.fromResource(R.drawable.secure_point_icon)

            Marker(
                state = MarkerState(position = LatLng(safeZone.latitude, safeZone.longitude)),
                title = safeZone.name,
                snippet = safeZone.category ?: "Seguro",
                icon = icon,
                onInfoWindowClick = {onNavigateToSafeZoneDetail(safeZone.id.toString())}
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
        AlertDialog(
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
                TextButton(
                    onClick = {
                        locationPermissions.launchMultiplePermissionRequest()
                    }
                ) {
                    Text("Permitir")
                }
            },
            dismissButton = {
                TextButton(onClick = {}) {
                    Text("Cancelar")
                }
            }
        )
    }
}