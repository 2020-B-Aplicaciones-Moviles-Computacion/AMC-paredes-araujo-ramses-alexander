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
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.util.*

class ActividadEditarUsuario : AppCompatActivity() {
    lateinit var storage: FirebaseStorage
    val usuarioActual = AuthUsuario.infUsuario
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_editar_usuario)
        storage = Firebase.storage

        cargarValoresActuales()

        // botones

        iniciarBotonFecha()

        val botonSeleccionarFotoPerfil = findViewById<Button>(
            R.id.btn_actualizar_usuario_sel_img_perfil
        )
        botonSeleccionarFotoPerfil
            .setOnClickListener {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.type = "image/*"
                startActivityForResult(intent,303)
            }

        val botonSeleccionarFotoFondo = findViewById<Button>(
            R.id.btn_actualizar_usuario_sel_img_fondo
        )
        botonSeleccionarFotoFondo
            .setOnClickListener {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.type = "image/*"
                startActivityForResult(intent,304)
            }

        val botonActualizarUsuario = findViewById<Button>(R.id.btn_actualizar_nuevo_usuario)
        botonActualizarUsuario.setOnClickListener{
            actualizarUsuario()

        }
        val botonCancelar = findViewById<Button>(R.id.btn_cancelar_actualizar_usuario)
        botonCancelar.setOnClickListener{
            irActividad(MainActivity::class.java)
        }

        //btn_actualizar_usuario_sel_fecha
    }
    fun iniciarBotonFecha(){
        //fecha
        val c= Calendar.getInstance()
        val year= c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val tvFecha = findViewById<TextView>(R.id.tv_actualizar_usuario_fechaNac)

        val botonSelFecha = findViewById<Button>(R.id.btn_actualizar_usuario_sel_fecha)
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
    fun cargarValoresActuales(){
        Log.i("actualizacion ","si entra ${usuarioActual}")
        val etNombre= findViewById<EditText>(R.id.et_actualizar_usuario_nombre)
        val etDescripcion= findViewById<EditText>(R.id.et_actualizar_usuario_Descripcion)
        val etCorreo= findViewById<EditText>(R.id.et_actualizar_usuario_correo)
        val tvFechaNac = findViewById<TextView>(R.id.tv_actualizar_usuario_fechaNac)
        val ivProfile = findViewById<ImageView>(R.id.iv_actualizar_usuario_img_perfil)
        val ivBackground = findViewById<ImageView>(R.id.iv_actualizar_usuario_img_fondo)

        etNombre.setText(usuarioActual?.name)
        etDescripcion.setText(usuarioActual?.descripcion)
        etCorreo.setText(usuarioActual?.correo)
        tvFechaNac.setText(usuarioActual?.fechaNac)
        descargarImagenPerfil(usuarioActual?.fotoPerfil.toString(),ivProfile)
        descargarImagenPerfil(usuarioActual?.fotoFondo.toString(),ivBackground)

    }
    fun descargarImagenPerfil(nameImageAccount:String, imageView: ImageView) {

        //gs://moviles-computacion-gamenodepa.appspot.com/20210312_003235Fondo.jpg
        val gsReference = storage.getReferenceFromUrl("gs://moviles-computacion-gamenodepa.appspot.com/${nameImageAccount}.jpg")
        var bitmap: Bitmap? = null
        // 1024*1024*5 800000
        gsReference.getBytes(1024*1024*5).addOnSuccessListener {
            bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            Log.i("image","se obtuvo la imagen perfil")

            // val imageView =  findViewById<ImageView>(R.id.iv_account_profile_picture)
            imageView?.setImageBitmap(bitmap)

        }.addOnFailureListener{

        }

    }

    fun actualizarUsuario(){
        //necesitamos un hashmap
        var usuarioNuevo: FirestoreInfoUsuarioDTO? = null

        if (true) {
            var name = usuarioActual?.name
            var correo = usuarioActual?.correo
            var descripcion = findViewById<EditText>(R.id.et_actualizar_usuario_Descripcion).text.toString()
            var fechaNac = findViewById<TextView>(R.id.tv_actualizar_usuario_fechaNac).text.toString()
            var fotoPerfil = usuarioActual?.fotoPerfil
            var fotoFondo = usuarioActual?.fotoFondo
            usuarioNuevo =  FirestoreInfoUsuarioDTO(usuarioActual?.uid!!,usuarioActual.correo,descripcion,fechaNac,
                fotoPerfil.toString(),fotoFondo.toString(),name!!)

            val nuevaInfUsuario = hashMapOf<String,Any?>(
                "name" to name,
                "correo" to correo,
                "descripcion" to descripcion,
                "fechaNac" to fechaNac,
                "fotoPerfil" to fotoPerfil,
                "fotoFondo" to fotoFondo
            )

            //eliminar


            val db = Firebase.firestore
            val referencia = db.collection("infUsuario")
                .document(usuarioActual?.uid!!)
                .update(nuevaInfUsuario)
            referencia
                .addOnSuccessListener {
                    //borro las antiguas imagenes con el nombre
                    //subo con el mismo nombre

                    //perfil
                    eliminarSubirImagen(usuarioActual.fotoPerfil,findViewById<ImageView>(R.id.iv_actualizar_usuario_img_perfil))
                    //background
                    eliminarSubirImagen(usuarioActual.fotoFondo,findViewById<ImageView>(R.id.iv_actualizar_usuario_img_fondo))
                    AuthUsuario.infUsuario= usuarioNuevo
                    Toast.makeText(this, "${this.getString(R.string.usuario_actualizado_exito)}", Toast.LENGTH_SHORT).show()
                    irActividad(ActividadMenuPrincipal::class.java)
                }
                .addOnFailureListener{
                    Toast.makeText(this, "${this.getString(R.string.usuario_actualizado_fail)}", Toast.LENGTH_SHORT).show()
                }


        }

    }
    fun subirImagen(nameFile:String,imageView: ImageView) {

        val storage = Firebase.storage
        val storageRef = storage.reference
        //val name = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val name = nameFile+".jpg"
        val imagetoUplRef = storageRef.child(name)

        val uploadImageRef = storageRef.child("images/fondo/${name}")

        imagetoUplRef.name == uploadImageRef.name // true
        imagetoUplRef.path == uploadImageRef.path // false
        //val imageView = findViewById<ImageView>(R.id.iv_crear_usuario_img_fondo)

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

    fun eliminarSubirImagen(nameImage:String,imageView: ImageView){
        //getReferenceFromUrl("gs://moviles-computacion-gamenodepa.appspot.com/${nameImageAccount}.jpg")
        //var desertRef = storage.child("images/${nameImage}.jpg");
        var desertRef = storage.getReferenceFromUrl("gs://moviles-computacion-gamenodepa.appspot.com/${nameImage}.jpg")
// Delete the file
        desertRef.delete().addOnSuccessListener {
            Log.i("image","Se elimino la imagen")
            subirImagen(nameImage,imageView)
        }.addOnFailureListener{
            Log.i("image","Fallo al eliminar la imagen")
        }

    }/*
    fun eliminarImagenPortada(nameImage:String){
        //getReferenceFromUrl("gs://moviles-computacion-gamenodepa.appspot.com/${nameImageAccount}.jpg")
        //var desertRef = storage.child("images/${nameImage}.jpg");
        var desertRef = storage.getReferenceFromUrl("gs://moviles-computacion-gamenodepa.appspot.com/${nameImage}.jpg")
// Delete the file
        desertRef.delete().addOnSuccessListener {
            Log.i("image","Se elimino la imagen")
        }.addOnFailureListener{
            Log.i("image","Fallo al eliminar la imagen")
        }

    }*/




    fun irActividad(
        clase: Class<*>,
        parametros: ArrayList<Pair<String, *>>? = null,
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            303->{
                if(resultCode== RESULT_OK){
                    //perfil
                    Log.i("resultado","Si selecciono imagen")
                    var uri = data?.data
                    val imageUri: Uri? = uri
                    if (uri!=null){

                        val imageView = findViewById<ImageView>(R.id.iv_actualizar_usuario_img_perfil)
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
                    //background
                    Log.i("resultado","Si selecciono imagen")
                    var uri = data?.data
                    val imageUri: Uri? = uri
                    if (uri!=null){

                        val imageView = findViewById<ImageView>(R.id.iv_actualizar_usuario_img_fondo)
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
        }
    }
}