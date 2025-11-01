package feature.sales

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import id.firobusiness.propolisstocklite.core.data.ProductEntity
import id.firobusiness.propolisstocklite.core.data.Repository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import androidx.hilt.navigation.compose.hiltViewModel

@HiltViewModel
class SalesViewModel @Inject constructor(
    private val repo: Repository
) : ViewModel() {
    
    val products = repo.getProductsFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}

@Composable
fun SalesRoute(
    vm: SalesViewModel = hiltViewModel()
) {
    val products by vm.products.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Buyer Purchase System",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(products) { product ->
                ProductCard(product = product)
            }
        }
    }
}

@Composable
fun ProductCard(product: ProductEntity) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Price: $${product.unitPrice}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Stock: ${product.stockQty}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
