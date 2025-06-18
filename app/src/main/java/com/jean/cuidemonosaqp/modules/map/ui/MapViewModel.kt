package com.jean.cuidemonosaqp.modules.map.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jean.cuidemonosaqp.modules.points.data.model.PointResponse
import com.jean.cuidemonosaqp.modules.points.domain.usecase.GetAllPointsUseCase
import com.jean.cuidemonosaqp.shared.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getAllPointsUseCase: GetAllPointsUseCase
) : ViewModel(){

    private val _points = MutableStateFlow<List<PointResponse>>(emptyList())
    val points: StateFlow<List<PointResponse>> = _points

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadPoints()
    }

    private fun loadPoints(){
        viewModelScope.launch {
            when (val result = getAllPointsUseCase()) {
                is NetworkResult.Success -> {
                    _points.value = result.data
                    Log.d("MapViewModel", "SUCCESS: ${result.data}")
                }
                is NetworkResult.Error -> {
                    _error.value = result.message ?: "Error al cargar puntos"
                }
                else -> {
                    //
                }
            }
        }
    }
}