package com.example.practica03

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Firebase
import com.google.firebase.database.database

class Detalle : AppCompatActivity() {

    val database = Firebase.database
    val myRef = database.getReference("Peliculas")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle)

        var nombre = findViewById<EditText>(R.id.anombre_peli)
        var genero = findViewById<EditText>(R.id.agenero_peli)
        var anio = findViewById<EditText>(R.id.aaño_peli)
        var imagen = findViewById<ImageView>(R.id.ImagenDe)
        var editar = findViewById<Button>(R.id.agregar_peli)
        var eliminar = findViewById<Button>(R.id.eliminar_peli)
        val parametros = intent.extras

        nombre.setText(parametros?.getCharSequence("nombre").toString())
        genero.setText(parametros?.getCharSequence("genero").toString())
        anio.setText(parametros?.getCharSequence("anio").toString())

        if(parametros?.getCharSequence("genero") == "terror")
        {
            imagen.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.terror))
        }
        else if(parametros?.getCharSequence("genero") == "comedia")
        {
            imagen.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.comedia))
        }
        else {
            imagen.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ada))
        }
        editar.setOnClickListener {
            var pelicula = PeliCampos(nombre.text.toString(),genero.text.toString(),anio.text.toString())
            myRef.child(parametros?.getCharSequence("id").toString()).setValue(pelicula).addOnCompleteListener {
                task ->
                if(task.isSuccessful){
                    Toast.makeText(this,"Exito pelicula editada",Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this,"Error"+task.exception!!.message.toString(),Toast.LENGTH_LONG).show()
                }
            }
        }

        eliminar.setOnClickListener {

            val builder: AlertDialog.Builder = MaterialAlertDialogBuilder(this)
            builder.setMessage("¿Estas seguro de eliminar esta pelicula)")
                .setPositiveButton("Aceptar"){ dialog, id ->


                    myRef.child(parametros?.getCharSequence("id").toString()).removeValue()
                        .addOnCompleteListener {
                            task ->
                            if(task.isSuccessful)
                            {
                                Toast.makeText(this,"Exito pelicula eliminada",Toast.LENGTH_LONG).show()
                            }
                            else
                            {
                                Toast.makeText(this,"Error",Toast.LENGTH_LONG).show()
                            }
                        }
                }
                .setNegativeButton("Cancelar"){
                    dialog, id ->
                }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()

        }
    }
}