package com.example.shoppingcartapp.data.remote.dto

data class CartItemResponse(
    val productCode: String,
    val name: String,
    val description: String?,
    val categoryCode: String?,
    val categoryName: String?,
    val photoUrl: String?,
    val price: Double,
    val stock: Int,
    val quantity: Int
)
