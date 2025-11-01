package id.firobusiness.propolisstocklite.core.designsystem

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun AppTopBar(title: String) {
    TopAppBar(title = { Text(title) })
}
