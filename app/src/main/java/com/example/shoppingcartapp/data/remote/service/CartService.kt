package com.example.shoppingcartapp.data.remote.service

import com.example.shoppingcartapp.data.remote.dto.AddProductRequest
import com.example.shoppingcartapp.data.remote.dto.ApplyCouponRequest
import com.example.shoppingcartapp.data.remote.dto.CartResponse
import com.example.shoppingcartapp.data.remote.dto.RemoveProductRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CartService {
    @GET("api/cart")
    suspend fun getCart(): CartResponse

    @POST("api/cart/add")
    suspend fun addProductToCart(@Body body: AddProductRequest)

    @POST("api/cart/remove")
    suspend fun removeProductFromCart(@Body body: RemoveProductRequest)

    @POST("api/cart/apply-coupon")
    suspend fun applyCoupon(@Body body: ApplyCouponRequest)

    @POST("api/cart/remove-coupon")
    suspend fun removeCoupon()

    @POST("api/cart/confirm")
    suspend fun confirmCart()
}
