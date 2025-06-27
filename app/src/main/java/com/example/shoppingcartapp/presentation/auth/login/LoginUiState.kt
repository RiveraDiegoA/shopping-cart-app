package com.example.shoppingcartapp.presentation.auth.login

import com.example.shoppingcartapp.domain.constants.Constants

data class LoginUiState(
    val username: String = Constants.EMPTY,
    val password: String = Constants.EMPTY,
    val usernameError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

