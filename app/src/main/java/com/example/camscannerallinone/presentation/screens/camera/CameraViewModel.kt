package com.example.camscannerallinone.presentation.screens.camera

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.camscannerallinone.domain.usecase.SaveScanUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CameraState(
    val isCapturing: Boolean = false,
    val isFlashOn: Boolean = false,
    val capturedImage: Bitmap? = null,
    val error: String? = null
)

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val saveScanUseCase: SaveScanUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CameraState())
    val state = _state.asStateFlow()

    fun onImageCaptured(bitmap: Bitmap) {
        _state.value = _state.value.copy(isCapturing = true)
        viewModelScope.launch {
            try {
                saveScanUseCase(bitmap, null)
                _state.value = _state.value.copy(isCapturing = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(isCapturing = false, error = e.message)
            }
        }
    }

    fun toggleFlash() {
        _state.value = _state.value.copy(isFlashOn = !state.value.isFlashOn)
    }
}
