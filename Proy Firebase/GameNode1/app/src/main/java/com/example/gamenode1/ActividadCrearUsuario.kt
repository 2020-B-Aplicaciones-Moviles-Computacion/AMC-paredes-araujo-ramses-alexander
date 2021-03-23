package com.example.gamenode1

import android.app.DatePickerDialog
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
import android.widget.*
import com.example.gamenode1.dto.FirestoreInfoUsuarioDTO
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import com.google.firebase.storage.ktx.storage

class ActividadCrearUsuario : AppCompatActivity() {
    lateinit var photoFile: File
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_crear_usuario)
        iniciarBotonFecha()
        val correoTextView = findViewById<TextView>(R.id.et_crear_usuario_correo)
        correoTextView.setText(AuthUsuario.usuario?.email.toString())
        val nombreTextView = findViewById<TextView>(R.id.et_crear_group_nombre)
        nombreTextView.setText(AuthUsuario.usuario?.name.toString())


        val botonSeleccionarFoto = findViewById<Button>(
            R.id.btn_create_group_sel_img
        )
        botonSeleccionarFoto
            .setOnClickListener {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.type = "image/*"
                startActivityForResult(intent,303)
            }
        val botonSeleccionarFotoFondo = findViewById<Button>(
            R.id.btn_crear_usuario_sel_img_fondo
        )
        botonSeleccionarFotoFondo
            .setOnClickListener {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.type = "image/*"
                startActivityForResult(intent,304)
            }
        val botonCrearUsuario = findViewById<Button>(R.id.btn_create_new_group)
        botonCrearUsuario.setOnClickListener{
            crearUsuario()

            Thread.sleep(2_000)
            irActividad(ActividadMenuPrincipal::class.java)
        }
        val botonCancelar = findViewById<Button>(R.id.btn_cancel_create_new_group)
        botonCancelar.setOnClickListener{
            irActividad(MainActivity::class.java)
        }
        //btn_cancelar_crear_usuario
    }
    fun iniciarBotonFecha(){
        //fecha
        val c= Calendar.getInstance()
        val year= c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val tvFecha = findViewById<TextView>(R.id.tv_crear_usuario_fechaNac)

        val botonSelFecha = findViewById<Button>(R.id.btn_crear_usuario_sel_fecha)
        botonSelFecha.setOnClickListener{
            var dpd = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener {
                        view, mYear,mMonth , mDay ->
                    val mmMonth = mMonth+1
                    val date = "$mDay/$mmMonth/$mYear"
                    tvFecha.setText(date)
                },year,month,day)
            dpd.show()
        }
    }


    fun crearUsuario(){
        //necesitamos un hashmap

        if (true) {
            var name = findViewById<EditText>(R.id.et_crear_group_nombre).text.toString()
            var correo = findViewById<EditText>(R.id.et_crear_usuario_correo).text.toString()
            var descripcion = findViewById<EditText>(R.id.et_crear_group_descripcion).text.toString()
            var fechaNac = findViewById<TextView>(R.id.tv_crear_usuario_fechaNac).text.toString()
            var fotoPerfil = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())+"Perfil"
            var fotoFondo = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())+"Fondo"

            val nuevaInfUsuario = hashMapOf<String,Any?>(
                "name" to name,
                "correo" to correo,
                "descripcion" to descripcion,
                "fechaNac" to fechaNac,
                "fotoPerfil" to fotoPerfil,
                "fotoFondo" to fotoFondo
            )


            val db = Firebase.firestore
            val referencia = db.collection("infUsuario")
                .document()
                .set(nuevaInfUsuario)
            referencia
                .addOnSuccessListener {
                    //se sube las imagenes con los nombes
                    includesForUploadFilesPerfil(fotoPerfil)
                    includesForUploadFilesFondo(fotoFondo)
                    //AuthUsuario.infUsuario = FirestoreInfoUsuarioDTO("",correo,descripcion,fechaNac,fotoPerfil,fotoFondo,name)
                    Toast.makeText(this, "${this.getString(R.string.nuevo_usuario_creado_exito)}", Toast.LENGTH_SHORT).show()
                    cargarInfPerfil(correo)

                    irActividad(ActividadMenuPrincipal::class.java)
                }
                .addOnFailureListener{
                    Toast.makeText(this, "${this.getString(R.string.nuevo_usuario_creado_fail)}", Toast.LENGTH_SHORT).show()
                }


        }

    }
    fun cargarInfPerfil(correo:String){
        val db = Firebase.firestore
        val referencia = db.collection("infUsuario")
        referencia
                .limit(1)
                .whereEqualTo("correo", correo)
                .get()
                .addOnSuccessListener {
                    for (document in it){
                        Log.i("firebase-firestore","XXXXXX ${document.data}  ID${document.id}")
                        AuthUsuario.infUsuario = document.toObject(FirestoreInfoUsuarioDTO::class.java)

                    }
                }.addOnFailureListener{
                    Log.i("firebase-firestore","Failure : ${it}")
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


    fun includesForUploadFilesPerfil(nameFile:String) {

        val storage = Firebase.storage
        val storageRef = storage.reference
        //val name = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val name = nameFile+".jpg"
        val imagetoUplRef = storageRef.child(name)

        val uploadImageRef = storageRef.child("images/perfil/${name}")

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

    fun includesForUploadFilesFondo(nameFile:String) {

        val storage = Firebase.storage
        val storageRef = storage.reference
        //val name = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val name = nameFile+".jpg"
        val imagetoUplRef = storageRef.child(name)

        val uploadImageRef = storageRef.child("images/fondo/${name}")

        imagetoUplRef.name == uploadImageRef.name // true
        imagetoUplRef.path == uploadImageRef.path // false
        val imageView = findViewById<ImageView>(R.id.iv_crear_usuario_img_fondo)

        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = imagetoUplRef.putBytes(data)
        uploadTask.addOnSuccessListener {
            Toast.makeText(this,"${this.getString(R.string.subir_archivo)}", Toast.LENGTH_SHORT)
            Log.i("storage-firebase","valio")
        }.addOnFailureListener {
            // Handle unsuccessful uploads
            Log.i("storage-firebase","fallo la subida de imagenes")
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
            304->{
                if(resultCode== RESULT_OK){
                    Log.i("resultado","Si selecciono imagen")
                    var uri = data?.data
                    val imageUri: Uri? = uri
                    if (uri!=null){

                        val imageView = findViewById<ImageView>(R.id.iv_crear_usuario_img_fondo)
                        val bitmap2 =
                            MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
                        imageView.setImageBitmap(bitmap2)
                    }
                }else{
                    Log.i("resultado","No selecciono una imagen")
                }

            }
            305->{
                if(resultCode== RESULT_OK){
                    Log.i("resultado","Si selecciono imagen")

                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    val imageView = findViewById<ImageView>(R.id.iv_create_group_img)
                    imageView.setImageBitmap(imageBitmap)
                }else{
                    Log.i("resultado","No selecciono una imagen")
                }

            }
            306->{
                if(resultCode== RESULT_OK){
                    Log.i("resultado","Si selecciono contacto")
                    val imageView = findViewById<ImageView>(R.id.iv_create_group_img)

                    //val imageBitmap = data?.extras?.get("data") as Bitmap
                    val imageBitmap = BitmapFactory.decodeFile(photoFile.absolutePath)

                    imageView.setImageBitmap(imageBitmap)
                }else{
                    Log.i("resultado","No selecciono una imagen")
                }

            }
        }
    }
}