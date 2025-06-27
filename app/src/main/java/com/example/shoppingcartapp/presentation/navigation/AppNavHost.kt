package com.example.shoppingcartapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shoppingcartapp.presentation.auth.login.LoginScreen
import com.example.shoppingcartapp.presentation.auth.register.RegisterScreen
import com.example.shoppingcartapp.presentation.cart.CartScreen
import com.example.shoppingcartapp.presentation.products.ProductsScreen
import com.example.shoppingcartapp.presentation.splash.SplashScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.SPLASH
    ) {
        composable(NavigationRoutes.SPLASH) {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(NavigationRoutes.LOGIN) {
                        popUpTo(NavigationRoutes.SPLASH) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToCart = {
                    navController.navigate(NavigationRoutes.PRODUCTS) {
                        popUpTo(NavigationRoutes.SPLASH) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(NavigationRoutes.LOGIN) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(NavigationRoutes.REGISTER) },
                onLoginSuccess = {
                    navController.navigate(NavigationRoutes.PRODUCTS) {
                        popUpTo(NavigationRoutes.LOGIN) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(NavigationRoutes.REGISTER) {
            RegisterScreen(
                onNavigateToLogin = { navController.navigate(NavigationRoutes.LOGIN) },
                onRegisterSuccess = {
                    navController.navigate(NavigationRoutes.PRODUCTS) {
                        popUpTo(NavigationRoutes.REGISTER) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(NavigationRoutes.PRODUCTS) {
            ProductsScreen(
                onNavigateToCart = { navController.navigate(NavigationRoutes.CART) },
                onNavigateToLogin = { navController.navigate(NavigationRoutes.LOGIN) }
            )
        }
        composable(NavigationRoutes.CART) {
            CartScreen(
                onNavigateToProducts = { navController.navigate(NavigationRoutes.PRODUCTS) },
            )
        }
    }
}