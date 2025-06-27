package com.example.shoppingcartapp.data.remote.dto

data class CartResponse(
    val code: String?,
    val items: List<CartItemResponse>,
    val couponCode: String?,
    val couponName: String?,
    val couponDescription: String?,
    val couponDiscountPercent: Double?,
    val subtotal: Double,
    val discount: Double,
    val total: Double,
    val totalUSD: Double,
    val confirmed: Boolean
)
