package com.example.shoppingcartapp.presentation.cart

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingcartapp.data.remote.dto.AddProductRequest
import com.example.shoppingcartapp.data.remote.dto.ApplyCouponRequest
import com.example.shoppingcartapp.data.remote.dto.RemoveProductRequest
import com.example.shoppingcartapp.data.remote.service.CartService
import com.example.shoppingcartapp.domain.model.mapper.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartService: CartService
) : ViewModel() {

    var uiState by mutableStateOf(CartUiState())
        private set

    init {
        onEvent(CartEvent.LoadCart)
    }

    fun onEvent(event: CartEvent) {
        when (event) {
            is CartEvent.LoadCart -> loadCart()
            is CartEvent.RemoveProduct -> removeProduct(event.code)
            is CartEvent.IncreaseProductQuantity -> changeProductQuantity(event.code, 1)
            is CartEvent.DecreaseProductQuantity -> changeProductQuantity(event.code, -1)
            is CartEvent.ApplyCoupon -> applyCoupon(event.code)
            is CartEvent.RemoveCoupon -> removeCoupon()
            is CartEvent.ConfirmPurchase -> confirm()
        }
    }

    private fun loadCart() = launch {
        uiState = uiState.copy(isLoading = true)
        try {
            val cart = cartService.getCart()
            uiState = uiState.copy(
                products = cart.items.map { it.toDomain() },
                couponCode = cart.couponCode,
                couponName = cart.couponName,
                couponDescription = cart.couponDescription,
                couponDiscountPercent = cart.couponDiscountPercent,
                subtotal = cart.subtotal,
                discount = cart.discount,
                total = cart.total,
                totalUsd = cart.totalUSD,
                isLoading = false
            )
        } catch (e: Exception) {
            uiState = uiState.copy(errorMessage = e.message, isLoading = false)
        }
    }

    private fun changeProductQuantity(code: String, delta: Int) = launch {
        cartService.addProductToCart(AddProductRequest(code, delta))
        onEvent(CartEvent.LoadCart)
    }

    private fun removeProduct(code: String) = launch {
        cartService.removeProductFromCart(RemoveProductRequest(code))
        onEvent(CartEvent.LoadCart)
    }

    private fun applyCoupon(code: String) = launch {
        try {
            cartService.applyCoupon(ApplyCouponRequest(code))
            onEvent(CartEvent.LoadCart)
        } catch (e: Exception) {
            uiState = uiState.copy(errorMessage = e.message)
        }
    }

    private fun removeCoupon() = launch {
        cartService.removeCoupon()
        onEvent(CartEvent.LoadCart)
    }

    private fun confirm() = launch {
        try {
            cartService.confirmCart()
            uiState = uiState.copy(successMessage = "Compra exitosa")
            onEvent(CartEvent.LoadCart)
        } catch (e: Exception) {
            uiState = uiState.copy(errorMessage = e.message)
        }
    }

    private fun launch(block: suspend () -> Unit) {
        viewModelScope.launch {
            uiState = uiState.copy(successMessage = null, errorMessage = null)
            block()
        }
    }
}
