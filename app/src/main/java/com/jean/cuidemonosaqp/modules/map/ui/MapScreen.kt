package com.jean.cuidemonosaqp.modules.map.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.jean.cuidemonosaqp.R
import com.jean.cuidemonosaqp.modules.safeZone.data.dto.SafeZoneResponseDTO
import com.jean.cuidemonosaqp.shared.components.CurrentLocationMap
import com.jean.cuidemonosaqp.shared.theme.CuidemonosAQPTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Composable
fun MapScreenHost(
    viewModel: MapViewModel = hiltViewModel(),
    onNavigateToCreateSafeZone: () -> Unit,
    onNavigateToSafeZoneDetail: (id:String) -> Unit,
) {
    val safeZones by viewModel.safeZones.collectAsStateWithLifecycle()

    MapScreen(
        safeZones = safeZones,
        onNavigateToCreateSafeZone = onNavigateToCreateSafeZone,
        onNavigateToSafeZoneDetail = onNavigateToSafeZoneDetail
    )
}


@OptIn(ExperimentalPermissionsApi::class, ExperimentalCoroutinesApi::class)
@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    safeZones: List<SafeZoneResponseDTO>,
    onNavigateToCreateSafeZone: () -> Unit = {},
    onNavigateToSafeZoneDetail: (id:String) -> Unit = {},
) {

    var searchText by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = stringResource(R.string.app_name),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            //Search Bar
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                shape = RoundedCornerShape(25.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    TextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    )
                }
            }


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
            safeZones = emptyList()
        )
    }
}