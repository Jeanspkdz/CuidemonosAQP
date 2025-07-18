package com.jean.cuidemonosaqp.modules.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jean.cuidemonosaqp.modules.profile.ui.components.AddReviewDialog
import com.jean.cuidemonosaqp.modules.profile.ui.components.ProfileAvatar
import com.jean.cuidemonosaqp.modules.profile.ui.components.ProfileStat
import com.jean.cuidemonosaqp.modules.profile.ui.components.ProfileStatistics
import com.jean.cuidemonosaqp.modules.profile.ui.components.StartRating
import com.jean.cuidemonosaqp.modules.profile.ui.components.UserReviewList
import com.jean.cuidemonosaqp.shared.theme.CuidemonosAQPTheme


@Composable
fun ProfileScreenHost(
    viewModel: ProfileViewModel,
    modifier: Modifier = Modifier
) {
    val user by viewModel.userState.collectAsStateWithLifecycle()
    val reviews by viewModel.reviews.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val showAddReviewDialog by viewModel.showAddReviewDialog.collectAsStateWithLifecycle()
    val rating by viewModel.rating.collectAsStateWithLifecycle()
    val userReviewComment by viewModel.userReviewComment.collectAsStateWithLifecycle()


    ProfileScreen(
        user = user,
        reviews = reviews,
        rating = rating,
        onSelectRating = viewModel::onSelectRating,
        userReviewComment = userReviewComment,
        onChangeUserReviewComment = viewModel::onChangeUserReviewComment,
        onShowDialog = viewModel::showDialog,
        onHideDialog = viewModel::hideDialog,
        showAddReviewDialog = showAddReviewDialog,
        onCreateUserReview = viewModel::onCreateUserReview,
        isLoading = isLoading,
        modifier = modifier,
    )
}

@Composable
fun ProfileScreen(
    user: UserUI?,
    isLoading: Boolean,
    reviews: List<ReviewUI>,
    onShowDialog: () -> Unit,
    onHideDialog: () -> Unit,
    showAddReviewDialog: Boolean,
    rating: Int,
    onSelectRating: (score: Int) -> Unit,
    userReviewComment: String,
    onChangeUserReviewComment: (comment: String) -> Unit,
    onCreateUserReview: () -> Unit,
    modifier: Modifier = Modifier
) {

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ProfileAvatar(
            profilePhotoUrl = user.profilePhotoUrl,
        )

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

        StartRating()

        Spacer(modifier = Modifier.height(20.dp))

        // Botón Calificar Usuario
        Button(
            onClick = { onShowDialog() },
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

        if(showAddReviewDialog){
            AddReviewDialog(
                onDismiss = {onHideDialog()},
                rating = rating,
                onSelectRating = onSelectRating,
                userReviewComment = userReviewComment,
                onChangeUserReviewComment = onChangeUserReviewComment ,
                onCreateUserReview = onCreateUserReview
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        ProfileStatistics(
            listOf(
                ProfileStat("📍", user.monitoredPoints.toString(), "Puntos Vigilados"),
                ProfileStat("⏱️", "${user.surveillanceHours}h", "Vigilancia"),
                ProfileStat("🛡️", "${user.reliability}%", "Confiabilidad")
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        UserReviewList(reviews = reviews)
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
            rating = 4,
            onSelectRating = {},
            userReviewComment = "Buena informacion",
            onChangeUserReviewComment = {},
            onShowDialog = {},
            onHideDialog = {},
            showAddReviewDialog = false,
            onCreateUserReview = {}
        )
    }
}