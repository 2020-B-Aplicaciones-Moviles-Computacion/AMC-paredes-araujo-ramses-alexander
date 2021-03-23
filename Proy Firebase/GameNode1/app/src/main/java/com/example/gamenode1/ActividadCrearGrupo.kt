package com.example.gamenode1

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import com.example.gamenode1.dto.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class ActividadCrearGrupo : AppCompatActivity() {

    val arregloVideojuegos = arrayListOf<FirestoreJuegoDTO>()
    var juegoSeleccionado :FirestoreJuegoDTO?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_crear_grupo)
        cargarJuegos()
        val botonSeleccionarFoto = findViewById<Button>(
            R.id.btn_create_group_sel_img
        )
        botonSeleccionarFoto
            .setOnClickListener {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.type = "image/*"
                startActivityForResult(intent,303)
            }
        val botonCrearJuego = findViewById<Button>(
            R.id.btn_create_new_group
        )
        botonCrearJuego.setOnClickListener{
            crearGrupo()
        }
        val btnCancelar = findViewById<Button>(R.id.btn_cancel_create_new_group)
        btnCancelar.setOnClickListener{
            irActividad(ActividadMenuPrincipal::class.java)

        }



    }

    fun crearGrupo(){
        //necesitamos un hashmap

        if (true) {
            var nombre = findViewById<EditText>(R.id.et_crear_group_nombre).text.toString()
            var descripcion = findViewById<EditText>(R.id.et_crear_group_descripcion).text.toString()
            var juego = FirestoreJuegoGrupoDTO(juegoSeleccionado?.uid!!,juegoSeleccionado?.nombre!!,juegoSeleccionado?.fotoFondo!!)
            var imgGrupo = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())+"Group"
            val fechaCreacion = Timestamp(System.currentTimeMillis())
            val a = fechaCreacion

            val nuevoJuego = hashMapOf<String,Any?>(
                "usuario" to FirestoreUsuarioGrupoDTO(AuthUsuario.infUsuario?.uid.toString()),
                "nombre" to nombre,
                "descripcion" to descripcion,
                "imagen" to imgGrupo,
                "juego" to juego,
                "fechaCreacion" to fechaCreacion
            )


            val db = Firebase.firestore
            val referencia = db.collection("grupo")
                .document()
                .set(nuevoJuego)
            referencia
                .addOnSuccessListener {
                    //se sube las imagenes con los nombes
                    includesForUploadFilesPerfil(imgGrupo)
                    //AuthUsuario.infUsuario = FirestoreInfoUsuarioDTO("",correo,descripcion,fechaNac,fotoPerfil,fotoFondo,name)
                    Toast.makeText(this, "${this.getString(R.string.nuevo_grupo_creado_exito)}", Toast.LENGTH_SHORT).show()
                    agregarUsuarioGrupoCreado(fechaCreacion)
                }
                .addOnFailureListener{
                    Toast.makeText(this, "${this.getString(R.string.nuevo_grupo_creado_fail)}", Toast.LENGTH_SHORT).show()
                }


        }

    }

    fun agregarUsuarioGrupoCreado(fechaCreacion : Timestamp){


        val db = Firebase.firestore
        val referencia = db.collection("grupo") .
        whereEqualTo("fechaCreacion",fechaCreacion)
            .get()
        referencia
            .addOnSuccessListener {
                //se sube las imagenes con los nombes
                for (document in it){
                    Log.i("firebase-firestore","${document.id} => ${document.data}")
                    val grupo = document.toObject(FirestoreGrupoDTO::class.java)
                    grupo.uid = document.id
                    //aqui hago la agregacion de asignacion
                    val nuevaAsignacion = hashMapOf<String,Any?>(
                        "fechaAsignacion" to fechaCreacion,
                        "grupo" to FirestoreGrupoAsignacionDTO(grupo.uid,grupo.nombre,grupo.juego?.uid!!,grupo.juego?.nombre!!,grupo.juego?.foto!!),
                        "usuario" to FirestoreUsuarioAsignacionDTO(AuthUsuario.infUsuario?.uid.toString(),AuthUsuario.infUsuario?.name.toString(),AuthUsuario.infUsuario?.fotoPerfil.toString())
                    )
                    db.collection("asignacion").document()
                        .set(nuevaAsignacion).addOnSuccessListener {
                            Toast.makeText(this, "${this.getString(R.string.nuevo_grupo_creado_dueño_agregado_exito)}", Toast.LENGTH_SHORT).show()

                            irActividad(ActividadMenuPrincipal::class.java)
                        }.addOnFailureListener {
                                Toast.makeText(this, "${this.getString(R.string.nuevo_grupo_creado_dueño_agregado_fail)}", Toast.LENGTH_SHORT).show()
                        }

                }
                //includesForUploadFilesPerfil(imgGrupo)
                //AuthUsuario.infUsuario = FirestoreInfoUsuarioDTO("",correo,descripcion,fechaNac,fotoPerfil,fotoFondo,name)
                //Toast.makeText(this, "Succes Creation!! \nPlease wait.", Toast.LENGTH_SHORT).show()
                irActividad(ActividadMenuPrincipal::class.java)
            }
            .addOnFailureListener{
                Toast.makeText(this, "${this.getString(R.string.nuevo_grupo_creado_dueño_agregado_fail)}", Toast.LENGTH_SHORT).show()
            }
    }

    fun includesForUploadFilesPerfil(nameFile:String) {

        val storage = Firebase.storage
        val storageRef = storage.reference
        //val name = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val name = nameFile+".jpg"
        val imagetoUplRef = storageRef.child(name)

        val uploadImageRef = storageRef.child("images/${name}")

        imagetoUplRef.name == uploadImageRef.name // true
        imagetoUplRef.path == uploadImageRef.path // false
        val imageView = findViewById<ImageView>(R.id.iv_create_group_img)

        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = imagetoUplRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener {
            Toast.makeText(this,"${this.getString(R.string.subir_archivo)}", Toast.LENGTH_SHORT)
            Log.i("storage-firebase","valio")
        }

    }


    fun cargarJuegos(){
        // val arreglo = ArrayList<FirestoreRestauranteDTO>()

        val spinnerJuegos = findViewById(R.id.sp_create_group_select_game) as Spinner
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
                //cargarInfJuegoSel(juegoSeleccionado!!)
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            303->{
                if(resultCode== RESULT_OK){
                    Log.i("resultado","Si selecciono imagen")
                    var uri = data?.data
                    val imageUri: Uri? = uri
                    if (uri!=null){

                        val imageView = findViewById<ImageView>(R.id.iv_create_group_img)
                        val bitmap2 =
                            MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
                        imageView.setImageBitmap(bitmap2)
                    }
                }else{
                    Log.i("resultado","No selecciono una imagen")
                }

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