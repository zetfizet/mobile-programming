package com.example.diceroller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiceApp()
        }
    }
}

@Composable
fun DiceApp() {
    var dice by remember { mutableStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F4F4))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Dice Roller 🎲", fontSize = 28.sp)

        Spacer(modifier = Modifier.height(40.dp))

        DiceCube3D(number = dice)

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = { dice = Random.nextInt(1, 7) },
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Roll Dice")
        }
    }
}

@Composable
fun DiceCube3D(number: Int) {
    Box(
        modifier = Modifier.size(220.dp),
        contentAlignment = Alignment.Center
    ) {

        Canvas(modifier = Modifier.size(180.dp)) {

            val size = size.minDimension
            val depth = size * 0.25f

            // FRONT FACE COORDINATES
            val frontTopLeft = Offset(0f, depth)
            val frontTopRight = Offset(size - depth, depth)
            val frontBottomLeft = Offset(0f, size)
            val frontBottomRight = Offset(size - depth, size)

            // === TOP FACE ===
            val topPath = Path().apply {
                moveTo(frontTopLeft.x, frontTopLeft.y)
                lineTo(frontTopRight.x, frontTopRight.y)
                lineTo(size, 0f)
                lineTo(depth, 0f)
                close()
            }

            drawPath(
                path = topPath,
                color = Color(0xFFEDEDED),
                style = Fill
            )

            // === SIDE FACE ===
            val sidePath = Path().apply {
                moveTo(frontTopRight.x, frontTopRight.y)
                lineTo(size, 0f)
                lineTo(size, size - depth)
                lineTo(frontBottomRight.x, frontBottomRight.y)
                close()
            }

            drawPath(
                path = sidePath,
                color = Color(0xFFD6D6D6),
                style = Fill
            )

            // === FRONT FACE ===
            drawRect(
                color = Color.White,
                topLeft = frontTopLeft,
                size = androidx.compose.ui.geometry.Size(size - depth, size - depth)
            )

            // === DOTS ===
            val dotRadius = 10f

            val centerX = (size - depth) / 2
            val centerY = depth + (size - depth) / 2

            val left = (size - depth) * 0.25f
            val right = (size - depth) * 0.75f
            val top = depth + (size - depth) * 0.25f
            val bottom = depth + (size - depth) * 0.75f

            fun drawDot(x: Float, y: Float) {
                drawCircle(Color.Black, dotRadius, Offset(x, y))
            }

            when (number) {
                1 -> drawDot(centerX, centerY)
                2 -> {
                    drawDot(left, top)
                    drawDot(right, bottom)
                }
                3 -> {
                    drawDot(left, top)
                    drawDot(centerX, centerY)
                    drawDot(right, bottom)
                }
                4 -> {
                    drawDot(left, top)
                    drawDot(right, top)
                    drawDot(left, bottom)
                    drawDot(right, bottom)
                }
                5 -> {
                    drawDot(left, top)
                    drawDot(right, top)
                    drawDot(centerX, centerY)
                    drawDot(left, bottom)
                    drawDot(right, bottom)
                }
                6 -> {
                    drawDot(left, top)
                    drawDot(left, centerY)
                    drawDot(left, bottom)
                    drawDot(right, top)
                    drawDot(right, centerY)
                    drawDot(right, bottom)
                }
            }
        }
    }
}