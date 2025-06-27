package com.example.shoppingcartapp.data.remote.service

import com.example.shoppingcartapp.data.remote.dto.LoginRequest
import com.example.shoppingcartapp.data.remote.dto.LoginResponse
import com.example.shoppingcartapp.data.remote.dto.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): LoginResponse
}
