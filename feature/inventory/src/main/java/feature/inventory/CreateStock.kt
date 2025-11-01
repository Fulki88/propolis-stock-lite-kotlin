package feature.inventory

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.firobusiness.propolisstocklite.core.common.Ids
import id.firobusiness.propolisstocklite.core.data.ProductEntity
import id.firobusiness.propolisstocklite.core.data.Repository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateStockViewModel @Inject constructor(
    private val repo: Repository
) : ViewModel() {
    
    fun createProduct(
        sku: String,
        name: String,
        variant: String?,
        unitPrice: Long,
        unitCost: Long?,
        stockQty: Int,
        minStock: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val product = ProductEntity(
                    id = Ids.newId(),
                    sku = sku,
                    name = name,
                    variant = variant.takeIf { it?.isNotBlank() == true },
                    unitPrice = unitPrice,
                    unitCost = unitCost,
                    stockQty = stockQty,
                    minStock = minStock
                )
                repo.addProduct(product)
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Unknown error")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateStockRoute(onBack: () -> Unit) {
    val vm: CreateStockViewModel = hiltViewModel()
    var sku by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var variant by remember { mutableStateOf("") }
    var unitPrice by remember { mutableStateOf("") }
    var unitCost by remember { mutableStateOf("") }
    var stockQty by remember { mutableStateOf("") }
    var minStock by remember { mutableStateOf("") }
    var showSuccess by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create New Stock") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Back") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = sku,
                onValueChange = { sku = it },
                label = { Text("SKU") },
                placeholder = { Text("e.g., BP-GOLD-10") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Product Name") },
                placeholder = { Text("e.g., British Propolis Gold") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = variant,
                onValueChange = { variant = it },
                label = { Text("Variant (Optional)") },
                placeholder = { Text("e.g., 10ml") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = unitPrice,
                onValueChange = { unitPrice = it },
                label = { Text("Unit Price (Rp)") },
                placeholder = { Text("e.g., 250000") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = unitCost,
                onValueChange = { unitCost = it },
                label = { Text("Unit Cost (Optional, Rp)") },
                placeholder = { Text("e.g., 150000") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = stockQty,
                onValueChange = { stockQty = it },
                label = { Text("Stock Quantity") },
                placeholder = { Text("e.g., 50") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = minStock,
                onValueChange = { minStock = it },
                label = { Text("Minimum Stock") },
                placeholder = { Text("e.g., 5") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            
            Button(
                onClick = {
                    if (sku.isBlank() || name.isBlank() || unitPrice.isBlank() || stockQty.isBlank() || minStock.isBlank()) {
                        showError = "Please fill all required fields"
                        return@Button
                    }
                    
                    try {
                        vm.createProduct(
                            sku = sku,
                            name = name,
                            variant = variant,
                            unitPrice = unitPrice.toLong(),
                            unitCost = unitCost.takeIf { it.isNotBlank() }?.toLong(),
                            stockQty = stockQty.toInt(),
                            minStock = minStock.toInt(),
                            onSuccess = {
                                showSuccess = true
                                // Clear form
                                sku = ""
                                name = ""
                                variant = ""
                                unitPrice = ""
                                unitCost = ""
                                stockQty = ""
                                minStock = ""
                            },
                            onError = { showError = it }
                        )
                    } catch (e: NumberFormatException) {
                        showError = "Please enter valid numbers"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create Product")
            }
        }
    }
    
    // Success dialog
    if (showSuccess) {
        AlertDialog(
            onDismissRequest = { showSuccess = false },
            title = { Text("Success") },
            text = { Text("Product created successfully!") },
            confirmButton = {
                TextButton(onClick = { showSuccess = false }) {
                    Text("OK")
                }
            }
        )
    }
    
    // Error dialog
    showError?.let { error ->
        AlertDialog(
            onDismissRequest = { showError = null },
            title = { Text("Error") },
            text = { Text(error) },
            confirmButton = {
                TextButton(onClick = { showError = null }) {
                    Text("OK")
                }
            }
        )
    }
}