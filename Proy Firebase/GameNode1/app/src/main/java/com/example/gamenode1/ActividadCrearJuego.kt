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
import com.example.gamenode1.dto.FirestoreUsuarioJuegoDTO
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class ActividadCrearJuego : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_crear_juego)
        iniciarBotonFecha()


        val botonSeleccionarFoto = findViewById<Button>(
            R.id.btn_create_game_sel_img
        )
        botonSeleccionarFoto
            .setOnClickListener {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.type = "image/*"
                startActivityForResult(intent,303)
            }
        val botonCrearJuego = findViewById<Button>(
            R.id.btn_crear_nuevo_juego
        )
        botonCrearJuego.setOnClickListener{
            crearJuego()
        }
        val btnCancelar = findViewById<Button>(R.id.btn_cancelar_crear_juego)
        btnCancelar.setOnClickListener{
            irActividad(ActividadMenuPrincipal::class.java)

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
    fun iniciarBotonFecha(
    ) {
        //fecha
        val c= Calendar.getInstance()
        val year= c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val tvFecha = findViewById<TextView>(R.id.tv_create_game_fecha)

        val botonSelFecha = findViewById<Button>(R.id.btn_create_game_sel_fecha)
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


    fun crearJuego(){
        //necesitamos un hashmap

        if (true) {
            var nombre = findViewById<EditText>(R.id.et_create_game_nombre).text.toString()
            var tipo = findViewById<EditText>(R.id.et_create_game_tipo).text.toString()
            var desarrolladora = findViewById<EditText>(R.id.et_create_game_developer).text.toString()
            var descripcion = findViewById<EditText>(R.id.et_create_game_description).text.toString()
            var fechaNac = findViewById<TextView>(R.id.tv_create_game_fecha).text.toString()
            var imgJuegos = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())+"Game"

            val nuevoJuego = hashMapOf<String,Any?>(
                "usuario" to FirestoreUsuarioJuegoDTO(AuthUsuario.infUsuario?.uid.toString()),
                "nombre" to nombre,
                "tipo" to tipo,
                "desarrolladora" to desarrolladora,
                "descripcion" to descripcion,
                "fechaSalida" to fechaNac,
                "fotoFondo" to imgJuegos
            )


            val db = Firebase.firestore
            val referencia = db.collection("juego")
                .document()
                .set(nuevoJuego)
            referencia
                .addOnSuccessListener {
                    //se sube las imagenes con los nombes
                    includesForUploadFilesPerfil(imgJuegos)
                    //AuthUsuario.infUsuario = FirestoreInfoUsuarioDTO("",correo,descripcion,fechaNac,fotoPerfil,fotoFondo,name)
                    Toast.makeText(this, "${this.getString(R.string.nuevo_juego_creado_exito)}", Toast.LENGTH_SHORT).show()
                    irActividad(ActividadMenuPrincipal::class.java)
                }
                .addOnFailureListener{
                    Toast.makeText(this, "${this.getString(R.string.nuevo_juego_creado_fail)}", Toast.LENGTH_SHORT).show()
                }


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
        val imageView = findViewById<ImageView>(R.id.iv_create_game_img_portada)

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            303->{
                if(resultCode== RESULT_OK){
                    Log.i("resultado","Si selecciono imagen")
                    var uri = data?.data
                    val imageUri: Uri? = uri
                    if (uri!=null){

                        val imageView = findViewById<ImageView>(R.id.iv_create_game_img_portada)
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
}