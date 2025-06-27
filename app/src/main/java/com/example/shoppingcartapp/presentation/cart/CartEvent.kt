package com.example.shoppingcartapp.presentation.cart

sealed class CartEvent {
    object LoadCart : CartEvent()
    data class IncreaseProductQuantity(val code: String) : CartEvent()
    data class DecreaseProductQuantity(val code: String) : CartEvent()
    data class RemoveProduct(val code: String) : CartEvent()
    data class ApplyCoupon(val code: String) : CartEvent()
    object RemoveCoupon : CartEvent()
    object ConfirmPurchase : CartEvent()
}
