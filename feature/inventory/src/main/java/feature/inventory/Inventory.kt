package feature.inventory

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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

@HiltViewModel
class InventoryVm @Inject constructor(
    private val stockRepository: StockRepository
): ViewModel() {
    
    private val _uiState = MutableStateFlow(InventoryState())
    val uiState: StateFlow<InventoryState> = _uiState.asStateFlow()
    
    private var currentPage = 1
    private val pageSize = 30
    
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
}

data class InventoryState(
    val items: List<StockItem> = emptyList(),
    val isLoading: Boolean = false,
    val hasMore: Boolean = true,
    val total: Int = 0,
    val error: String? = null
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
            ExtendedFloatingActionButton(
                onClick = onNewSale
            ) {
                Text(AppStrings.NEW_SALE)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // Header with total count
            if (state.total > 0) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = String.format(AppStrings.TOTAL_ITEMS, state.total),
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
                        InventoryRow(item)
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
private fun InventoryRow(item: StockItem) {
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
                
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Text(
                        text = AppStrings.AVAILABLE_STOCK,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
    }
}

// Removed seed functionality as we're now using API data
