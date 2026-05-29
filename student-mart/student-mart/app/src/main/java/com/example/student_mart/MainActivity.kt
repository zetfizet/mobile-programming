package com.example.student_mart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.student_mart.ui.theme.StudentmartTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.DecimalFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            StudentmartTheme {
                MainScreen()
            }
        }
    }
}

// Helper untuk format harga
fun String.toNumericPrice(): Long = this.replace("Rp", "").replace(".", "").trim().toLongOrNull() ?: 0L
fun Long.formatPrice(): String = "Rp" + DecimalFormat("#,###").format(this).replace(",", ".")

sealed class Screen(val route: String, val icon: ImageVector, val label: String) {
    object Home : Screen("home", Icons.Default.Home, "Home")
    object Cart : Screen("cart", Icons.Default.ShoppingCart, "Cart")
    object Profile : Screen("profile", Icons.Default.Person, "Profile")
    object Detail : Screen("detail/{productName}", Icons.Default.Info, "Detail") {
        fun createRoute(productName: String) = "detail/$productName"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val cartItems = remember { mutableStateListOf<Product>() }

    var showNotification by remember { mutableStateOf(false) }
    var notificationMessage by remember { mutableStateOf("") }
    var notificationIcon by remember { mutableStateOf<ImageVector>(Icons.Default.Check) }
    var notificationColor by remember { mutableStateOf(Color(0xFF4CAF50)) } // Default Success Green

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = currentRoute in listOf(Screen.Home.route, Screen.Cart.route, Screen.Profile.route)

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.navigationBarsPadding(),
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                if (showBottomBar) {
                    Surface(
                        modifier = Modifier.fillMaxWidth().shadow(20.dp, RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)),
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                    ) {
                        NavigationBar(containerColor = Color.Transparent, tonalElevation = 0.dp) {
                            val items = listOf(Screen.Home, Screen.Cart, Screen.Profile)
                            items.forEach { screen ->
                                val isSelected = currentRoute == screen.route
                                NavigationBarItem(
                                    icon = { 
                                        BadgedBox(
                                            badge = {
                                                if (screen == Screen.Cart && cartItems.isNotEmpty()) {
                                                    Badge(containerColor = Color(0xFFE91E63)) { 
                                                        Text(cartItems.size.toString(), color = Color.White) 
                                                    }
                                                }
                                            }
                                        ) { Icon(screen.icon, contentDescription = screen.label) }
                                    },
                                    label = { Text(screen.label) },
                                    selected = isSelected,
                                    onClick = {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        ) { paddingValues ->
            NavHost(navController, startDestination = Screen.Home.route, modifier = Modifier.padding(paddingValues)) {
                composable(Screen.Home.route) {
                    HomeScreen(
                        onAddToCart = { product ->
                            cartItems.add(product)
                            notificationMessage = "Successfully added ${product.name} to cart! 🛒"
                            notificationIcon = Icons.Default.Check
                            notificationColor = Color(0xFF4CAF50)
                            showNotification = true
                        },
                        onProductClick = { navController.navigate(Screen.Detail.createRoute(it.name)) }
                    )
                }
                composable(Screen.Cart.route) {
                    CartScreen(
                        cartItems = cartItems,
                        onIncrease = { product -> 
                            cartItems.add(product)
                            notificationMessage = "Increased quantity of ${product.name}! 📈"
                            notificationIcon = Icons.Default.Add
                            notificationColor = Color(0xFF2196F3) // Info Blue
                            showNotification = true
                        },
                        onDecrease = { product -> 
                            cartItems.remove(product)
                            notificationMessage = "Decreased quantity of ${product.name}! 📉"
                            notificationIcon = Icons.Default.Remove
                            notificationColor = Color(0xFFFF9800) // Warning Orange
                            showNotification = true
                        },
                        onRemove = { product -> 
                            cartItems.removeAll { it.name == product.name }
                            notificationMessage = "Removed ${product.name} from cart! 🗑️"
                            notificationIcon = Icons.Default.Delete
                            notificationColor = Color(0xFFF44336) // Danger Red
                            showNotification = true
                        },
                        onCheckout = { 
                            scope.launch { snackbarHostState.showSnackbar("Success! Order placed.") }
                            cartItems.clear()
                        },
                        onStartShopping = { navController.navigate(Screen.Home.route) },
                        onProductClick = { navController.navigate(Screen.Detail.createRoute(it.name)) }
                    )
                }
                composable(Screen.Profile.route) { ProfileScreen() }
                composable(Screen.Detail.route, arguments = listOf(navArgument("productName") { type = NavType.StringType })) {
                    val name = it.arguments?.getString("productName")
                    productList.find { p -> p.name == name }?.let { p ->
                        ProductDetailScreen(p, onBack = { navController.popBackStack() }, 
                            onAddToCart = { 
                                cartItems.add(p)
                                notificationMessage = "Successfully added ${p.name} to cart! 🛒"
                                notificationIcon = Icons.Default.Check
                                notificationColor = Color(0xFF4CAF50)
                                showNotification = true
                            },
                            onProductClick = { related -> navController.navigate(Screen.Detail.createRoute(related.name)) }
                        )
                    }
                }
            }
        }

        // Custom Dynamic Cart Notification Popup
        AnimatedVisibility(
            visible = showNotification,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 48.dp, start = 24.dp, end = 24.dp)
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier.fillMaxWidth().border(1.dp, notificationColor.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = notificationColor,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                notificationIcon,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = notificationMessage,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            LaunchedEffect(showNotification) {
                if (showNotification) {
                    delay(2500)
                    showNotification = false
                }
            }
        }
    }
}

@Composable
fun CartScreen(
    cartItems: List<Product>,
    onIncrease: (Product) -> Unit,
    onDecrease: (Product) -> Unit,
    onRemove: (Product) -> Unit,
    onCheckout: () -> Unit,
    onStartShopping: () -> Unit,
    onProductClick: (Product) -> Unit
) {
    if (cartItems.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🛒", fontSize = 100.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Your cart is empty", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text("Looks like you haven't added anything yet.", color = Color.Gray, textAlign = TextAlign.Center, modifier = Modifier.padding(16.dp))
                Button(onClick = onStartShopping, shape = RoundedCornerShape(12.dp)) { Text("Start Shopping") }
            }
        }
    } else {
        val groupedItems = cartItems.groupBy { it.name }.map { it.value.first() to it.value.size }
        val subtotal = cartItems.sumOf { it.price.toNumericPrice() }
        val shipping = 10000L
        val discount = if (subtotal > 100000) 20000L else 0L
        val total = subtotal + shipping - discount

        Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 24.dp, bottom = 140.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("My Cart", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
                        Spacer(modifier = Modifier.width(12.dp))
                        Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = CircleShape) {
                            Text("${cartItems.size} Items", modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                items(groupedItems) { (product, qty) ->
                    CartItemCard(
                        product = product, 
                        qty = qty, 
                        onIncrease = { onIncrease(product) }, 
                        onDecrease = { onDecrease(product) }, 
                        onRemove = { onRemove(product) }
                    )
                }

                item {
                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.ConfirmationNumber, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Use Voucher / Promo Code", fontWeight = FontWeight.Bold)
                                Text("STUDENT50 applied", style = MaterialTheme.typography.labelSmall, color = Color(0xFF2E7D32))
                            }
                            Icon(Icons.Default.ChevronRight, contentDescription = null)
                        }
                    }
                }

                item {
                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), shape = RoundedCornerShape(16.dp), border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text("Payment & Delivery", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocalShipping, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color.Gray)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Estimated Arrival: Tomorrow - 2 Days", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color.Gray)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Payment: E-Wallet / Bank Transfer", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                            }
                            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                            SummaryRow("Subtotal", subtotal.formatPrice())
                            SummaryRow("Shipping Fee", shipping.formatPrice())
                            if (discount > 0) SummaryRow("Discount", "- ${discount.formatPrice()}", color = Color(0xFF2E7D32))
                            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                            SummaryRow("Total Payment", total.formatPrice(), isTotal = true)
                        }
                    }
                }

                item {
                    Text("You may also like", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
                    LazyRow(modifier = Modifier.padding(top = 12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(productList.filter { !cartItems.contains(it) }.take(4)) { p ->
                            Box(modifier = Modifier.width(160.dp)) {
                                ProductCard(product = p, onAddToCart = { onIncrease(p) }, onClick = { onProductClick(p) })
                            }
                        }
                    }
                }
            }

            Surface(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().shadow(12.dp), color = MaterialTheme.colorScheme.surface) {
                Button(onClick = onCheckout, modifier = Modifier.fillMaxWidth().padding(16.dp).height(56.dp), shape = RoundedCornerShape(16.dp)) {
                    Text("Proceed to Checkout", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun CartItemCard(product: Product, qty: Int, onIncrease: () -> Unit, onDecrease: () -> Unit, onRemove: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), elevation = CardDefaults.cardElevation(2.dp)) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(80.dp).background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                if (product.imageRes != null) { Image(painter = painterResource(product.imageRes), contentDescription = null, modifier = Modifier.padding(8.dp)) } 
                else { Text(product.emoji, fontSize = 32.sp) }
            }
            Column(modifier = Modifier.weight(1f).padding(horizontal = 12.dp)) {
                Text(product.name, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(product.price, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.ExtraBold)
                Spacer(modifier = Modifier.height(12.dp))
                
                // Fixed/Adjusted Button Sizes (+ and -) - Forced consistent design with layout constraint
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.height(28.dp)
                ) {
                    // Custom tiny elegant minus button
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                            .clickable { onDecrease() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Remove, 
                            contentDescription = "Decrease", 
                            modifier = Modifier.size(12.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Text(
                        text = "$qty", 
                        fontWeight = FontWeight.Bold, 
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.widthIn(min = 16.dp)
                    )

                    // Custom tiny elegant plus button (Replaced IconButton to prevent automatic Android minimum size expansion)
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .clickable { onIncrease() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Add, 
                            contentDescription = "Increase", 
                            tint = Color.White, 
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                IconButton(onClick = onRemove, modifier = Modifier.size(36.dp)) { Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red.copy(alpha = 0.6f), modifier = Modifier.size(20.dp)) }
                Text("Subtotal", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                Text((product.price.toNumericPrice() * qty).formatPrice(), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}

@Composable
fun SummaryRow(label: String, value: String, isTotal: Boolean = false, color: Color = Color.Unspecified) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal, color = if (isTotal) MaterialTheme.colorScheme.onSurface else Color.Gray)
        Text(value, fontWeight = if (isTotal) FontWeight.ExtraBold else FontWeight.Bold, color = if (isTotal) MaterialTheme.colorScheme.primary else color)
    }
}

@Composable
fun HomeScreen(onAddToCart: (Product) -> Unit, onProductClick: (Product) -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredProducts = productList.filter {
        it.name.contains(searchQuery, ignoreCase = true) || it.category.contains(searchQuery, ignoreCase = true)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("Hello, Rafie!", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
                    Text("StudentMart 🎓", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onBackground))
                }
                Surface(
                    onClick = { },
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 4.dp,
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.Notifications, 
                            contentDescription = null, 
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }

        item {
            OutlinedTextField(
                value = searchQuery, onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(16.dp)),
                placeholder = { Text("Search your study needs...", color = MaterialTheme.colorScheme.secondary) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = MaterialTheme.colorScheme.primary
                ),
                singleLine = true
            )
        }

        item {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)) {
                Row(modifier = Modifier.padding(24.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Electronics Promo", color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f), style = MaterialTheme.typography.labelMedium)
                        Text("Up to 50% Off", color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = { }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onPrimary), shape = RoundedCornerShape(8.dp)) {
                            Text("Check Now", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Text("💻", fontSize = 60.sp)
                }
            }
        }

        item { Text("Recommended Products", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground) }

        items(filteredProducts.chunked(2)) { rowItems ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                rowItems.forEach { product ->
                    Box(modifier = Modifier.weight(1f)) {
                        ProductCard(product = product, onAddToCart = { onAddToCart(product) }, onClick = { onProductClick(product) })
                    }
                }
                if (rowItems.size == 1) Spacer(modifier = Modifier.weight(1f))
            }
        }
        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
fun ProductDetailScreen(product: Product, onBack: () -> Unit, onAddToCart: () -> Unit, onProductClick: (Product) -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(bottom = 80.dp)) {
            Box(modifier = Modifier.fillMaxWidth().height(350.dp).background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))) {
                if (product.imageRes != null) { Image(painter = painterResource(id = product.imageRes), contentDescription = product.name, modifier = Modifier.fillMaxSize().padding(24.dp), contentScale = ContentScale.Fit) } 
                else { Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(text = product.emoji, fontSize = 100.sp) } }
                IconButton(onClick = onBack, modifier = Modifier.padding(16.dp).background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f), CircleShape).align(Alignment.TopStart)) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onSurface)
                }
            }
            Column(modifier = Modifier.padding(20.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = product.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    Text(text = product.price, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.ExtraBold)
                }
                Row(modifier = Modifier.padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB400), modifier = Modifier.size(20.dp))
                    Text(" ${product.rating} ", fontWeight = FontWeight.Bold)
                    Text("(${product.reviewCount} Reviews)", color = Color.Gray)
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                Text("Description", fontWeight = FontWeight.Bold)
                Text(text = product.description, modifier = Modifier.padding(top = 8.dp), lineHeight = 22.sp)
                Spacer(modifier = Modifier.height(24.dp))
                Text("Seller Information", fontWeight = FontWeight.Bold)
                Row(modifier = Modifier.padding(top = 12.dp).fillMaxWidth().background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(12.dp)).padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(45.dp).background(MaterialTheme.colorScheme.surface, CircleShape), contentAlignment = Alignment.Center) { Text(product.sellerImage, fontSize = 24.sp) }
                    Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                        Text(text = product.sellerName, fontWeight = FontWeight.Bold)
                        Text(text = "Active 10 mins ago", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                    OutlinedButton(onClick = { }, shape = RoundedCornerShape(8.dp)) { Text("Visit Store", fontSize = 12.sp) }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text("Specifications", fontWeight = FontWeight.Bold)
                product.specifications.forEach { (key, value) ->
                    Row(modifier = Modifier.padding(top = 8.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = key, color = Color.Gray); Text(text = value, fontWeight = FontWeight.Medium)
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text("Related Products", fontWeight = FontWeight.Bold)
                LazyRow(modifier = Modifier.padding(top = 12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(productList.filter { it.category == product.category && it.name != product.name }) { related ->
                        Box(modifier = Modifier.width(160.dp)) { ProductCard(product = related, onAddToCart = { }, onClick = { onProductClick(related) }) }
                    }
                }
            }
        }
        Surface(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().shadow(12.dp), color = MaterialTheme.colorScheme.surface) {
            Row(modifier = Modifier.padding(16.dp).navigationBarsPadding(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = onAddToCart, modifier = Modifier.weight(1f).height(50.dp), shape = RoundedCornerShape(12.dp)) {
                    Icon(Icons.Default.ShoppingCart, modifier = Modifier.size(20.dp), contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp)); Text("Add to Cart")
                }
                Button(onClick = { }, modifier = Modifier.weight(1f).height(50.dp), shape = RoundedCornerShape(12.dp)) { Text("Buy Now") }
            }
        }
    }
}

