package com.example.si_akademik_its.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.si_akademik_its.ui.theme.*

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, onNavigateToRegister: () -> Unit) {
    var nrp by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().background(ItsSurface).verticalScroll(rememberScrollState())) {
        Box(modifier = Modifier.fillMaxWidth().height(250.dp).background(Brush.verticalGradient(listOf(ItsBiruTua, ItsBiruCover)))) {
            Box(modifier = Modifier.fillMaxWidth().height(4.dp).background(ItsKuningTua))
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                Box(modifier = Modifier.size(72.dp).clip(RoundedCornerShape(18.dp)).background(ItsKuningTua), contentAlignment = Alignment.Center) {
                    Text("ITS", color = ItsBiruTua, fontWeight = FontWeight.Black, fontSize = 24.sp)
                }
                Text("myITS Academic", color = ItsWhite, fontSize = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 16.dp))
            }
        }
        
        Box(modifier = Modifier.fillMaxWidth().padding(20.dp).offset(y = (-24).dp)) {
            Card(shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = ItsWhite), elevation = CardDefaults.cardElevation(8.dp)) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("Masuk", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(20.dp))
                    OutlinedTextField(value = nrp, onValueChange = { nrp = it }, label = { Text("NRP") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = password, onValueChange = { password = it }, 
                        label = { Text("Password") }, 
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(22.dp))
                    Button(onClick = onLoginSuccess, modifier = Modifier.fillMaxWidth().height(50.dp), colors = ButtonDefaults.buttonColors(containerColor = ItsBiruTua), shape = RoundedCornerShape(12.dp)) {
                        Text("Masuk", fontWeight = FontWeight.Bold)
                    }
                    TextButton(onClick = onNavigateToRegister, modifier = Modifier.fillMaxWidth()) {
                        Text("Belum punya akun? Daftar", color = ItsBiruTuaLambang)
                    }
                }
            }
        }
    }
}
