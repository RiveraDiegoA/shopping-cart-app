package com.example.shoppingcartapp.presentation.products

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingcartapp.data.local.SessionManager
import com.example.shoppingcartapp.data.remote.dto.AddProductRequest
import com.example.shoppingcartapp.data.remote.dto.RemoveProductRequest
import com.example.shoppingcartapp.data.remote.service.CartService
import com.example.shoppingcartapp.data.remote.service.ProductService
import com.example.shoppingcartapp.domain.model.Product
import com.example.shoppingcartapp.domain.model.mapper.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val productService: ProductService,
    private val cartService: CartService
) : ViewModel() {

    var uiState by mutableStateOf(ProductsUiState())
        private set

    init {
        onEvent(ProductsEvent.LoadUserInfo)
        onEvent(ProductsEvent.LoadProducts)
    }

    fun onEvent(event: ProductsEvent) {
        when (event) {
            is ProductsEvent.ToggleUserSheet -> updateUserSheetVisibility(event.isVisible)
            is ProductsEvent.Logout -> logout()
            is ProductsEvent.LoadUserInfo -> loadUserInfo()
            is ProductsEvent.LoadProducts -> loadProducts()
            is ProductsEvent.AddToCart -> addProduct(event.code)
            is ProductsEvent.RemoveFromCart -> removeProduct(event.code)
        }
    }

    private fun logout() = launch {
        sessionManager.clearSession()
    }

    private fun updateUserSheetVisibility(isVisible: Boolean) = launch {
        uiState = uiState.copy(showUserSheet = isVisible)
    }

    private fun loadUserInfo() = launch {
        uiState = uiState.copy(
            isLoading = true,
            name = sessionManager.getName() ?: "--",
            username = sessionManager.getUsername() ?: "--",
            role = sessionManager.getRole() ?: "--"
        )
    }

    private fun loadProducts() = launch {
        uiState = uiState.copy(isLoading = true)
        try {
            val products = productService.getAllProducts()
            val cart = cartService.getCart()
            val updatedProducts: List<Product> = products.map {
                it.toDomain().copy(
                    inCart = cart.items.any { item -> item.productCode == it.code }
                )
            }

            uiState = uiState.copy(
                products = updatedProducts,
                cartCount = cart.items.sumOf { it.quantity },
                cartTotal = cart.total,
                isLoading = false
            )
        } catch (e: Exception) {
            uiState = uiState.copy(error = e.message, isLoading = false)
        }
    }

    private fun addProduct(code: String) = launch {
        cartService.addProductToCart(AddProductRequest(code, 1))
        onEvent(ProductsEvent.LoadProducts)
    }

    private fun removeProduct(code: String) = launch {
        cartService.removeProductFromCart(RemoveProductRequest(code))
        onEvent(ProductsEvent.LoadProducts)
    }

    private fun launch(block: suspend () -> Unit) {
        viewModelScope.launch {
            uiState = uiState.copy(error = null)
            block()
        }
    }
}
