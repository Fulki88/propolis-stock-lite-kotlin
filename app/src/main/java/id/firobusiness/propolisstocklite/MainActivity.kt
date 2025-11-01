package id.firobusiness.propolisstocklite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import dagger.hilt.android.AndroidEntryPoint
import id.firobusiness.propolisstocklite.ui.theme.AppTheme
import id.firobusiness.propolisstocklite.navigation.AppNavGraph

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface {
                    AppNavGraph()
                }
            }
        }
    }
}
