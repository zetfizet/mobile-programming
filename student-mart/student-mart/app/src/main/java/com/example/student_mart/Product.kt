package com.example.student_mart

import androidx.annotation.DrawableRes

data class Product(
    val name: String,
    val price: String,
    val description: String,
    val emoji: String,
    val category: String = "General",
    @DrawableRes val imageRes: Int? = null,
    val rating: Float = 4.5f,
    val reviewCount: Int = 120,
    val sellerName: String = "Student Store Official",
    val sellerImage: String = "🎓",
    val specifications: Map<String, String> = mapOf(
        "Condition" to "New",
        "Stock" to "Available",
        "Shipping" to "Surabaya"
    )
)