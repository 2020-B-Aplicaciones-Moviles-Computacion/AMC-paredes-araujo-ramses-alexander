package com.example.gamenode1

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.gamenode1.dto.FirestoreJuegoDTO
import com.example.gamenode1.dto.FirestoreJuegoLibreriaDTO
import com.example.gamenode1.dto.FirestoreUsuarioJuegoDTO
import com.example.gamenode1.dto.FirestoreUsuarioLibreriaDTO
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class ActividadAgregarJuegoBiblioteca : AppCompatActivity() {

    val arregloVideojuegos = arrayListOf<FirestoreJuegoDTO>()
    var juegoSeleccionado :FirestoreJuegoDTO?= null
    lateinit var storage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_agregar_juego_biblioteca)
        storage = Firebase.storage
        cargarJuegos()
        val btnAceptar = findViewById<Button>(R.id.btn_add_game_library_accept)
        btnAceptar.setOnClickListener{
            Log.i("click","si entra")
            crearLibreriaUsuario()
        }
        val btnCancelar = findViewById<Button>(R.id.btn_add_game_library_deny)
        btnCancelar.setOnClickListener{
            irActividad(ActividadMenuPrincipal::class.java)
        }
    }


    fun cargarJuegos(){
        // val arreglo = ArrayList<FirestoreRestauranteDTO>()

        val spinnerJuegos = findViewById(R.id.sp_add_game_library) as Spinner
        val adaptadorRestaurantes = ArrayAdapter(this,android.R.layout.simple_list_item_1,arregloVideojuegos)
        spinnerJuegos.adapter = adaptadorRestaurantes
        spinnerJuegos.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                Log.i("firebase-firestore","Nada")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var x =arregloVideojuegos[position]
                Log.i("firebase-firestore","{$position} ${x}")
                juegoSeleccionado = arregloVideojuegos[position]
                //aqui poner en pantalla toda la inf del juego
                cargarInfJuegoSel(juegoSeleccionado!!)
            }

        }

        val db = Firebase.firestore

        val referencia = db.collection("juego")

        referencia.get()
            .addOnSuccessListener {
                Log.i("firebase-firestore","---------$it")
                for(document in it){
                    Log.i("firebase-firestore","${document.id} => ${document.data}")
                    val juego = document.toObject(FirestoreJuegoDTO::class.java)
                    juego.uid = document.id
                    arregloVideojuegos.add(juego)
                    adaptadorRestaurantes.notifyDataSetChanged()
                }
            }
            .addOnFailureListener{

            }
    }
    fun cargarInfJuegoSel(juego: FirestoreJuegoDTO){
        //add_game_library_developer
        val textDate = findViewById<TextView>(R.id.tv_add_game_library_date)
        val textDesxription = findViewById<TextView>(R.id.tv_add_game_library_description)
        val textDeveloper = findViewById<TextView>(R.id.tv_add_game_library_developer)
        val textType = findViewById<TextView>(R.id.tv_add_game_library_type)

        descargarImagenPerfil(juego.fotoFondo)
        textDate.text = juego.fechaSalida
        textDesxription.text=juego.descripcion
        textDeveloper.text=juego.desarrolladora
        textType.text=juego.tipo
    }
    fun descargarImagenPerfil(nameImageAccount:String) {

        //gs://moviles-computacion-gamenodepa.appspot.com/20210312_003235Fondo.jpg
        val gsReference = storage.getReferenceFromUrl("gs://moviles-computacion-gamenodepa.appspot.com/${nameImageAccount}.jpg")
        var bitmap: Bitmap? = null
        //1024 * 1024 *5     800000
        gsReference.getBytes(1024 * 1024 *5 ).addOnSuccessListener {
            bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            Log.i("image","se obtuvo la imagen perfil")

            val imageView = findViewById<ImageView>(R.id.iv__add_game_library)
            imageView?.setImageBitmap(bitmap)

        }.addOnFailureListener{

        }

    }

    fun crearLibreriaUsuario(){

        if (true) {
            val usuario = FirestoreUsuarioLibreriaDTO(AuthUsuario.infUsuario!!.uid,AuthUsuario.infUsuario!!.name,AuthUsuario.infUsuario!!.fotoPerfil)
            var juego = FirestoreJuegoLibreriaDTO(juegoSeleccionado!!.uid,juegoSeleccionado!!.desarrolladora,juegoSeleccionado!!.fotoFondo,juegoSeleccionado!!.nombre,juegoSeleccionado!!.tipo)


            val nuevoJuegoEnLibreria = hashMapOf<String,Any?>(
                    "fechaAgregacion" to Timestamp(System.currentTimeMillis()),
                    "usuario" to usuario,
                    "juego" to juego
            )
            Log.i("firebase-firestore libreria","Se agrega fecha 1: ${Timestamp(System.currentTimeMillis())}")
            Log.i("firebase-firestore libreria","Se agrega ${nuevoJuegoEnLibreria}")

            val db = Firebase.firestore
            val referencia = db.collection("libreria")
                    .document()
                    .set(nuevoJuegoEnLibreria)
            referencia
                    .addOnSuccessListener {
                        //se sube las imagenes con los nombes
                        //includesForUploadFilesPerfil(imgJuegos)
                        //AuthUsuario.infUsuario = FirestoreInfoUsuarioDTO("",correo,descripcion,fechaNac,fotoPerfil,fotoFondo,name)
                        Toast.makeText(this, "${this.getString(R.string.juego_añadido_exito)}", Toast.LENGTH_SHORT).show()
                        irActividad(ActividadMenuPrincipal::class.java)
                    }
                    .addOnFailureListener{
                        Toast.makeText(this, "${this.getString(R.string.juego_añadido_fail)}", Toast.LENGTH_SHORT).show()
                    }


        }

    }
    fun irActividad(
            clase: Class<*>,
            parametros: ArrayList<Pair<String,*>>? = null,
            codigo:Int? = null
    ){
        val intentExplicito = Intent(
                this,
                clase
        )
        //FOR
        Log.i("intent-explicito","Se envia: ${parametros}")
        if(parametros!=null){
            parametros.forEach{
                var nombreVariable = it.first
                var valorVariable = it.second

                var tipoDato = false
                tipoDato = it.second is String
                if (tipoDato){
                    intentExplicito.putExtra(nombreVariable.toString(),valorVariable as String)
                }
                tipoDato = it.second is Int
                if(tipoDato){
                    intentExplicito.putExtra(nombreVariable.toString(),valorVariable as Int)
                }
                tipoDato = it.second is Parcelable
                if(tipoDato){
                    intentExplicito.putExtra(nombreVariable.toString(),valorVariable as Parcelable)
                }
            }
        }
        if (codigo!=null){
            startActivityForResult(intentExplicito,codigo)
        }else{
            startActivity(intentExplicito)
        }


    }

}