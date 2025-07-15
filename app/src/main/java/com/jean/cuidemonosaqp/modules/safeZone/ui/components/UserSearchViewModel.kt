package com.jean.cuidemonosaqp.modules.safeZone.ui.createPoint.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jean.cuidemonosaqp.modules.user.domain.model.User
import com.jean.cuidemonosaqp.modules.user.domain.usecase.SearchUsersUseCase
import com.jean.cuidemonosaqp.shared.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Estado para la búsqueda de usuarios
 */
data class UserSearchState(
    val searchQuery: String = "",
    val users: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * ViewModel para manejar la búsqueda de usuarios
 */
@HiltViewModel
class UserSearchViewModel @Inject constructor(
    private val searchUsersUseCase: SearchUsersUseCase
) : ViewModel() {

    companion object {
        private const val SEARCH_DELAY = 300L // Delay para evitar búsquedas excesivas
    }

    private val _state = MutableStateFlow(UserSearchState())
    val state = _state.asStateFlow()

    private var searchJob: Job? = null

    /**
     * Actualiza la consulta de búsqueda
     * @param query Nueva consulta de búsqueda
     */
    fun onSearchQueryChange(query: String) {
        _state.value = _state.value.copy(searchQuery = query)

        // Cancelar búsqueda anterior
        searchJob?.cancel()

        // Iniciar nueva búsqueda con delay
        searchJob = viewModelScope.launch {
            delay(SEARCH_DELAY)
            searchUsers(query)
        }
    }

    /**
     * Busca usuarios basado en la consulta
     * @param query Consulta de búsqueda
     */
    private suspend fun searchUsers(query: String) {
        if (query.isBlank()) {
            _state.value = _state.value.copy(
                users = emptyList(),
                isLoading = false,
                error = null
            )
            return
        }

        _state.value = _state.value.copy(
            isLoading = true,
            error = null
        )

        when (val result = searchUsersUseCase(query)) {
            is NetworkResult.Success -> {
                _state.value = _state.value.copy(
                    users = result.data,
                    isLoading = false,
                    error = null
                )
            }
            is NetworkResult.Error -> {
                _state.value = _state.value.copy(
                    users = emptyList(),
                    isLoading = false,
                    error = result.message
                )
            }
            is NetworkResult.Loading -> {
                _state.value = _state.value.copy(
                    isLoading = true,
                    error = null
                )
            }
        }
    }

    /**
     * Limpia la búsqueda actual
     */
    fun clearSearch() {
        searchJob?.cancel()
        _state.value = UserSearchState()
    }
}