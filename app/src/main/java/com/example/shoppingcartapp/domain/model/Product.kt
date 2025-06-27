package com.example.shoppingcartapp.domain.model

data class Product(
    val code: String,
    val name: String,
    val description: String?,
    val categoryCode: String?,
    val categoryName: String?,
    val photoUrl: String?,
    val price: Double,
    val stock: Int,
    val inCart: Boolean
)

