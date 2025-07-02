package com.jean.cuidemonosaqp.modules.profile.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun AddReviewDialog(
    rating: Int,
    onRatingSelected: (score: Int) -> Unit,
    userReviewComment: String,
    onUserReviewCommentChanged: (comment: String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = {
            Text("Calificar Usuario")
        },
        text = {
            Column {
                Text(
                    text = "Calificación",
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(5) { index ->
                        IconButton(
                            onClick = { onRatingSelected(index + 1) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = if (index < rating) Icons.Default.Star else Icons.Outlined.Star,
                                contentDescription = null,
                                tint = if (index < rating) Color(0xFFFFC107) else Color.Gray,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Text(
                        text = "$rating/5",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                // Comentario
                OutlinedTextField(
                    value = userReviewComment,
                    onValueChange = { onUserReviewCommentChanged(it) },
                    label = { Text("Comentario") },
                    placeholder = { Text("Escribe tu experiencia con este usuario...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    maxLines = 4
                )
            }


        },
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(
                onClick = {}
            ) {
                Text("Enviar")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismiss() }
            ) {
                Text("Cancelar")
            }
        }
    )
}
