package com.example.practica03

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        var btnLogin = findViewById<Button>(R.id.Login)
        var email = findViewById<EditText>(R.id.Email)
        var password = findViewById<EditText>(R.id.Password)


        btnLogin.setOnClickListener {
            auth.signInWithEmailAndPassword("mario@mario.com","123456").addOnCompleteListener{
                task ->

                if (task.isSuccessful)
                {
                    Toast.makeText(this,"Se inicio sesion correctamente", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this,MenuPrincipal::class.java))
                }
                else{
                    Toast.makeText(this,"Error"+task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                }
            }

        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser!= null)
        {
            Toast.makeText(this,"No hay usuarios autenticados", Toast.LENGTH_LONG).show()

        }
        else
        {
            Toast.makeText(this,"Ya estas autenticado", Toast.LENGTH_LONG).show()
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        auth.signOut()
    }
}