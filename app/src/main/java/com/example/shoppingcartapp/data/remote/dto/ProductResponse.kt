package com.example.shoppingcartapp.data.remote.dto

data class ProductResponse(
    val code: String,
    val name: String,
    val description: String?,
    val categoryCode: String?,
    val categoryName: String?,
    val photoUrl: String?,
    val price: Double,
    val stock: Int
)