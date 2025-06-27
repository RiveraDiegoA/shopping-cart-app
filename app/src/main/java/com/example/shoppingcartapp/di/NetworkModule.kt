package com.example.shoppingcartapp.di

import com.example.shoppingcartapp.data.local.SessionManager
import com.example.shoppingcartapp.data.remote.AuthInterceptor
import com.example.shoppingcartapp.data.remote.service.AuthService
import com.example.shoppingcartapp.data.remote.service.CartService
import com.example.shoppingcartapp.data.remote.service.ProductService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideInterceptor(sessionManager: SessionManager): Interceptor =
        AuthInterceptor { sessionManager.getToken() }

    @Provides
    fun provideHttpClient(authInterceptor: Interceptor): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.100.6:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    @Provides
    fun provideAuthService(retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @Provides
    fun provideProductService(retrofit: Retrofit): ProductService =
        retrofit.create(ProductService::class.java)

    @Provides
    fun provideCartService(retrofit: Retrofit): CartService =
        retrofit.create(CartService::class.java)
}
