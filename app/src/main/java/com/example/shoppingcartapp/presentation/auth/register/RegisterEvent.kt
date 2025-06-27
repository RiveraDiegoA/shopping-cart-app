package com.example.shoppingcartapp.presentation.auth.register

sealed class RegisterEvent {
    data class OnNameChange(val value: String) : RegisterEvent()
    data class OnUsernameChange(val value: String) : RegisterEvent()
    data class OnPasswordChange(val value: String) : RegisterEvent()
    object Submit : RegisterEvent()
}
