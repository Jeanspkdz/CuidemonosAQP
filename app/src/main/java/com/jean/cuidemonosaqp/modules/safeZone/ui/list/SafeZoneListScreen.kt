package com.jean.cuidemonosaqp.modules.safeZone.ui.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.jean.cuidemonosaqp.R
import com.jean.cuidemonosaqp.shared.theme.CuidemonosAQPTheme

@Composable
fun SafeZoneListScreenHost(
    viewModel: SafeZoneListViewModel = hiltViewModel(),
    onNavigateToSafeZoneDetail: (id: String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val currentPageItems = remember(state.currentPage, state.filteredSafeZones) {
        viewModel.getCurrentPageItems()
    }

    SafeZoneListScreen(
        state = state,
        currentPageItems = currentPageItems,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onToggleUserZonesFilter = viewModel::onToggleUserZonesFilter,
        onLoadMore = viewModel::onLoadMore,
        onRefresh = viewModel::refresh,
        onNavigateToSafeZoneDetail = onNavigateToSafeZoneDetail
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SafeZoneListScreen(
    state: SafeZoneListState,
    currentPageItems: List<SafeZoneListItem>,
    onSearchQueryChange: (String) -> Unit,
    onToggleUserZonesFilter: () -> Unit,
    onLoadMore: () -> Unit,
    onRefresh: () -> Unit,
    onNavigateToSafeZoneDetail: (id: String) -> Unit
) {
    val listState = rememberLazyListState()
    
    // Detect when user scrolls near the bottom to load more
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null && 
                    lastVisibleIndex >= currentPageItems.size - 3 && 
                    state.hasMorePages && 
                    !state.isLoading) {
                    onLoadMore()
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Header
        Text(
            text = "Puntos Seguros",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        // Search Bar
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(25.dp)
        ) {
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { 
                    Text(
                        "Buscar zonas seguras...",
                        color = Color.Gray
                    ) 
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.Gray
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Filter Toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (state.isShowingUserZones) "Mis Puntos" else "Todos los Puntos",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )

            FilterChip(
                onClick = onToggleUserZonesFilter,
                label = { 
                    Text(
                        text = if (state.isShowingUserZones) "Mis Puntos" else "Todos",
                        fontSize = 12.sp
                    ) 
                },
                selected = state.isShowingUserZones,
                leadingIcon = {
                    Icon(
                        Icons.Default.FilterList,
                        contentDescription = "Filter",
                        modifier = Modifier.size(16.dp)
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Content
        when {
            state.isLoading && currentPageItems.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            
            state.error != null -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = state.error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onRefresh) {
                        Text("Reintentar")
                    }
                }
            }
            
            currentPageItems.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (state.isShowingUserZones) 
                                "No tienes zonas seguras asignadas" 
                            else 
                                "No se encontraron zonas seguras",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray
                        )
                        if (state.searchQuery.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Intenta con otros términos de búsqueda",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
            
            else -> {
                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(currentPageItems) { safeZone ->
                        SafeZoneListItem(
                            safeZone = safeZone,
                            onClick = { onNavigateToSafeZoneDetail(safeZone.id.toString()) }
                        )
                    }
                    
                    // Loading indicator for pagination
                    if (state.isLoading) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                            }
                        }
                    }
                    
                    // Bottom padding
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun SafeZoneListItem(
    safeZone: SafeZoneListItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Safe Zone Image
            AsyncImage(
                model = safeZone.photoUrl ?: "https://via.placeholder.com/80x80?text=SZ",
                contentDescription = "Imagen de ${safeZone.name}",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray.copy(alpha = 0.1f)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Name and User Zone Indicator
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = safeZone.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    
                    if (safeZone.isUserZone) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Mi Punto",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Status
                Text(
                    text = safeZone.status,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Rating
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = String.format("%.1f", safeZone.rating),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun SafeZoneListScreenPreview() {
    CuidemonosAQPTheme {
        SafeZoneListScreen(
            state = SafeZoneListState(
                filteredSafeZones = listOf(
                    SafeZoneListItem(
                        id = 1,
                        name = "Parque Los Tulipanes",
                        photoUrl = null,
                        rating = 4.5,
                        status = "Punto seguro",
                        isUserZone = true
                    ),
                    SafeZoneListItem(
                        id = 2,
                        name = "Plaza de Armas",
                        photoUrl = null,
                        rating = 4.2,
                        status = "Punto seguro",
                        isUserZone = false
                    ),
                    SafeZoneListItem(
                        id = 3,
                        name = "Centro Comercial Mall Aventura",
                        photoUrl = null,
                        rating = 4.8,
                        status = "Punto seguro",
                        isUserZone = true
                    )
                ),
                searchQuery = "",
                isShowingUserZones = false,
                isLoading = false
            ),
            currentPageItems = listOf(
                SafeZoneListItem(
                    id = 1,
                    name = "Parque Los Tulipanes",
                    photoUrl = null,
                    rating = 4.5,
                    status = "Punto seguro",
                    isUserZone = true
                ),
                SafeZoneListItem(
                    id = 2,
                    name = "Plaza de Armas",
                    photoUrl = null,
                    rating = 4.2,
                    status = "Punto seguro",
                    isUserZone = false
                )
            ),
            onSearchQueryChange = {},
            onToggleUserZonesFilter = {},
            onLoadMore = {},
            onRefresh = {},
            onNavigateToSafeZoneDetail = {}
        )
    }
}
