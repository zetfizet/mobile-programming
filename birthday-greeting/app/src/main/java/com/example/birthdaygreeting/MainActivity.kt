package com.example.birthdaygreeting

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etName = findViewById<EditText>(R.id.etName)
        val btnWish = findViewById<Button>(R.id.btnWish)
        val tvResult = findViewById<TextView>(R.id.tvResult)

        btnWish.setOnClickListener {
            val name = etName.text.toString().trim()

            tvResult.text = if (name.isEmpty()) {
                "Please enter your name"
            } else {
                "Happy Birthday, $name 🎉"
            }
        }
    }
}