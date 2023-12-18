package nz.eloque.nextcloud_shopping

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import nz.eloque.nextcloud_shopping.ui.ShoppingApp
import nz.eloque.nextcloud_shopping.ui.theme.ShoppingTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShoppingTheme {
                ShoppingApp()
            }
        }
    }
}
