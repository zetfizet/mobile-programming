package com.example.student_mart

val productList = listOf(
    Product(
        "PlayStation 5",
        "Rp7.500.000",
        "The latest Sony console with ultra-high speed SSD, deeper immersion with support for haptic feedback, adaptive triggers, and 3D Audio, and an all-new generation of incredible PlayStation games.",
        "🎮",
        "Electronics",
        imageRes = R.drawable.ps_5,
        rating = 4.9f,
        reviewCount = 850,
        sellerName = "Sony Official Store",
        sellerImage = "🏢",
        specifications = mapOf(
            "CPU" to "x86-64-AMD Ryzen Zen 2",
            "GPU" to "AMD Radeon RDNA 2-based",
            "Memory" to "GDDR6 16GB",
            "Storage" to "825GB SSD",
            "Video Out" to "Support of 4K 120Hz TVs"
        )
    ),
    Product(
        "Wireless Mouse",
        "Rp90.000",
        "Fantech Wireless Mouse. Lightweight design, long battery life, and ergonomic shape perfect for long study sessions or gaming.",
        "🖱️",
        "Electronics",
        imageRes = R.drawable.mouse_fantech,
        rating = 4.7f,
        reviewCount = 1200,
        sellerName = "Fantech Gear Indonesia",
        sellerImage = "🖱️",
        specifications = mapOf(
            "Connectivity" to "2.4GHz Wireless",
            "DPI" to "1600 DPI",
            "Battery" to "1x AA Battery",
            "Sensor" to "Optical"
        )
    ),
    Product(
        "Mechanical Keyboard",
        "Rp250.000",
        "RGB gaming keyboard with blue switches. Tactile and clicky, providing the best typing experience for coding and assignments.",
        "⌨️",
        "Electronics",
        imageRes = R.drawable.keyboard,
        rating = 4.8f,
        reviewCount = 430,
        sellerName = "Tech Gadget Hub",
        specifications = mapOf(
            "Switch" to "Blue Switch",
            "Layout" to "TKL (80%)",
            "Backlight" to "RGB 16.8M Colors"
        )
    ),
    Product(
        "Calculus Book",
        "Rp45.000",
        "Second-hand Calculus: Early Transcendentals. Good condition, no missing pages, only a few highlights.",
        "📘",
        "Books",
        imageRes = R.drawable.buku_kalkulus,
        rating = 4.5f,
        reviewCount = 15,
        sellerName = "Senior Bookstore",
        sellerImage = "👨‍🎓",
        specifications = mapOf(
            "Edition" to "8th Edition",
            "Author" to "James Stewart",
            "Language" to "English"
        )
    )
)