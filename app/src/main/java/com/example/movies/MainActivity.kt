package com.example.movies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.movies.View.Movies
import com.example.movies.View.SignUp
import com.example.movies.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth

        setUpListener()
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, Movies::class.java)
            startActivity(intent)
        }
    }

    private fun setUpListener() {
        binding.txtSignUp.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        binding.btnEntrar.setOnClickListener(View.OnClickListener {

            val email = binding.txtEmail.text.toString()
            val password = binding.txtPassword.text.toString()

            if (password.isEmpty() || email.isEmpty()) {
                Toast.makeText(
                    baseContext, getString(R.string.fields_for_login),
                    Toast.LENGTH_SHORT
                ).show()

                return@OnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        auth.currentUser
                        Toast.makeText(
                            baseContext, getString(R.string.login_done),
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this, Movies::class.java)
                        startActivity(intent)
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(
                            baseContext, getString(R.string.login_failed),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        })
    }
}
