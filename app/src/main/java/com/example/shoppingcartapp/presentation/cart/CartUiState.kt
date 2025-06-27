package com.example.shoppingcartapp.presentation.cart

import com.example.shoppingcartapp.domain.constants.Constants
import com.example.shoppingcartapp.domain.model.CartItem

data class CartUiState(
    val isLoading: Boolean = false,
    val products: List<CartItem> = emptyList(),
    val couponCode: String? = null,
    val couponName: String? = null,
    val couponDescription: String? = null,
    val couponDiscountPercent: Double? = null,
    val subtotal: Double = Constants.DEFAULT_DECIMAL,
    val discount: Double = Constants.DEFAULT_DECIMAL,
    val total: Double = Constants.DEFAULT_DECIMAL,
    val totalUsd: Double = Constants.DEFAULT_DECIMAL,
    val successMessage: String? = null,
    val errorMessage: String? = null
)
