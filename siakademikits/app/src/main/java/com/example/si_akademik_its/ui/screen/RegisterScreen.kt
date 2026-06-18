package com.example.si_akademik_its.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.si_akademik_its.ui.theme.*

@Composable
fun RegisterScreen(onRegisterSuccess: () -> Unit, onNavigateToLogin: () -> Unit) {
    var nama by remember { mutableStateOf("") }
    var nrp by remember { mutableStateOf("") }
    
    Column(modifier = Modifier.fillMaxSize().background(ItsSurface).verticalScroll(rememberScrollState())) {
        Box(modifier = Modifier.fillMaxWidth().height(200.dp).background(Brush.verticalGradient(listOf(ItsBiruTua, ItsBiruCover))), contentAlignment = Alignment.Center) {
            Text("Pendaftaran Akun", color = ItsWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        Column(modifier = Modifier.padding(24.dp)) {
            OutlinedTextField(value = nama, onValueChange = { nama = it }, label = { Text("Nama Lengkap") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(value = nrp, onValueChange = { nrp = it }, label = { Text("NRP") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onRegisterSuccess, modifier = Modifier.fillMaxWidth().height(50.dp), colors = ButtonDefaults.buttonColors(containerColor = ItsBiruTua), shape = RoundedCornerShape(12.dp)) {
                Text("Daftar")
            }
            TextButton(onClick = onNavigateToLogin, modifier = Modifier.fillMaxWidth()) {
                Text("Sudah punya akun? Masuk", color = ItsBiruTuaLambang)
            }
        }
    }
}
