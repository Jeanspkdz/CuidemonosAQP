package com.jean.cuidemonosaqp.modules.notification.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jean.cuidemonosaqp.modules.notification.data.dto.UserSafeZoneInvitationUpdateDto
import com.jean.cuidemonosaqp.modules.notification.domain.usecase.GetUserSafeZoneInvitations
import com.jean.cuidemonosaqp.modules.notification.domain.usecase.UpdateUserSafeZoneInvitation
import com.jean.cuidemonosaqp.shared.network.NetworkResult
import com.jean.cuidemonosaqp.shared.preferences.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val getUserSafeZoneInvitations: GetUserSafeZoneInvitations,
    private val updateUserSafeZoneInvitation: UpdateUserSafeZoneInvitation,
    private val sessionRepository: SessionRepository,
) : ViewModel() {

    companion object {
        const val TAG = "NotificationsViewModel"
    }

    private val _invitations = MutableStateFlow<List<SafeZoneInvitationUI>>(emptyList())
    val invitations: StateFlow<List<SafeZoneInvitationUI>> = _invitations.asStateFlow()


    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    init {
        loadInvitations()
    }

    private fun loadInvitations() {
        viewModelScope.launch {
            _isLoading.value = true
            when (val response = getUserSafeZoneInvitations(sessionRepository.getUserId())) {
                is NetworkResult.Success -> {
                    Log.d(TAG, "loadInvitations Success: ${response.data}")
                    val invitationsUI = response.data.map {
                        Log.d(TAG, "loadInvitations to UI: ${it.toUI()}")
                        it.toUI()
                    }
                    _invitations.value = invitationsUI
                }

                is NetworkResult.Error -> {
                    Log.e(TAG, "loadInvitations Error: ${response.message}")
                    _errorMessage.value = response.message
                }

                is NetworkResult.Loading -> {
                    Log.d(TAG, "loadInvitations Loading...")
                }
            }
            _isLoading.value = false

        }
    }

    fun markInvitationAsSeen(invitationId: String) {
        viewModelScope.launch {
            val invitationSeen = _invitations.value.find {
                it.id == invitationId
            }

            when (val response = updateUserSafeZoneInvitation(
                invitationId = invitationId,
                UserSafeZoneInvitationUpdateDto(
                    isSeen = true,
                    confirmedAt = invitationSeen?.confirmedAt,
                    status = invitationSeen?.status.toString()
                )
            )) {
                is NetworkResult.Success -> {
                    Log.d(TAG, "markInvitationAsSeen Success: ${response.data}")
                    val invitationUpdated = response.data.toUI()
                    _invitations.value = _invitations.value.map {
                        if(it.id == invitationUpdated.id){
                            invitationUpdated
                        }else {
                            it
                        }
                   }
                }

                is NetworkResult.Error -> {
                    Log.e(TAG, "loadInvitations Error: ${response.message}")
                    _errorMessage.value = response.message
                }

                is NetworkResult.Loading -> {
                    Log.d(TAG, "loadInvitations Loading...")
                }
            }
        }
    }

    fun acceptInvitation(invitationId: String){
        viewModelScope.launch {
            val invitationAccepted = _invitations.value.find {
                it.id == invitationId
            }

            when (val response = updateUserSafeZoneInvitation(
                invitationId = invitationId,
                UserSafeZoneInvitationUpdateDto(
                    isSeen = invitationAccepted!!.isSeen,
                    confirmedAt = invitationAccepted.confirmedAt,
                    status = SafeZoneInvitationStatus.ACCEPTED.toString()
                )
            )) {
                is NetworkResult.Success -> {
                    Log.d(TAG, "markInvitationAsSeen Success: ${response.data}")
                    val invitationUpdated = response.data.toUI()
                    _invitations.value = _invitations.value.map {
                        if(it.id == invitationUpdated.id){
                            invitationUpdated
                        }else {
                            it
                        }
                    }
                }

                is NetworkResult.Error -> {
                    Log.e(TAG, "loadInvitations Error: ${response.message}")
                    _errorMessage.value = response.message
                }

                is NetworkResult.Loading -> {
                    Log.d(TAG, "loadInvitations Loading...")
                }
            }
        }
    }

    fun rejectInvitation(invitationId: String){
        viewModelScope.launch {
            val invitationAccepted = _invitations.value.find {
                it.id == invitationId
            }

            when (val response = updateUserSafeZoneInvitation(
                invitationId = invitationId,
                UserSafeZoneInvitationUpdateDto(
                    isSeen = invitationAccepted!!.isSeen,
                    confirmedAt = invitationAccepted.confirmedAt,
                    status = SafeZoneInvitationStatus.REJECTED.toString()
                )
            )) {
                is NetworkResult.Success -> {
                    Log.d(TAG, "markInvitationAsSeen Success: ${response.data}")
                    val invitationUpdated = response.data.toUI()
                    _invitations.value = _invitations.value.map {
                        if(it.id == invitationUpdated.id){
                            invitationUpdated
                        }else {
                            it
                        }
                    }
                }

                is NetworkResult.Error -> {
                    Log.e(TAG, "loadInvitations Error: ${response.message}")
                    _errorMessage.value = response.message
                }

                is NetworkResult.Loading -> {
                    Log.d(TAG, "loadInvitations Loading...")
                }
            }
        }
    }
}