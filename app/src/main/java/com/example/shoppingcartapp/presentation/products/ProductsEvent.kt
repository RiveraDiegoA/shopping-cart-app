package com.example.shoppingcartapp.presentation.products

sealed class ProductsEvent {
    data class ToggleUserSheet(val isVisible: Boolean) : ProductsEvent()
    object Logout : ProductsEvent()
    object LoadUserInfo : ProductsEvent()
    object LoadProducts : ProductsEvent()
    data class AddToCart(val code: String) : ProductsEvent()
    data class RemoveFromCart(val code: String) : ProductsEvent()
}
