package com.jean.cuidemonosaqp.shared.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jean.cuidemonosaqp.modules.profile.domain.usecase.GetUserInfoUseCase
import com.jean.cuidemonosaqp.modules.profile.ui.UserUI
import com.jean.cuidemonosaqp.modules.profile.ui.toUI
import com.jean.cuidemonosaqp.shared.network.NetworkResult
import com.jean.cuidemonosaqp.shared.preferences.DefaultSessionValues
import com.jean.cuidemonosaqp.shared.preferences.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val getUserInfoUseCase: GetUserInfoUseCase
) : ViewModel() {

    companion object {
        const val TAG = "SharedViewModel"
    }

    private val _isInitialLoading = MutableStateFlow(true)
    val isInitialLoading = _isInitialLoading.asStateFlow()

    private val _userId: MutableStateFlow<String?> = MutableStateFlow(null)
    val userId = _userId
        .onStart {
            observeUserId()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _userId.value
        )

    private val _user = MutableStateFlow<UserUI?>(null)
    val user = _user.onStart {
        observerUser()
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        null
    )

    val isAuthenticated = userId
        .map { it !== DefaultSessionValues.ID_DEFAULT.toString() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)


    private fun observeUserId() {
        sessionRepository.observeUserId()
            .onEach {
                Log.d(TAG, "observeUserId: $it")
                _userId.value = it
            }
            .launchIn(viewModelScope)
    }


    private fun observerUser() {
        userId.onEach {
            Log.d(TAG, "observerUser: $it")
            if (it !== null) {
                Log.d(TAG, "observerUser: fetching User Info with $it")
                loadUserInfo()
            }
        }.launchIn(viewModelScope)
    }

    fun logout(){
        viewModelScope.launch {
            sessionRepository.clearSession()
        }
    }

    private fun loadUserInfo() {
        viewModelScope.launch {
            val userId = _userId.value!!
            if (userId == DefaultSessionValues.ID_DEFAULT.toString()) {
                Log.d(TAG, "loadUserInfo: No hay nada en DataStore")
                delay(2000)
                _isInitialLoading.value = false
                return@launch
            }

            when (val result = getUserInfoUseCase(userId)) {
                is NetworkResult.Success -> {
                    _user.value = result.data.toUI()
                    Log.d(TAG, "loadUserInfo: ${_user.value} ")
                }

                is NetworkResult.Error -> {
                    //Clean Session
                    Log.d(TAG, "loadUserInfo: CLEAR SESSION")
                    sessionRepository.clearSession()
                }

                NetworkResult.Loading -> {}
            }
            if (_isInitialLoading.value) {
                _isInitialLoading.value = false
            }
        }
    }

}