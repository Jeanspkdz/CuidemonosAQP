package com.jean.cuidemonosaqp.modules.profile.ui

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.jean.cuidemonosaqp.modules.userreviews.domain.usecase.GetReviewsForUserUseCase
import com.jean.cuidemonosaqp.modules.profile.domain.usecase.GetUserInfoUseCase
import com.jean.cuidemonosaqp.modules.userreviews.data.dto.UserReviewRequestDTO
import com.jean.cuidemonosaqp.modules.userreviews.domain.usecase.CreateUserReviewUseCase
import com.jean.cuidemonosaqp.navigation.Routes
import com.jean.cuidemonosaqp.shared.network.NetworkResult
import com.jean.cuidemonosaqp.shared.preferences.SessionRepository
import com.jean.cuidemonosaqp.shared.utils.SnackBarController
import com.jean.cuidemonosaqp.shared.utils.SnackBarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val getUserInfoUseCase: GetUserInfoUseCase,
    val getUserReviewsUseCase: GetReviewsForUserUseCase,
    val createUserReviewUseCase: CreateUserReviewUseCase,
    private val sessionRepository: SessionRepository,
    private val savedStateHandle: SavedStateHandle,
) :
    ViewModel() {

    companion object {
        const val TAG = "ProfileViewModel"
    }

    private lateinit var currentUserId : String

    private val _userState = MutableStateFlow<UserUI?>(null)
    val userState: StateFlow<UserUI?> = _userState.asStateFlow()

    private val _reviews = MutableStateFlow<List<ReviewUI>>(emptyList())
    val reviews: StateFlow<List<ReviewUI>> = _reviews.asStateFlow()

    private val _isOwnProfile = MutableStateFlow(false)
    val isOwnProfile = _isOwnProfile.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _showAddReviewDialog = MutableStateFlow(false)
    val showAddReviewDialog = _showAddReviewDialog.asStateFlow()

    private val _rating = MutableStateFlow(0)
    val rating = _rating.asStateFlow()

    private val _userReviewComment = MutableStateFlow("")
    val userReviewComment = _userReviewComment.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    init {
        getCurrentUserId()
        checkOwnProfile()
        loadProfileData()
        loadReviews()
    }

    private fun checkOwnProfile(){
       viewModelScope.launch {
           _isOwnProfile.value = currentUserId == sessionRepository.getUserId()
       }
    }

    private fun getCurrentUserId(){
        val profileRoute = savedStateHandle.toRoute<Routes.Profile>()
        val id = profileRoute.id
        currentUserId = id
    }

    private fun loadProfileData() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                Log.d(TAG, "SESSION ID: ${sessionRepository.getUserId()}")

                val userId = currentUserId

                when (val response = getUserInfoUseCase(userId)) {
                    is NetworkResult.Error -> {
                        Log.d(TAG, "loadProfileData Error ${response.message}")
                        _errorMessage.update { response.message }
                    }

                    is NetworkResult.Success -> {
                        Log.d(TAG, "loadProfileData Success ${response.data}")
                        val data = response.data
                        _userState.update { data.toUI() }
                    }

                    is NetworkResult.Loading -> {
                        Log.d("LOGIN_LOADING", "Cargando...")
                    }
                }

            } catch (e: Exception) {
                // Handle Exception
                Log.d(TAG, "ERROR :  ${e.message}")
                _errorMessage.update { "Something went wrong." }
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun loadReviews() {
        viewModelScope.launch {
            try {
                when (val response = getUserReviewsUseCase(currentUserId)) {
                    is NetworkResult.Error -> {
                        Log.d(TAG, "loadProfileData Error ${response.message}")
                        _errorMessage.update { response.message }
                    }

                    is NetworkResult.Success -> {
                        Log.d(TAG, "loadProfileData Success ${response.data}")
                        val data = response.data
                        _reviews.update { data.map { it.toUI() } }
                    }

                    is NetworkResult.Loading -> {
                        Log.d("LOGIN_LOADING", "Cargando...")
                    }
                }
            } catch (e: Throwable) {
                Log.d(TAG, "ERROR :  ${e.message}")
                _errorMessage.update { "Something went wrong." }
            }
        }
    }

    fun showDialog(){
        _showAddReviewDialog.update { true }
    }
    fun hideDialog(){
        _showAddReviewDialog.update { false }
    }
    fun onSelectRating(rating: Int){
        _rating.update { rating }
    }
    fun onChangeUserReviewComment(comment : String){
        _userReviewComment.update { comment }
    }
    fun onCreateUserReview(){
        //Call UseCase
        Log.d(TAG, "onCreateUserReview: ${rating.value}")
        Log.d(TAG, "onCreateUserReview: ${userReviewComment.value}")


        _isLoading.value = true
        viewModelScope.launch {
            when(val result = createUserReviewUseCase(
                reviewedId = currentUserId.toInt(),
                reviewerId = sessionRepository.getUserId()!!.toInt(),
                comment = userReviewComment.value,
                score = rating.value
            )){
                is NetworkResult.Error -> {
                    Log.d(TAG, "onCreateUserReview: ${result.message}")
                    SnackBarController.sendEvent(
                        SnackBarEvent(
                            message = result.message,
                        )
                    )
                }
                is NetworkResult.Success -> {
                    hideDialog()
                    //Reset Fields
                    _userReviewComment.value = ""
                    _rating.value = 0
                    //Show SnackBar
                    SnackBarController.sendEvent(
                        SnackBarEvent(
                            message = "Review Creada Exitosamente",
                        )
                    )
                    //Update Current review list
                    _reviews.value += result.data.toUI()
                }
                NetworkResult.Loading -> {}
            }

        }
        _isLoading.value = false
    }

}
