package feature.inventory

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import feature.inventory.data.StockItem
import feature.inventory.data.StockRepository
import feature.inventory.data.PagingResult
import id.firobusiness.propolisstocklite.core.designsystem.AppTopBar
import id.firobusiness.propolisstocklite.core.common.AppStrings
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import id.firobusiness.propolisstocklite.core.common.AppColors

@HiltViewModel
class InventoryVm @Inject constructor(
    private val stockRepository: StockRepository
): ViewModel() {
    
    private val _uiState = MutableStateFlow(InventoryState())
    val uiState: StateFlow<InventoryState> = _uiState.asStateFlow()
    
    private var currentPage = 1
    private val pageSize = 50
    
    init {
        loadStocks()
    }
    
    fun loadStocks(refresh: Boolean = false) {
        if (refresh) {
            currentPage = 1
            _uiState.value = InventoryState()
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                val result = stockRepository.getStocks(currentPage, pageSize)
                result.fold(
                    onSuccess = { stockResult ->
                        val newItems = if (refresh) {
                            stockResult.stocks
                        } else {
                            _uiState.value.items + stockResult.stocks
                        }
                        
                        _uiState.value = _uiState.value.copy(
                            items = newItems,
                            isLoading = false,
                            hasMore = stockResult.stocks.size == pageSize,
                            total = stockResult.total,
                            error = null
                        )
                        currentPage++
                    },
                    onFailure = { error ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = error.message ?: "Unknown error"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error"
                )
            }
        }
    }
    
    fun loadMore() {
        if (!_uiState.value.isLoading && _uiState.value.hasMore) {
            loadStocks()
        }
    }
    
    fun toggleSaleMode() {
        _uiState.value = _uiState.value.copy(
            isInSaleMode = !_uiState.value.isInSaleMode,
            cartItems = if (_uiState.value.isInSaleMode) emptyMap() else _uiState.value.cartItems
        )
    }
    
    fun addToCart(stockItem: StockItem) {
        val currentCart = _uiState.value.cartItems.toMutableMap()
        val existingItem = currentCart[stockItem.name]
        
        if (existingItem != null) {
            currentCart[stockItem.name] = existingItem.copy(quantity = existingItem.quantity + 1)
        } else {
            currentCart[stockItem.name] = CartItem(stockItem, 1)
        }
        
        _uiState.value = _uiState.value.copy(cartItems = currentCart)
    }
    
    fun removeFromCart(stockItem: StockItem) {
        val currentCart = _uiState.value.cartItems.toMutableMap()
        val existingItem = currentCart[stockItem.name]
        
        if (existingItem != null && existingItem.quantity > 1) {
            currentCart[stockItem.name] = existingItem.copy(quantity = existingItem.quantity - 1)
        } else {
            currentCart.remove(stockItem.name)
        }
        
        _uiState.value = _uiState.value.copy(cartItems = currentCart)
    }
    
    fun setQuantity(stockItem: StockItem, quantity: Int) {
        val currentCart = _uiState.value.cartItems.toMutableMap()
        
        if (quantity > 0) {
            currentCart[stockItem.name] = CartItem(stockItem, quantity)
        } else {
            currentCart.remove(stockItem.name)
        }
        
        _uiState.value = _uiState.value.copy(cartItems = currentCart)
    }
    
    fun getTotalItems(): Int {
        return _uiState.value.cartItems.values.sumOf { it.quantity }
    }
    
    fun getTotalPrice(): Double {
        return _uiState.value.cartItems.values.sumOf { it.stockItem.price * it.quantity }.toDouble()
    }
}

data class CartItem(
    val stockItem: StockItem,
    val quantity: Int = 0
)

data class InventoryState(
    val items: List<StockItem> = emptyList(),
    val cartItems: Map<String, CartItem> = emptyMap(),
    val isLoading: Boolean = false,
    val hasMore: Boolean = true,
    val total: Int = 0,
    val error: String? = null,
    val isInSaleMode: Boolean = false
)

