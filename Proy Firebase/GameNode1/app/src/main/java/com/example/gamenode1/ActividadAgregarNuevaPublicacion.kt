package com.example.gamenode1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.*
import com.example.gamenode1.dto.FirestoreJuegoLibreriaDTO
import com.example.gamenode1.dto.FirestoreLibreriaDTO
import com.example.gamenode1.dto.FirestoreLibreriaPublicacionDTO
import com.example.gamenode1.dto.FirestoreUsuarioLibreriaDTO
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.sql.Timestamp

class ActividadAgregarNuevaPublicacion : AppCompatActivity() {

    val arregloVideojuegos = arrayListOf<FirestoreLibreriaDTO>()
    var juegoLibreriaSeleccionado :FirestoreLibreriaDTO?= null
    var arregloEtiqutasPublicacion = arrayListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_agregar_nueva_publicacion)
        //btn_add_publication_tag_add
        cargarJuegos()
        val btnAgregarEtiqueta = findViewById<Button>(R.id.btn_add_publication_tag_add)
        btnAgregarEtiqueta.setOnClickListener{
            agregarEtiqueta()
        }
        val btnAceptar = findViewById<Button>(R.id.btn_add_publicacion_accept)
        btnAceptar.setOnClickListener{
            crearNuevaPublicacion()
        }
        val btnCancelar = findViewById<Button>(R.id.btn_add_publicacion_deny)
        btnCancelar.setOnClickListener{
            irActividad(ActividadMenuPrincipal::class.java)
        }
    }
    fun agregarEtiqueta(){
        val etEtiquetaNueva = findViewById<EditText>(R.id.et_add_publicacion_tag)
        Log.i("firebase-------","se agrego: ${etEtiquetaNueva.text.toString() }")
        val texto = etEtiquetaNueva.text.toString()
        arregloEtiqutasPublicacion.add(texto)
        val tvEtiquetasAñadidas = findViewById<TextView>(R.id.tv_add_publicacion_tags_added)
        val textoAnterior = tvEtiquetasAñadidas.text.toString()
        tvEtiquetasAñadidas.setText("${textoAnterior} - ${texto}")
        etEtiquetaNueva.setText("")
    }
    fun cargarJuegos(){
        // val arreglo = ArrayList<FirestoreRestauranteDTO>()

        val spinnerJuegos = findViewById(R.id.sp_add_publicacion_select_game) as Spinner
        val adaptadorLibreriaJuegos = ArrayAdapter(this,android.R.layout.simple_list_item_1,arregloVideojuegos)
        spinnerJuegos.adapter = adaptadorLibreriaJuegos
        spinnerJuegos.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                Log.i("firebase-firestore","Nada")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var x =arregloVideojuegos[position]
                Log.i("firebase-firestore","{$position} ${x}")
                juegoLibreriaSeleccionado = arregloVideojuegos[position]
                //aqui poner en pantalla toda la inf del juego
                //cargarInfJuegoSel(juegoSeleccionado!!)
            }

        }

        val db = Firebase.firestore

        val referencia = db.collection("libreria")

        referencia
                .whereEqualTo("usuario.uid", AuthUsuario.infUsuario!!.uid)
                //.whereEqualTo("usuario", FirestoreUsuarioLibreriaDTO(AuthUsuario.infUsuario!!.uid,AuthUsuario.infUsuario!!.name,AuthUsuario.infUsuario!!.fotoPerfil))
                //.where("regions", "array-contains", "west_coast");
                //.whereArrayContains("usuario", AuthUsuario.infUsuario!!.uid)
                .get()

                .addOnSuccessListener {
                    Log.i("firebase-firestore","---------$it")
                    for(document in it){
                        Log.i("firebase-firestore","${document.id} => ${document.data}")
                        val juegoLibreria = document.toObject(FirestoreLibreriaDTO::class.java)
                        juegoLibreria.uid = document.id
                        arregloVideojuegos.add(juegoLibreria)
                        adaptadorLibreriaJuegos.notifyDataSetChanged()
                    }
                }
                .addOnFailureListener{
                    Log.i("firebase-firestore","No encontro nada")
                }
    }

    fun crearNuevaPublicacion(){

        if (true) {
            //juegoLibreriaSeleccionado
            val libreria = FirestoreLibreriaPublicacionDTO(
                    juegoLibreriaSeleccionado!!.uid,
                    juegoLibreriaSeleccionado!!.juego!!.uid,
                    juegoLibreriaSeleccionado!!.juego!!.fotoJuego,
                    juegoLibreriaSeleccionado!!.juego!!.nombre,
                    juegoLibreriaSeleccionado!!.usuario!!.uid,
                    juegoLibreriaSeleccionado!!.usuario!!.nombre,
                    juegoLibreriaSeleccionado!!.usuario!!.fotoUser)
            val tituloPublicacion = findViewById<EditText>(R.id.et_add_publication_title).text
            val descripcionPublicacion = findViewById<EditText>(R.id.et_add_publicacion_description).text

            val nuevaPublicacion = hashMapOf<String,Any?>(
                    "fechaPublicacion" to Timestamp(System.currentTimeMillis()),
                    "libreria" to libreria, //Aqui esta inf del juego y usuario
                    "titulo" to tituloPublicacion.toString(),
                    "etiquetas" to arregloEtiqutasPublicacion,
                    "descripcion" to descripcionPublicacion.toString()
            )
            Log.i("firebase-firestore libreria","Se agrega fecha 1: ${Timestamp(System.currentTimeMillis())}")
            Log.i("firebase-firestore libreria","Se agrega ${nuevaPublicacion}")

            val db = Firebase.firestore
            val referencia = db.collection("publicacion")
                    .document()
                    .set(nuevaPublicacion)
            referencia
                    .addOnSuccessListener {
                        //AuthUsuario.infUsuario = FirestoreInfoUsuarioDTO("",correo,descripcion,fechaNac,fotoPerfil,fotoFondo,name)
                        Toast.makeText(this, "${this.getString(R.string.nueva_publicacion_agregada_exito)}", Toast.LENGTH_SHORT).show()
                        irActividad(ActividadMenuPrincipal::class.java)
                    }
                    .addOnFailureListener{
                        Toast.makeText(this, "${this.getString(R.string.nueva_publicacion_agregada_fail)}", Toast.LENGTH_SHORT).show()
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