package com.example.shoppingcartapp.presentation.products

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.shoppingcartapp.R
import com.example.shoppingcartapp.domain.constants.Constants
import com.example.shoppingcartapp.domain.model.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(
    viewModel: ProductsViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit,
    onNavigateToCart: () -> Unit
) {
    val state = viewModel.uiState
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (state.showUserSheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.onEvent(ProductsEvent.ToggleUserSheet(false)) },
            sheetState = sheetState
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp)
                )
                Text("Nombre: ${state.name}")
                Text("Usuario: ${state.username}")
                Spacer(Modifier.height(16.dp))
                Button(onClick = {
                    viewModel.onEvent(ProductsEvent.ToggleUserSheet(false))
                    viewModel.onEvent(ProductsEvent.Logout)
                    onNavigateToLogin()
                }) {
                    Text("Cerrar sesión")
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Productos",
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                actions = {
                    IconButton(onClick = { viewModel.onEvent(ProductsEvent.ToggleUserSheet(true)) }) {
                        Icon(Icons.Default.AccountCircle, contentDescription = null)
                    }
                }
            )
        },
        /*floatingActionButton = {
            if (state.cartCount > 0) {
                Button(
                    onClick = onNavigateToCart,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Ver carrito (${state.cartCount}) - S/${state.cartTotal}")
                }
            }
        }*/
        floatingActionButton = {
            if (state.cartCount > 0) {
                ExtendedFloatingActionButton(
                    text = { Text("Ver carrito (${state.cartCount}) - S/.${state.cartTotal}") },
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) },
                    onClick = onNavigateToCart
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when {
                state.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                state.error != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Error: ${state.error}", color = MaterialTheme.colorScheme.error)
                    }
                }

                state.products.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No se registraron productos aún.")
                    }
                }

                else -> {
                    /*LazyColumn {
                        items(state.products) { product ->
                            ProductItem(
                                product = product,
                                onAdd = { viewModel.onEvent(ProductsEvent.AddToCart(product.code)) },
                                onRemove = { viewModel.onEvent(ProductsEvent.RemoveFromCart(product.code)) }
                            )
                        }
                    }*/
                    ProductGrid(
                        products = state.products,
                        onAdd = { viewModel.onEvent(ProductsEvent.AddToCart(it)) },
                        onRemove = { viewModel.onEvent(ProductsEvent.RemoveFromCart(it)) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductItem(
    product: Product,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    val cardElevation = CardDefaults.cardElevation(0.dp).takeIf { product.stock <= 0 }
        ?: CardDefaults.cardElevation(4.dp)

    val cardColors = CardDefaults.outlinedCardColors()
        .copy(containerColor = Constants.BackgroundSuccess).takeIf { product.inCart }
        ?: CardDefaults.cardColors()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = cardElevation,
        colors = cardColors
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(product.name, fontWeight = FontWeight.Bold)
                Text("Stock: ${product.stock}", style = MaterialTheme.typography.bodySmall)
                Text(
                    text = "S/${product.price}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Constants.Price
                )
            }

            IconButton(onClick = if (product.inCart) onRemove else onAdd) {
                Icon(
                    painter = painterResource(if (product.inCart) R.drawable.ic_round_delete_forever else R.drawable.ic_round_add_circle_outline),
                    contentDescription = null,
                    tint = if (product.inCart) Constants.IconError else Constants.IconSuccess
                )
            }
        }
    }
}

@Composable
fun ProductGrid(
    products: List<Product>,
    onAdd: (String) -> Unit,
    onRemove: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(products) { product ->
            ProductCard(
                product = product,
                onAdd = { onAdd(product.code) },
                onRemove = { onRemove(product.code) }
            )
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                if (product.photoUrl.isNullOrEmpty()) {
                    Icon(Icons.Default.AccountBox, contentDescription = null, Modifier.size(48.dp))
                } else {
                    AsyncImage(
                        model = product.photoUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
            Text(product.name, fontWeight = FontWeight.Bold, maxLines = 1)
            Text(
                product.description.orEmpty(),
                minLines = 2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(
                Modifier
                    .fillMaxHeight()
                    .weight(1f)
            )

            val color = when {
                product.stock <= 0 -> Color.Gray
                product.inCart -> Constants.IconError
                else -> Constants.IconSuccess
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "S/${product.price}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        if (product.inCart) onRemove() else onAdd()
                    },
                    enabled = product.stock > 0,
                    modifier = Modifier.background(color, shape = CircleShape)
                ) {
                    Icon(
                        imageVector = if (product.inCart) Icons.Default.Delete else Icons.Default.Add,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }
    }
}