@Composable
fun InventoryRoute(onNewSale: () -> Unit) {
    val vm: InventoryVm = hiltViewModel()
    val state by vm.uiState.collectAsState()
    val listState = rememberLazyListState()

    // Infinite scroll detection
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null && 
                    lastVisibleIndex >= state.items.size - 5 && 
                    state.hasMore && 
                    !state.isLoading) {
                    vm.loadMore()
                }
            }
    }

    Scaffold(
        topBar = { 
            AppTopBar(
                title = AppStrings.INVENTORY_TITLE,
                actions = {
                    IconButton(onClick = { vm.loadStocks(refresh = true) }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = AppStrings.REFRESH
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if (!state.isInSaleMode) {
                ExtendedFloatingActionButton(
                    onClick = { vm.toggleSaleMode() }
                ) {
                    Text(AppStrings.NEW_SALE)
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // Header with total count or cart info
            if (state.isInSaleMode && state.cartItems.isNotEmpty()) {
                // Cart summary header
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .border(2.dp, AppColors.BorderGreen, RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(
                        containerColor = AppColors.BackgroundGreen
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = String.format(AppStrings.TOTAL_ITEMS_CART, vm.getTotalItems()),
                            style = MaterialTheme.typography.titleMedium,
                            color = AppColors.TextGreenDark,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = String.format(AppStrings.TOTAL_PRICE, vm.getTotalPrice()),
                            style = MaterialTheme.typography.titleMedium,
                            color = AppColors.TextGreenDark,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            } else if (!state.isInSaleMode && state.total > 0) {
                // Regular inventory header
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = if (state.isInSaleMode) AppStrings.NEW_SALE else String.format(AppStrings.TOTAL_ITEMS, state.total),
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            } else if (state.isInSaleMode) {
                // New Sale header when cart is empty
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = AppStrings.NEW_SALE,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            
            // Content
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.items) { item ->
                        InventoryRow(
                            item = item,
                            isInSaleMode = state.isInSaleMode,
                            cartQuantity = state.cartItems[item.name]?.quantity ?: 0,
                            onAddToCart = { vm.addToCart(item) },
                            onRemoveFromCart = { vm.removeFromCart(item) },
                            onSetQuantity = { quantity -> vm.setQuantity(item, quantity) }
                        )
                    }
                    
                    // Loading indicator at bottom
                    if (state.isLoading && state.items.isNotEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        strokeWidth = 2.dp
                                    )
                                    Text(
                                        text = AppStrings.LOAD_MORE,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                    
                    // No more items indicator
                    if (!state.hasMore && state.items.isNotEmpty()) {
                        item {
                            Text(
                                text = AppStrings.NO_MORE_ITEMS,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                
                // Initial loading state
                if (state.isLoading && state.items.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            CircularProgressIndicator()
                            Text(
                                text = AppStrings.LOADING,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
                
                // Error state
                if (state.error != null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = state.error ?: AppStrings.UNKNOWN_ERROR,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Center
                            )
                            Button(onClick = { vm.loadStocks(refresh = true) }) {
                                Text(AppStrings.RETRY)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InventoryRow(
    item: StockItem,
    isInSaleMode: Boolean,
    cartQuantity: Int,
    onAddToCart: () -> Unit,
    onRemoveFromCart: () -> Unit,
    onSetQuantity: (Int) -> Unit
) {
    var showQuantityDialog by remember { mutableStateOf(false) }
    
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = String.format(AppStrings.PRICE_FORMAT_RUPIAH.replace("%,d", "%,.0f"), item.price.toDouble()),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                
                if (isInSaleMode) {
                    // Quantity controls for sale mode
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Minus button
                        IconButton(
                            onClick = onRemoveFromCart,
                            enabled = cartQuantity > 0,
                            modifier = Modifier
                                .size(32.dp)
                                .background(
                                    if (cartQuantity > 0) MaterialTheme.colorScheme.error else Color.Gray,
                                    CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = AppStrings.REMOVE_ITEM,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        
                        // Quantity display (clickable)
                        Text(
                            text = cartQuantity.toString(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (cartQuantity > 0) AppColors.TextGreenDark else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .clickable { showQuantityDialog = true }
                                .background(
                                    if (cartQuantity > 0) AppColors.BackgroundGreen else Color.Transparent,
                                    RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                                .widthIn(min = 32.dp),
                            textAlign = TextAlign.Center
                        )
                        
                        // Plus button
                        IconButton(
                            onClick = onAddToCart,
                            modifier = Modifier
                                .size(32.dp)
                                .background(AppColors.SuccessGreen, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = AppStrings.ADD_ITEM,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                } else {
                    // Available stock indicator for normal mode
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = AppColors.BackgroundGreen
                        )
                    ) {
                        Text(
                            text = AppStrings.AVAILABLE_STOCK,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = AppColors.AvailableGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
    
    // Quantity input dialog
    if (showQuantityDialog) {
        QuantityDialog(
            currentQuantity = cartQuantity,
            onQuantitySet = { newQuantity ->
                onSetQuantity(newQuantity)
                showQuantityDialog = false
            },
            onDismiss = { showQuantityDialog = false }
        )
    }
}

@Composable
private fun QuantityDialog(
    currentQuantity: Int,
    onQuantitySet: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var quantity by remember { mutableStateOf(currentQuantity.toString()) }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = AppStrings.QUANTITY_DIALOG_TITLE,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { newValue ->
                        // Only allow digits
                        if (newValue.all { it.isDigit() } || newValue.isEmpty()) {
                            quantity = newValue
                        }
                    },
                    label = { Text(AppStrings.ENTER_QUANTITY) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(AppStrings.CANCEL)
                    }
                    
                    Button(
                        onClick = {
                            val newQuantity = quantity.toIntOrNull() ?: 0
                            onQuantitySet(newQuantity)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(AppStrings.CONFIRM)
                    }
                }
            }
        }
    }
}