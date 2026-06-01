package com.example.studentregistration.utils

object ValidationUtils {

    fun isValidNim(nim: String): Boolean {
        return nim.isNotBlank() && nim.length in 8..12 && nim.all { it.isDigit() }
    }

    fun isValidName(name: String): Boolean {
        return name.isNotBlank() && name.length >= 3
    }

    fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPhone(phone: String): Boolean {
        val cleanPhone = phone.replace("[\\s-]".toRegex(), "")
        return cleanPhone.isNotBlank() && cleanPhone.length in 10..13 &&
                (cleanPhone.startsWith("08") || cleanPhone.startsWith("+62"))
    }

    fun isValidSemester(semester: String): Boolean {
        val sem = semester.toIntOrNull() ?: return false
        return sem in 1..14
    }
}