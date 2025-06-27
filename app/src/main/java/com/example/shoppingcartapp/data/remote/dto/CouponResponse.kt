package com.example.shoppingcartapp.data.remote.dto

data class CouponResponse(
    val code: String,
    val name: String,
    val discountPercent: Double
)
