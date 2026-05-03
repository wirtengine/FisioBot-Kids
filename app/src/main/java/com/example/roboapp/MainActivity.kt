package com.example.roboapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.roboapp.ui.theme.navigation.AppNavGraph
import com.example.roboapp.ui.theme.FisioBotTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FisioBotTheme {
                AppNavGraph()
            }
        }
    }
}
