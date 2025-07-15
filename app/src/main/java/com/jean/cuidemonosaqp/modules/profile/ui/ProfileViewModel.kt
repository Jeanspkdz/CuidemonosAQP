package com.jean.cuidemonosaqp.modules.profile.ui

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.jean.cuidemonosaqp.modules.profile.domain.usecase.GetReviewsForUserUseCase
import com.jean.cuidemonosaqp.modules.profile.domain.usecase.GetUserInfoUseCase
import com.jean.cuidemonosaqp.shared.network.NetworkResult
import com.jean.cuidemonosaqp.modules.user.domain.model.User
import com.jean.cuidemonosaqp.navigation.Routes
import com.jean.cuidemonosaqp.shared.preferences.SessionCache
import com.jean.cuidemonosaqp.shared.preferences.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val getUserInfoUseCase: GetUserInfoUseCase,
    val getUserReviewsUseCase: GetReviewsForUserUseCase,
    private val sessionCache: SessionCache,
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

    private val _events = MutableStateFlow<ProfileEvent?>(null)
    val events: StateFlow<ProfileEvent?> = _events.asStateFlow()

    init {
        getCurrentUserId()
        loadProfileData()
        loadReviews()
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
                Log.d(TAG, "SESSION ID: ${sessionCache.getUserId()}")

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
    }

}

sealed class ProfileEvent {

}