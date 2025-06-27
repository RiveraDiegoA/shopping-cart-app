package com.example.shoppingcartapp.presentation.auth.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingcartapp.data.local.SessionManager
import com.example.shoppingcartapp.data.remote.dto.RegisterRequest
import com.example.shoppingcartapp.data.remote.service.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authService: AuthService,
    private val sessionManager: SessionManager
) : ViewModel() {

    var uiState by mutableStateOf(RegisterUiState())
        private set

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.OnNameChange -> uiState = uiState.copy(name = event.value)
            is RegisterEvent.OnUsernameChange -> uiState = uiState.copy(username = event.value)
            is RegisterEvent.OnPasswordChange -> uiState = uiState.copy(password = event.value)
            is RegisterEvent.Submit -> handleAuth()
        }
    }

    private fun handleAuth() = viewModelScope.launch {
        uiState = uiState.copy(isLoading = true, error = null)

        if (!validateInputs()) {
            uiState = uiState.copy(isLoading = false)
            return@launch
        }

        try {
            val response = authService.register(
                RegisterRequest(
                    uiState.name,
                    uiState.username,
                    uiState.password
                )
            )
            sessionManager.saveToken(response.token)
            sessionManager.saveRole(response.role)
            sessionManager.saveUsername(response.username)
            sessionManager.saveName(response.name)
            uiState = uiState.copy(isSuccess = true, isLoading = false)
        } catch (e: Exception) {
            uiState = uiState.copy(error = e.message ?: "Error", isLoading = false)
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        val nameError = when {
            uiState.username.isEmpty() -> {
                isValid = false
                "El usuario debe tener un nombre"
            }

            else -> null
        }

        val usernameError = when {
            uiState.username.length !in 5..20 -> {
                isValid = false
                "El usuario debe tener entre 5 y 20 caracteres"
            }

            !uiState.username.any { it.isLetter() } || !uiState.username.any { it.isDigit() } -> {
                isValid = false
                "Debe contener al menos una letra y un número"
            }

            else -> null
        }

        val passwordError = when {
            uiState.password.length !in 5..20 -> {
                isValid = false
                "La contraseña debe tener entre 5 y 20 caracteres"
            }

            !uiState.password.any { it.isLetter() } || !uiState.password.any { it.isDigit() } -> {
                isValid = false
                "Debe contener al menos una letra y un número"
            }

            else -> null
        }

        uiState = uiState.copy(
            nameError = nameError,
            usernameError = usernameError,
            passwordError = passwordError
        )

        return isValid
    }
}
