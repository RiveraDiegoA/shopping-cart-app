package com.example.shoppingcartapp.presentation.auth.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingcartapp.data.local.SessionManager
import com.example.shoppingcartapp.data.remote.dto.LoginRequest
import com.example.shoppingcartapp.data.remote.service.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService: AuthService,
    private val sessionManager: SessionManager
) : ViewModel() {

    var uiState by mutableStateOf(LoginUiState(username = "aje25", password = "aje25"))
        private set

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnEmailChange -> uiState = uiState.copy(username = event.username)
            is LoginEvent.OnPasswordChange -> uiState = uiState.copy(password = event.password)
            is LoginEvent.Submit -> login()
        }
    }

    private fun login() = viewModelScope.launch {
        uiState = uiState.copy(isLoading = true)

        if (!validateInputs()) {
            uiState = uiState.copy(isLoading = false)
            return@launch
        }

        try {
            val response = authService.login(LoginRequest(uiState.username, uiState.password))
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
            usernameError = usernameError,
            passwordError = passwordError
        )

        return isValid
    }
}

