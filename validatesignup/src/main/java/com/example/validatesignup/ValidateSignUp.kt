package com.example.validatesignup

object ValidateSignUp {
    fun isEmpty(email: String, password: String, confirmPassword: String): Boolean {
        return email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()
    }
}