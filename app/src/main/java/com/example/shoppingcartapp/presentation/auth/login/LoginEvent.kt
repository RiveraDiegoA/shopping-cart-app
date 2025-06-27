package com.example.shoppingcartapp.presentation.auth.login

sealed class LoginEvent {
    data class OnEmailChange(val username: String) : LoginEvent()
    data class OnPasswordChange(val password: String) : LoginEvent()
    data object Submit : LoginEvent()
}
