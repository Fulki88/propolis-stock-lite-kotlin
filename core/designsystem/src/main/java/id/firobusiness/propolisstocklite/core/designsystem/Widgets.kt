package id.firobusiness.propolisstocklite.core.designsystem

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(title: String) {
    TopAppBar(title = { Text(title) })
}
