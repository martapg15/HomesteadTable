package dam_a51564.homesteadtable

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dam_a51564.homesteadtable.navigation.AppNavigation
import dam_a51564.homesteadtable.ui.theme.HomesteadTableTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HomesteadTableTheme {
                HomesteadTableTheme {
                    AppNavigation()
                }
            }
        }
    }
}