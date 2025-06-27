package com.example.shoppingcartapp.data.remote.dto

data class LoginResponse(
    val token: String,
    val role: String,
    val username: String,
    val name: String
)
