package com.example.si_akademik_its

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.si_akademik_its.ui.navigation.NavGraph
import com.example.si_akademik_its.ui.theme.SiakademikitsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SiakademikitsTheme {
                NavGraph()
            }
        }
    }
}
