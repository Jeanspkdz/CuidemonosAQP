package com.jean.cuidemonosaqp.modules.profile.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.jean.cuidemonosaqp.R
import com.jean.cuidemonosaqp.shared.theme.CuidemonosAQPTheme
import com.jean.cuidemonosaqp.modules.user.domain.model.User


@Composable
fun ProfileScreenHost(
    viewModel: ProfileViewModel ,
    modifier: Modifier = Modifier
) {
    val user by viewModel.userState.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val reviews by viewModel.reviews.collectAsStateWithLifecycle()

    ProfileScreen(
        user = user,
        isLoading = isLoading,
        reviews = reviews,
        onRateUser = viewModel::onCalificarUsuario,
        modifier = modifier,
    )
}

@Composable
fun ProfileScreen(
    user: UserUI?,
    isLoading: Boolean,
    reviews: List<ReviewUI>,
    onRateUser: () -> Unit,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current


    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    if (user == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Something went wrong. Please try again.")
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item {
            // Imagen de perfil con borde azul
            Box {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(user.profilePhotoUrl)
                        .placeholder(R.drawable.default_profile)
                        .error(R.drawable.default_profile)
                        .crossfade(true)
                        .build(),
                    contentScale = ContentScale.Crop,
                    contentDescription = "User Photo Profile",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )

                // Ícono de verificación
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .align(Alignment.BottomEnd),
                    contentAlignment = Alignment.Center
                ) {
                    Text("✓", color = Color.White, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Nombre
            Text(
                text = user.fullName ?: "",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            // Miembro desde
            Text(
                text = user?.memberSince ?: "",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Rating con estrellas
            Row(verticalAlignment = Alignment.CenterVertically) {
                repeat(5) {
                    Text("⭐", fontSize = 20.sp)
                }
                repeat(5 - 5) {
                    Text("☆", fontSize = 23.sp, color = Color.Gray)
                }
            }

            Text(
                text = "5.0/5.0",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

//            Text(
//                text = "${profile.totalRatings} calificaciones",
//                fontSize = 14.sp,
//                color = Color.Gray
//            )

            Spacer(modifier = Modifier.height(20.dp))

            // Botón Calificar Usuario
            Button(
                onClick = { onRateUser() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    "Calificar Usuario",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Estadísticas en iconos
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    icon = "📍",
                    value = user.monitoredPoints.toString(),
                    label = "Puntos Vigilados"
                )

                StatItem(
                    icon = "⏱️",
                    value = "${user.surveillanceHours}h",
                    label = "Vigilancia"
                )

                StatItem(
                    icon = "🛡️",
                    value = "${user.reliability}%",
                    label = "Confiabilidad"
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Título Reseñas
            Text(
                text = "Reseñas Recientes",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        // Lista de reseñas
        items(reviews) { review ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    // Avatar pequeño de la reseña
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.Gray.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("👤", fontSize = 20.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = review.author,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )

                        // Estrellas de la reseña
                        Row {
                            repeat(review.stars) {
                                Text("⭐", fontSize = 14.sp)
                            }
                        }

                        Text(
                            text = review.comment,
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 4.dp)
                        )

                        Text(
                            text = review.date,
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatItem(icon: String, value: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = icon,
            fontSize = 24.sp
        )

        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    CuidemonosAQPTheme(dynamicColor = false) {
        ProfileScreen(
            user = UserUI(
                id = 1,
                dni = "74839201",
                firstName = "Juan",
                lastName = "Pérez",
                phone = "+51 912345678",
                email = "juan.perez@email.com",
                address = "Av. Los Héroes 123, Arequipa",
                profilePhotoUrl = "https://example.com/profile.jpg",
                memberSince = "2022-03-15",
                rating = 4.5,
                monitoredPoints = 12,
                surveillanceHours = 320,
                reliability = 95
            ),
            isLoading = false,
            reviews = listOf(
                ReviewUI(
                    id = "1",
                    author = "María González",
                    stars = 5,
                    comment = "Excelente servicio, muy amable y puntual.",
                    date = "2025-06-01"
                ),
                ReviewUI(
                    id = "2",
                    author = "Carlos Herrera",
                    stars = 4,
                    comment = "Buena atención aunque tardó un poco.",
                    date = "2025-06-10"
                ),
                ReviewUI(
                    id = "3",
                    author = "Lucía Pérez",
                    stars = 3,
                    comment = "Regular, esperaba algo más profesional.",
                    date = "2025-06-15"
                ),
                ReviewUI(
                    id = "4",
                    author = "Juan Díaz",
                    stars = 5,
                    comment = "Muy recomendable, volveré a contratar.",
                    date = "2025-06-17"
                )
            ),
            onRateUser = {}
        )
    }
}