package com.example.movies.View

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.movies.MainActivity
import com.example.movies.R
import com.example.movies.databinding.ActivitySignUpBinding
import com.example.validatesignup.ValidateSignUp.isEmpty
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
class SignUp : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth
        setUpListener()

    }

    private fun setUpListener() {
        binding.txtSignIn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnRegister.setOnClickListener(View.OnClickListener {

            val email = binding.txtEmail.text.toString().trim()
            val password = binding.txtPassword.text.toString()
            val confirmPassword = binding.txtPassword2.text.toString()

            if (isEmpty(email, password, confirmPassword)) {
                Toast.makeText(
                    baseContext, getString(R.string.all_fields_required),
                    Toast.LENGTH_SHORT
                ).show()

                return@OnClickListener
            }

            if(password.length < 6) {
                Toast.makeText(
                    baseContext, getString(R.string.weak_password),
                    Toast.LENGTH_SHORT
                ).show()

                return@OnClickListener
            }
            if (password != confirmPassword) {
                Toast.makeText(
                    baseContext, getString(R.string.match_password),
                    Toast.LENGTH_SHORT
                ).show()

                return@OnClickListener
            }
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        Log.d(TAG, user.toString())
                        Toast.makeText(
                            baseContext, getString(R.string.user_registered),
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext, getString(R.string.user_register_fail),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        })
    }
}