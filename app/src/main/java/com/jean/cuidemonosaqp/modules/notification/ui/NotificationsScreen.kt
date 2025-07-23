@file:OptIn(ExperimentalMaterial3Api::class)

package com.jean.cuidemonosaqp.modules.notification.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun NotificationsScreenHost(
    viewModel: NotificationsViewModel,
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val invitations by viewModel.invitations.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    NotificationsScreen(
        invitations = invitations,
        isLoading = isLoading,
        errorMessage = errorMessage,
        onMarkInvitationAsSeen = viewModel::markInvitationAsSeen,
        onAcceptInvitation = viewModel::acceptInvitation,
        onRejectInvitation = viewModel::rejectInvitation,
        onBackPressed = onNavigateToHome
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    onBackPressed: () -> Unit = {},
    invitations: List<SafeZoneInvitationUI> = emptyList(),
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onMarkInvitationAsSeen: (invitationId: String) -> Unit = {},
    onRejectInvitation: (String) -> Unit = {},
    onAcceptInvitation: (String) -> Unit = {}
) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Top Bar
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .padding(end = 8.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Invitaciones",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = onBackPressed) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver atrás",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        // Content
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                isLoading -> {
                    // Loading indicator
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                errorMessage != null -> {
                    // Error state
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = Color(0xFFE57373)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Error al cargar invitaciones",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF424242)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = errorMessage ?: "Error desconocido",
                                fontSize = 14.sp,
                                color = Color(0xFF757575),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 32.dp)
                            )
                        }
                    }
                }

                invitations.isEmpty() -> {
                    // Empty state
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsNone,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = Color(0xFFBDBDBD)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "No tienes invitaciones",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF424242)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Cuando recibas invitaciones a zonas seguras aparecerán aquí",
                                fontSize = 14.sp,
                                color = Color(0xFF757575),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 32.dp)
                            )
                        }
                    }
                }

                else -> {
                    // Invitations List
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(invitations) { invitation ->
                            SafeZoneInvitationCard(
                                invitation = invitation,
                                onCardClick = {
                                    onMarkInvitationAsSeen(invitation.id)
                                },
                                onAcceptInvitation = onAcceptInvitation,
                                onRejectInvitation = onRejectInvitation
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SafeZoneInvitationCard(
    invitation: SafeZoneInvitationUI,
    onCardClick: (String) -> Unit,
    onRejectInvitation: (String) -> Unit,
    onAcceptInvitation: (String) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (!invitation.isSeen) {
                    onCardClick(invitation.id.toString())
                }
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (!invitation.isSeen) {
                Color(0xFFEBF4FF)
            } else {
                Color.White
            }
        ),
        border = if (!invitation.isSeen) {
            androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBBDEFB))
        } else {
            androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0))
        },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header with icon and status
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            if (!invitation.isSeen) {
                                Color(0xFFBBDEFB)
                            } else {
                                Color(0xFFF5F5F5)
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = if (!invitation.isSeen) {
                            Color(0xFF1976D2)
                        } else {
                            Color(0xFF757575)
                        }
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Title and status
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Invitación a Zona Segura",
                        fontSize = 16.sp,
                        fontWeight = if (!invitation.isSeen) FontWeight.SemiBold else FontWeight.Medium,
                        color = if (!invitation.isSeen) {
                            Color(0xFF212121)
                        } else {
                            Color(0xFF424242)
                        }
                    )

                    if (invitation.status == SafeZoneInvitationStatus.ACCEPTED) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = Color(0xFF4CAF50)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Confirmada",
                                fontSize = 12.sp,
                                color = Color(0xFF4CAF50),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    if (invitation.status == SafeZoneInvitationStatus.REJECTED) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Cancel,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = Color(0xFFF44336) // rojo
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Rechazada",
                                fontSize = 12.sp,
                                color = Color(0xFFF44336),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                // Unread indicator
                if (!invitation.isSeen) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF2196F3))
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Invitation details
            Column {
                Text(
                    text = "De: ${invitation.inviterName}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF424242)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Zona: ${invitation.safeZoneName}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1976D2),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                if (invitation.safeZoneDescription.isNotBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = invitation.safeZoneDescription,
                        fontSize = 14.sp,
                        color = Color(0xFF757575),
                        lineHeight = 20.sp
                    )
                }

                invitation.confirmedAt?.let { confirmedAt ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Confirmada el $confirmedAt",
                        fontSize = 12.sp,
                        color = Color(0xFF4CAF50),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Action buttons (only if not confirmed)
            if (invitation.status == SafeZoneInvitationStatus.PENDING) {
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { onRejectInvitation(invitation.id) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF757575)
                        )
                    ) {
                        Text(
                            text = "Rechazar",
                            fontSize = 14.sp
                        )
                    }

                    Button(
                        onClick = { onAcceptInvitation(invitation.id) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1976D2)
                        )
                    ) {
                        Text(
                            text = "Aceptar",
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationsScreenPreview() {
    MaterialTheme {
        NotificationsScreen(
        )
    }
}