package com.example.gamenode1

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.gamenode1.dto.FirestorePublicacionDTO
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class ActividadAgregarComentarioPublicacion : AppCompatActivity() {
    lateinit var publicacion: FirestorePublicacionDTO
    lateinit var storage: FirebaseStorage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_agregar_comentario_publicacion)
        storage = Firebase.storage

        val publicacionuid = intent.getStringExtra("publicacionSelec")
        if (publicacionuid != null) {
            cargarPublicacion(publicacionuid)
        }

        val retorno = findViewById<TextView>(R.id.tv_publication_comment_return)
        retorno.setOnClickListener {
            irActividad(ActividadMenuPrincipal::class.java)
        }

    }
    fun crearFragmento(fragmento: Fragment){
        val fragmentManager = supportFragmentManager
        //Transacciones
        val fragmentTransaction = fragmentManager.beginTransaction()
        //Crear instancia de fragmento
        val primerFragmento = fragmento
        //val primerFragment=PrimerFragment()
        val argumentos = Bundle()
        argumentos.putString("publicacionUid",publicacion.uid)
        primerFragmento.arguments=argumentos

        //Añadir Fragmento
        fragmentTransaction.replace(R.id.rl_publication_comment,primerFragmento)
        //fragmentoActual = primerFragmento
        fragmentTransaction.commit()
    }

    fun cargarPublicacion(document: String){
        val db = Firebase.firestore

        val referencia = db.collection("publicacion")

        referencia
                .document(document)
                //.whereEqualTo("libreria.uidUsuario", AuthUsuario.infUsuario!!.uid)
                //.orderBy("fechaPublicacion", Query.Direction.DESCENDING)
                //.whereEqualTo("usuario", FirestoreUsuarioLibreriaDTO(AuthUsuario.infUsuario!!.uid,AuthUsuario.infUsuario!!.name,AuthUsuario.infUsuario!!.fotoPerfil))
                //.where("regions", "array-contains", "west_coast");
                //.whereArrayContains("usuario", AuthUsuario.infUsuario!!.uid)
                .get()

                .addOnSuccessListener {
                    Log.i("firebase-firestore","---------$it")
                    publicacion = it.toObject(FirestorePublicacionDTO::class.java)!!
                    publicacion.uid =document

                    Log.i("comentario-comentario","Llego la publicacion: ${publicacion}  con la libreria ${publicacion}")
                    llenarPublicacion(publicacion)
                    crearFragmento(FragmentPublicationComments())
                    iniciarNavegacion()

                }
                .addOnFailureListener{
                    iniciarNavegacion()
                    Log.i("firebase-firestore","No encontro nada")
                }
        //adapterPublicaciones.setData(arregloPublicaciones)
    }
    fun llenarPublicacion(publicacion: FirestorePublicacionDTO){
        //_publication_comment
        val titulo = findViewById<TextView>(R.id.tv_publication_comment_title)
        val fecha = findViewById<TextView>(R.id.tv_publication_comment_date)
        val description = findViewById<TextView>(R.id.tv_publication_comment_description)
        val nameAndGame = findViewById<TextView>(R.id.tv_publication_comment_name_and_game)
        val tags = findViewById<TextView>(R.id.tv_publication_comment_tags)

        titulo.setText(publicacion.titulo)
        fecha.setText(publicacion.fechaPublicacion?.toDate().toString())
        description.setText(publicacion.descripcion)
        nameAndGame.setText("-"+publicacion.libreria?.nombreUsuario+"\n-"+publicacion.libreria?.nombreJuego)
        descargarImagen(publicacion.libreria?.fotoUsuario!!,findViewById<ImageView>(R.id.iv_publication_comment_profile_picture))
        descargarImagen(publicacion.libreria?.fotoJuego!!,findViewById<ImageView>(R.id.iv_publication_comment_game))



    }

    fun descargarImagen(nameImageAccount:String, imageView: ImageView) {

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
    @SuppressLint("ResourceAsColor")
    fun iniciarNavegacion(){
        val addNew = findViewById<LinearLayout>(R.id.ll_groups_op_new_groups)
        val comments = findViewById<LinearLayout>(R.id.ll_groups_op_my_groups)
        val addNewColor = findViewById<TextView>(R.id.tv_groups_op_new_groups_color)
        val commentsColor = findViewById<TextView>(R.id.tv_groups_op_my_groups_color)
        commentsColor?.setBackgroundColor(R.color.black)
        addNewColor?.setBackgroundColor(0)

        addNew?.setOnClickListener{
            addNewColor?.setBackgroundColor(R.color.black)
            commentsColor?.setBackgroundColor(0)
            crearFragmento(FragmentPublicationNewComment())
        }
        comments?.setOnClickListener{
            addNewColor?.setBackgroundColor(0)
            commentsColor?.setBackgroundColor(R.color.black)
            crearFragmento(FragmentPublicationComments())
            //FragmentPublicationComments()
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