@Composable
fun ProfileScreen() {
    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).verticalScroll(rememberScrollState())) {
        Box(modifier = Modifier.fillMaxWidth().background(brush = Brush.verticalGradient(colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primary.copy(alpha = 0.8f))), shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)).padding(32.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.size(100.dp).background(Color.White, CircleShape).padding(4.dp), contentAlignment = Alignment.Center) { Text("👨‍🎓", fontSize = 60.sp) }
                Spacer(modifier = Modifier.height(16.dp))
                Text("Rafie Zaidan U", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Color.White)
                Text("Informatics Engineering Student", color = Color.White.copy(alpha = 0.8f))
            }
        }
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    ProfileStatItem(Icons.Default.AccountBalanceWallet, "To Pay")
                    ProfileStatItem(Icons.Default.Inventory, "Shipping")
                    ProfileStatItem(Icons.Default.LocalShipping, "Arrived")
                    ProfileStatItem(Icons.Default.Star, "Review")
                }
            }
            Column(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp)).border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))) {
                ProfileMenuItem(Icons.Default.ConfirmationNumber, "My Vouchers", "3 active vouchers")
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp)); ProfileMenuItem(Icons.Default.LocationOn, "Shipping Address", "Surabaya, East Java")
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp)); ProfileMenuItem(Icons.Default.Payment, "Payment Method", "ShopeePay / Bank Transfer")
            }
            Button(onClick = { }, modifier = Modifier.fillMaxWidth().height(56.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEE2E2), contentColor = Color.Red), shape = RoundedCornerShape(16.dp)) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null); Spacer(modifier = Modifier.width(8.dp)); Text("Logout", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ProfileStatItem(icon: ImageVector, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
    }
}

@Composable
fun ProfileMenuItem(icon: ImageVector, title: String, subtitle: String) {
    Row(modifier = Modifier.fillMaxWidth().clickable { }.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(40.dp).background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f), CircleShape), contentAlignment = Alignment.Center) { Icon(icon, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp), contentDescription = null) }
        Column(modifier = Modifier.padding(horizontal = 16.dp).weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold); if (subtitle.isNotEmpty()) Text(subtitle, color = Color.Gray, style = MaterialTheme.typography.bodySmall)
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
    }
}
