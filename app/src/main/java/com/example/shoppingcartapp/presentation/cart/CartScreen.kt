package com.example.shoppingcartapp.presentation.cart

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shoppingcartapp.R
import com.example.shoppingcartapp.domain.constants.Constants
import com.example.shoppingcartapp.domain.model.CartItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    viewModel: CartViewModel = hiltViewModel(),
    onNavigateToProducts: () -> Unit
) {
    val state = viewModel.uiState
    var couponCode by remember { mutableStateOf(Constants.EMPTY) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Carrito de Compras") },
                navigationIcon = {
                    IconButton(onClick = onNavigateToProducts) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1F),
                contentAlignment = if (state.products.isNotEmpty()) Alignment.TopStart else Alignment.Center
            ) {
                when {
                    state.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    state.products.isEmpty() -> {
                        Text("Tu carrito está vacío.")
                    }

                    else -> {
                        LazyColumn {
                            items(state.products) { item ->
                                CartItemRow(item, viewModel)
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            if (state.couponCode != null) {
                Text(
                    "Cupón: ${state.couponName}",
                    fontWeight = FontWeight.Medium
                )
                Button(onClick = { viewModel.onEvent(CartEvent.RemoveCoupon) }) {
                    Text("Quitar cupón")
                }
            } else {

                OutlinedTextField(
                    value = couponCode,
                    onValueChange = { couponCode = it },
                    label = { Text("Código de cupón") },
                    modifier = Modifier.fillMaxWidth(),
                )
                Button(
                    onClick = {
                        viewModel.onEvent(CartEvent.ApplyCoupon(couponCode))
                        couponCode = Constants.EMPTY
                    },
                    modifier = Modifier.align(Alignment.End),
                    enabled = (couponCode != Constants.EMPTY) && state.products.isNotEmpty()
                ) {
                    Text("Aplicar cupón")
                }
            }

            Spacer(Modifier.height(16.dp))

            SummarySection(state)

            Spacer(Modifier.height(16.dp))

            state.successMessage?.let {
                Text(it, color = Color.Green, fontWeight = FontWeight.Bold)
            }

            state.errorMessage?.let {
                Text("Error: $it", color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = { viewModel.onEvent(CartEvent.ConfirmPurchase) },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.products.isNotEmpty()
            ) {
                Text("Confirmar compra")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartItemRow(
    item: CartItem,
    viewModel: CartViewModel
) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.name, fontWeight = FontWeight.Bold)
                Text("S/${item.price} x ${item.quantity}")
            }
            if (item.quantity > 1) {
                IconButton(onClick = {
                    viewModel.onEvent(CartEvent.DecreaseProductQuantity(item.productCode))
                }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_round_remove_circle_outline),
                        contentDescription = null,
                        tint = null
                    )
                }
            }
            Text("${item.quantity}")
            IconButton(onClick = {
                viewModel.onEvent(CartEvent.IncreaseProductQuantity(item.productCode))
            }) {
                Icon(
                    painter = painterResource(R.drawable.ic_round_add_circle_outline),
                    contentDescription = null,
                    tint = null
                )
            }
            IconButton(onClick = {
                viewModel.onEvent(CartEvent.RemoveProduct(item.productCode))
            }) {
                Icon(
                    painter = painterResource(R.drawable.ic_round_delete_forever),
                    contentDescription = null,
                    tint = Constants.IconError
                )
            }
        }
    }
}

@Composable
fun SummarySection(state: CartUiState) {
    Column {
        Text("Subtotal: S/${state.subtotal}")
        Text("Descuento: S/${state.discount}")
        Text("Total: S/${state.total}", fontWeight = FontWeight.Bold)
        Text("Total USD: ${state.totalUsd}")
    }
}
