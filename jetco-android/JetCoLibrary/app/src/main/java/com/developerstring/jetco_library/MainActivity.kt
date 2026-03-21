package com.developerstring.jetco_library

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.ui.graphics.Color
import com.developerstring.jetco_library.ui.theme.JetCoLibraryTheme

val UIBlue = Color(0xFF1E90FF)
val LightBlue = Color(0xFFB5DAFF)
val LightestPink = Color(0xFFF7F1FF)

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JetCoLibraryTheme {
                Scaffold(
                    floatingActionButton = {
                        MorphFloatingActionButtonPreview()
                    },
                    floatingActionButtonPosition = FabPosition.End
                ) {}
            }
        }
    }
}
