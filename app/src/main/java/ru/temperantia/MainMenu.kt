package ru.temperantia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ru.temperantia.ui.theme.DeepDarkGreen
import ru.temperantia.ui.theme.TemperantiaTheme

class MainMenu : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TemperantiaTheme {
                MainMenuCard()
            }
        }
    }
}

@Composable
fun MainMenuCard(modifier: Modifier = Modifier) {
    Surface (
        color = DeepDarkGreen,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Main Menu", color = Color.White)
    }
}