package com.example.shoppingcartapp.domain.model.mapper

import com.example.shoppingcartapp.data.remote.dto.ProductResponse
import com.example.shoppingcartapp.domain.model.Product

fun ProductResponse.toDomain() = Product(
    code = this.code,
    name = this.name,
    description = this.description,
    categoryCode = this.categoryCode,
    categoryName = this.categoryName,
    photoUrl = this.photoUrl,
    price = this.price,
    stock = this.stock,
    inCart = false
)