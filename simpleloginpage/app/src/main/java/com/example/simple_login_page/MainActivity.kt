package com.example.simple_login_page

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Facebook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LoginScreen()
        }
    }
}

@Composable
fun LoginScreen() {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Color(0xFF0D1B4C),
                        Color(0xFF1B2C6B)
                    )
                )
            )
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.78f),

                shape = RoundedCornerShape(
                    topStart = 40.dp,
                    topEnd = 40.dp
                ),

                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(28.dp),

                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Sign In",
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B4D9B)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Welcome back you've been missed!",
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                        },

                        modifier = Modifier.fillMaxWidth(),

                        label = {
                            Text("Email")
                        },

                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                        },

                        modifier = Modifier.fillMaxWidth(),

                        label = {
                            Text("Password")
                        },

                        visualTransformation =
                            PasswordVisualTransformation(),

                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Forgot Password?",
                        color = Color(0xFF1B4D9B),

                        modifier = Modifier
                            .align(Alignment.End)
                            .clickable { }
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    Button(
                        onClick = { },

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp),

                        shape = RoundedCornerShape(30.dp),

                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2956A3)
                        )
                    ) {

                        Text(
                            text = "Login",
                            fontSize = 18.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Don't have an account?"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Register",
                        color = Color(0xFF2956A3),
                        fontWeight = FontWeight.Bold,

                        modifier = Modifier.clickable { }
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    Text(
                        text = "Or sign in with",
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        horizontalArrangement =
                            Arrangement.spacedBy(24.dp)
                    ) {

                        SocialButton(
                            text = "G",
                            color = Color(0xFFDB4437)
                        )

                        SocialButton(
                            text = "f",
                            color = Color(0xFF1877F2)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SocialButton(
    text: String,
    color: Color
) {

    Box(
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
            .background(Color.White),

        contentAlignment = Alignment.Center
    ) {

        Text(
            text = text,
            color = color,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
    }
}