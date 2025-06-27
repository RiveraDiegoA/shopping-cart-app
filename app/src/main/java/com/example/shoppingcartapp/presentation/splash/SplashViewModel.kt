package com.example.shoppingcartapp.presentation.splash

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingcartapp.data.local.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {

    var isSessionValid by mutableStateOf<Boolean>(false)
        private set

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            isSessionValid = !sessionManager.getToken().isNullOrEmpty()
        }
    }
}
