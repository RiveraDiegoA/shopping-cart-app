package com.example.shoppingcartapp.presentation.auth.register

import com.example.shoppingcartapp.domain.constants.Constants

data class RegisterUiState(
    val name: String = Constants.EMPTY,
    val username: String = Constants.EMPTY,
    val password: String = Constants.EMPTY,
    val nameError: String? = null,
    val usernameError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)
