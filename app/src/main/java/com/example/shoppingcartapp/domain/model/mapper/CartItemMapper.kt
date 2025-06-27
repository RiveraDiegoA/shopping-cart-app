package com.example.shoppingcartapp.domain.model.mapper

import com.example.shoppingcartapp.data.remote.dto.CartItemResponse
import com.example.shoppingcartapp.domain.model.CartItem

fun CartItemResponse.toDomain() = CartItem(
    productCode = this.productCode,
    name = this.name,
    description = this.description,
    categoryCode = this.categoryCode,
    categoryName = this.categoryName,
    photoUrl = this.photoUrl,
    price = this.price,
    stock = this.stock,
    quantity = this.quantity
)