package com.example.gamenode1

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.gamenode1.dto.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.sql.Timestamp

class FragmentPublicationNewComment: Fragment() {
    //Asociamos el layout
    private var publicacionUid: String? = null
    lateinit var storage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            publicacionUid = it.getString("publicacionUid")
        }
        Log.i("publicacionUid"," onCreate publicacionUid: ${publicacionUid}")
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_publication_new_comment, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        storage = Firebase.storage
        cargarNombreFoto()
        val btnCrearNuevoComentario = getView()?.findViewById<Button>(R.id.btn_publication_new_comment_accept)
        btnCrearNuevoComentario?.setOnClickListener {
            agregarComentario()
        }

        val btnCancelarCrearNuevoComentario = getView()?.findViewById<Button>(R.id.btn_publication_new_comment_cancel)
        btnCancelarCrearNuevoComentario?.setOnClickListener{
            val parametros = arrayListOf<Pair<String,*>>(
                    Pair("publicacionSelec",publicacionUid)
            )
            getView()?.findViewById<EditText>(R.id.et_publication_new_comment_description)?.setText("")
                    //et_publication_new_comment_description
            //irActividad(ActividadAgregarComentarioPublicacion::class.java,parametros)
        }
    }
    fun cargarNombreFoto(){
        val nombreUsuario = getView()?.findViewById<TextView>(R.id.tv_new_publication_group_userName)
        val fotoUsuario = getView()?.findViewById<ImageView>(R.id.iv_new_publication_group)
        nombreUsuario?.setText(AuthUsuario.infUsuario?.name)
        if (fotoUsuario != null) {
            descargarImagenPerfil(AuthUsuario.infUsuario?.fotoPerfil!!,fotoUsuario)
        }

    }

    fun agregarComentario(){

        if (true) {
            val usuarioCom = FirestoreUsuarioComentarioDTO(AuthUsuario.infUsuario!!.uid,AuthUsuario.infUsuario!!.fotoPerfil,AuthUsuario.infUsuario!!.name)
            var publicationCom = FirestorePublicacionComentarioDTO(publicacionUid.toString())
            var descripcion = getView()?.findViewById<EditText>(R.id.et_publication_new_comment_description)?.text.toString()

            val nuevoComentario = hashMapOf<String,Any?>(
                    "fechaAgregacionComentario" to Timestamp(System.currentTimeMillis()),
                    "usuario" to usuarioCom,
                    "publicacion" to publicationCom,
                    "descripcion" to descripcion
            )
            Log.i("firebase-firestore libreria","Se agrega fecha 1: ${Timestamp(System.currentTimeMillis())}")
            Log.i("firebase-firestore libreria","Se agrega ${nuevoComentario}")

            val db = Firebase.firestore
            val referencia = db.collection("comentarioPublicacion")
                    .document()
                    .set(nuevoComentario)
            referencia
                    .addOnSuccessListener {
                        //se sube las imagenes con los nombes
                        //includesForUploadFilesPerfil(imgJuegos)
                        //AuthUsuario.infUsuario = FirestoreInfoUsuarioDTO("",correo,descripcion,fechaNac,fotoPerfil,fotoFondo,name)
                        Toast.makeText(this.requireContext(), "${this.getString(R.string.mensaje_comentario_creado_exito)}", Toast.LENGTH_SHORT).show()

                        getView()?.findViewById<EditText>(R.id.et_publication_new_comment_description)?.setText("")
                        val parametros = arrayListOf<Pair<String,*>>(
                                Pair("publicacionSelec",publicacionUid)
                        )
                        //irActividad(ActividadAgregarComentarioPublicacion::class.java,parametros)
                    }
                    .addOnFailureListener{
                        Toast.makeText(this.requireContext(), "${this.getString(R.string.mensaje_comentario_creado_fail)}", Toast.LENGTH_SHORT).show()
                    }


        }

    }

    fun irActividad(
            clase: Class<*>,
            parametros: ArrayList<Pair<String,*>>? = null,
            codigo:Int? = null
    ){
        val intentExplicito = Intent(
                this.context,
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
}