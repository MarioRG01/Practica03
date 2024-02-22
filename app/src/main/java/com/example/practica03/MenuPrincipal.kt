package com.example.practica03

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ListView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class MenuPrincipal : AppCompatActivity() {

    lateinit var pelicuas: ArrayList<Pelicula>
    private lateinit var auth: FirebaseAuth
    val database = Firebase.database
    val myRef = database.getReference("Peliculas")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_principal)

        auth = Firebase.auth
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)


        val menuHost: MenuHost = this

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu, menu)
            }



            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId){
                    R.id.salir ->{
                        auth.signOut()
                        finish()
                        true
                    }
                    R.id.perfil ->{
                        true
                    }
                    else -> false
                }
            }
        })

        // Read from the database
        myRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                pelicuas= ArrayList<Pelicula>()
                val value = snapshot.value
                Log.d(TAG, "Value is: " + value)

                snapshot.children.forEach{
                    hijo ->
                    var pelicula: Pelicula = Pelicula(hijo.child("nombre").toString(),hijo.child("genero").toString(),hijo.child("anio").toString(), hijo.key.toString())
                    pelicuas.add(pelicula)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }

        })

        fun llenalista()
        {
            val adaptador = PeliAdapter(this,pelicuas)
            var lista = findViewById<ListView>(R.id.lista)
            lista.adapter = adaptador
        }
    }

    override fun onKeyDown(keyCode: Int, event:KeyEvent?): Boolean{

        if (keyCode== KeyEvent.KEYCODE_BACK)
        {
            auth.signOut()
        }
        return super.onKeyDown(keyCode, event)
    }
}