package com.jean.cuidemonosaqp.modules.safeZone.ui.createPoint.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.jean.cuidemonosaqp.modules.user.domain.model.User

/**
 * Componente para buscar y seleccionar usuarios
 * @param selectedUsers Lista de usuarios ya seleccionados
 * @param onUserSelected Callback cuando se selecciona un usuario
 * @param onUserRemoved Callback cuando se remueve un usuario
 * @param maxUsers Número máximo de usuarios que se pueden seleccionar
 * @param modifier Modificador para el componente
 */
@Composable
fun UserSearchSelector(
    selectedUsers: List<User>,
    onUserSelected: (User) -> Unit,
    onUserRemoved: (User) -> Unit,
    maxUsers: Int = 3,
    modifier: Modifier = Modifier,
    viewModel: UserSearchViewModel = hiltViewModel()
) {
    var showSearchDialog by remember { mutableStateOf(false) }
    val state by viewModel.state.collectAsState()

    Column(modifier = modifier) {
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

            // Indicador de progreso
            Row {
                repeat(maxUsers) { index ->
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .padding(2.dp)
                            .background(
                                color = if (index < selectedUsers.size) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.surfaceVariant
                                },
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${index + 1}",
                            color = if (index < selectedUsers.size) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            },
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Descripción
        Text(
            text = "Se necesitan mínimo $maxUsers vigilantes para activar este punto seguro",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de búsqueda
        OutlinedButton(
            onClick = { showSearchDialog = true },
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedUsers.size < maxUsers
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar usuarios"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Invitar vigilantes por nombre o correo")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de usuarios seleccionados
        if (selectedUsers.isNotEmpty()) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(selectedUsers) { user ->
                    SelectedUserItem(
                        user = user,
                        onRemove = { onUserRemoved(user) }
                    )
                }
            }
        }
    }

    // Dialog de búsqueda
    if (showSearchDialog) {
        UserSearchDialog(
            state = state,
            onSearchQueryChange = viewModel::onSearchQueryChange,
            onUserSelected = { user ->
                onUserSelected(user)
                showSearchDialog = false
            },
            onDismiss = {
                showSearchDialog = false
                viewModel.clearSearch()
            },
            selectedUserIds = selectedUsers.map { it.id }
        )
    }
}

/**
 * Item para mostrar un usuario seleccionado
 */
@Composable
private fun SelectedUserItem(
    user: User,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Foto de perfil
            Image(
                painter = rememberAsyncImagePainter(
                    model = user.profilePhotoUrl,
                    error = rememberAsyncImagePainter("https://via.placeholder.com/40x40?text=${user.firstName.first()}")
                ),
                contentDescription = "Foto de ${user.fullName}",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Información del usuario
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

            // Botón de eliminar
            IconButton(
                onClick = onRemove
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remover usuario",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

/**
 * Dialog para buscar usuarios
 */
@Composable
private fun UserSearchDialog(
    state: UserSearchState,
    onSearchQueryChange: (String) -> Unit,
    onUserSelected: (User) -> Unit,
    onDismiss: () -> Unit,
    selectedUserIds: List<Int>
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .fillMaxHeight(0.8f),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Título
                Text(
                    text = "Buscar Vigilantes",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo de búsqueda
                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = { Text("Buscar por nombre o correo...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar"
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Contenido de búsqueda
                when {
                    state.isLoading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    state.error != null -> {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            )
                        ) {
                            Text(
                                text = "Error: ${state.error}",
                                modifier = Modifier.padding(16.dp),
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }

                    state.users.isEmpty() && state.searchQuery.isNotBlank() -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No se encontraron usuarios",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    else -> {
                        // Lista de usuarios encontrados
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(state.users) { user ->
                                UserSearchItem(
                                    user = user,
                                    isSelected = selectedUserIds.contains(user.id),
                                    onSelected = { onUserSelected(user) }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón de cerrar
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cerrar")
                }
            }
        }
    }
}

/**
 * Item para mostrar un usuario en la búsqueda
 */
@Composable
private fun UserSearchItem(
    user: User,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { if (!isSelected) onSelected() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Foto de perfil
            Image(
                painter = rememberAsyncImagePainter(
                    model = user.profilePhotoUrl,
                    error = rememberAsyncImagePainter("https://via.placeholder.com/40x40?text=${user.firstName.first()}")
                ),
                contentDescription = "Foto de ${user.fullName}",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Información del usuario
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

            // Indicador de selección
            if (isSelected) {
                Text(
                    text = "Seleccionado",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}