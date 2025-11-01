package feature.inventory

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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import id.firobusiness.propolisstocklite.core.designsystem.AppTopBar
import androidx.hilt.navigation.compose.hiltViewModel

@HiltViewModel
class InventoryVm @Inject constructor(
    private val repo: Repository
): ViewModel() {
    val uiState = repo.inventory()
        .map { InventoryState(items = it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), InventoryState(emptyList()))

    fun seedIfEmpty(seed: List<ProductEntity>) {
        viewModelScope.launch { repo.seedIfEmpty(seed) }
    }
}

data class InventoryState(val items: List<ProductEntity>)

@Composable
fun InventoryRoute(onNewSale: () -> Unit) {
    val vm: InventoryVm = hiltViewModel()
    val state by vm.uiState.collectAsState()

    // Seed from assets on first run
    SeedFromAssetsIfNeeded(vm)

    Scaffold(
        topBar = { AppTopBar("Inventory") },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = onNewSale, text = { Text("New Sale") })
        }
    ) { padding ->
        LazyColumn(Modifier.padding(padding).padding(16.dp)) {
            items(state.items) { item ->
                InventoryRow(item)
            }
        }
    }
}

@Composable
private fun InventoryRow(p: ProductEntity) {
    ElevatedCard(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Column(Modifier.padding(16.dp)) {
            Text("${p.name} (${p.sku})", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text("Variant: ${p.variant ?: "-"} • Price: Rp ${p.unitPrice}")
            Text("Stock: ${p.stockQty} (Min: ${p.minStock})")
        }
    }
}

@Composable
private fun SeedFromAssetsIfNeeded(vm: InventoryVm) {
    val context = androidx.compose.ui.platform.LocalContext.current
    LaunchedEffect(Unit) {
        val seedStr = context.assets.open("seed.json").bufferedReader().use { it.readText() }
        val seed = kotlinx.serialization.json.Json.decodeFromString(Seed.serializer(), seedStr)
        vm.seedIfEmpty(seed.products.map {
            ProductEntity(
                id = it.id, sku = it.sku, name = it.name, variant = it.variant,
                unitPrice = it.unitPrice, unitCost = it.unitCost, stockQty = it.stockQty,
                minStock = it.minStock, isActive = it.isActive
            )
        })
    }
}

@kotlinx.serialization.Serializable
data class Seed(val products: List<SeedProduct>)
@kotlinx.serialization.Serializable
data class SeedProduct(
    val id: String,
    val sku: String,
    val name: String,
    val variant: String?,
    val unitPrice: Long,
    val unitCost: Long?,
    val stockQty: Int,
    val minStock: Int,
    val isActive: Boolean
)
