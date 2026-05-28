package com.example.calculator_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    CalculatorScreen()
                }
            }
        }
    }
}

@Composable
fun CalculatorScreen() {

    var input by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("0") }

    val buttons = listOf(
        listOf("7", "8", "9", "÷"),
        listOf("4", "5", "6", "×"),
        listOf("1", "2", "3", "-"),
        listOf("C", "0", "=", "+")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101010))
            .padding(16.dp),
        verticalArrangement = Arrangement.Bottom
    ) {

        // DISPLAY
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.End
        ) {

            Text(
                text = input,
                fontSize = 32.sp,
                color = Color.LightGray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = result,
                fontSize = 48.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        // BUTTONS
        buttons.forEach { row ->

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                row.forEach { symbol ->

                    Button(
                        onClick = {

                            when (symbol) {

                                "C" -> {
                                    input = ""
                                    result = "0"
                                }

                                "=" -> {
                                    result = calculateResult(input)
                                }

                                else -> {
                                    input += symbol
                                }
                            }
                        },

                        modifier = Modifier
                            .weight(1f)
                            .height(80.dp),

                        shape = RoundedCornerShape(20.dp),

                        colors = ButtonDefaults.buttonColors(
                            containerColor =
                                if (symbol in listOf("÷", "×", "-", "+", "="))
                                    Color(0xFFFF9800)
                                else
                                    Color(0xFF2A2A2A)
                        )
                    ) {

                        Text(
                            text = symbol,
                            fontSize = 28.sp,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

fun calculateResult(expression: String): String {

    return try {

        val cleanedExpression = expression
            .replace("×", "*")
            .replace("÷", "/")

        val result = object {

            var index = -1
            var ch = 0

            fun nextChar() {
                ch =
                    if (++index < cleanedExpression.length)
                        cleanedExpression[index].code
                    else -1
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                return x
            }

            fun parseExpression(): Double {

                var x = parseTerm()

                while (true) {

                    when (ch.toChar()) {

                        '+' -> {
                            nextChar()
                            x += parseTerm()
                        }

                        '-' -> {
                            nextChar()
                            x -= parseTerm()
                        }

                        else -> return x
                    }
                }
            }

            fun parseTerm(): Double {

                var x = parseFactor()

                while (true) {

                    when (ch.toChar()) {

                        '*' -> {
                            nextChar()
                            x *= parseFactor()
                        }

                        '/' -> {
                            nextChar()
                            x /= parseFactor()
                        }

                        else -> return x
                    }
                }
            }

            fun parseFactor(): Double {

                val startIndex = index

                while (
                    ch in '0'.code..'9'.code ||
                    ch == '.'.code
                ) {
                    nextChar()
                }

                return cleanedExpression
                    .substring(startIndex, index)
                    .toDouble()
            }
        }.parse()

        result.toString()

    } catch (e: Exception) {
        "Error"
    }
}