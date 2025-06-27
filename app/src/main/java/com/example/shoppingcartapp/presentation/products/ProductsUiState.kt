package com.example.shoppingcartapp.presentation.products

import com.example.shoppingcartapp.domain.constants.Constants
import com.example.shoppingcartapp.domain.model.Product

data class ProductsUiState(
    val name: String = Constants.EMPTY,
    val username: String = Constants.EMPTY,
    val role: String = Constants.EMPTY,
    val showUserSheet: Boolean = false,

    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val cartCount: Int = Constants.DEFAULT_NUMBER,
    val cartTotal: Double = Constants.DEFAULT_DECIMAL,
    val error: String? = null
)
