package com.example.shoppingcartapp.data.remote.service

import com.example.shoppingcartapp.data.remote.dto.ProductResponse
import retrofit2.http.GET

interface ProductService {
    @GET("api/products")
    suspend fun getAllProducts(): List<ProductResponse>
}
