package com.example.shoppingcartapp.domain.model.mapper

import com.example.shoppingcartapp.data.remote.dto.CouponResponse
import com.example.shoppingcartapp.domain.model.Coupon

fun CouponResponse.toDomain() = Coupon(
    code = this.code,
    name = this.name,
    discountPercent = this.discountPercent
)