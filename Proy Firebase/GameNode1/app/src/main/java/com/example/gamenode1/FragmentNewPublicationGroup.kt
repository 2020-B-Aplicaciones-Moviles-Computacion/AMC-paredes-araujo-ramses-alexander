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

class FragmentNewPublicationGroup : Fragment() {

    private var groupUid: String? = null
    lateinit var storage: FirebaseStorage
    var asignacionBusqueda: FirestoreAsignacionDTO? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      arguments?.let {
            groupUid = it.getString("grupoUID")
        }
        Log.i("publicacion-id"," onCreate publicacionUid: ${groupUid}")
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_new_publication_group, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        storage = Firebase.storage
        cargarNombreFoto()
        val btnCrearNuevaPublicacionGrupo = getView()?.findViewById<Button>(R.id.btn_create_new_publication_group)
        btnCrearNuevaPublicacionGrupo?.setOnClickListener {
            //agregarPublicacion()
            obtencionAsignacion()
        }

        val btnCancelarCrearNuevoComentario = getView()?.findViewById<Button>(R.id.btn_cancel_create_new_publication_group)
        btnCancelarCrearNuevoComentario?.setOnClickListener{

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

    fun obtencionAsignacion(){
        //si pertenece debe tener su asignacion
        //si no pertenece se le creara
        val db = Firebase.firestore
        val referencia = db.collection("asignacion")
        val listaAsignacionesAux = arrayListOf<FirestoreAsignacionDTO>()
        referencia
            .whereEqualTo("grupo.uid", groupUid)
            .whereEqualTo("usuario.uid",AuthUsuario.infUsuario?.uid.toString())
            .get()
            .addOnSuccessListener {
                for (document in it){
                    val asignacion = document.toObject(FirestoreAsignacionDTO::class.java)
                    asignacion.uid=document.id
                    listaAsignacionesAux.add(asignacion)
                }
                asignacionBusqueda = listaAsignacionesAux.get(0)
                Log.i("asignacion verificacion", "El usuario existe es: ${asignacionBusqueda?.uid}  ${asignacionBusqueda?.grupo?.nombre}")
                agregarPublicacion()
            }.addOnFailureListener {
                Log.i("asignacion verificacion", "El usuario no existe error")
            }
    }



    fun agregarPublicacion(){

        if (true) {
            val asignacion = FirestorePublicacionAsignacionDTO(
                groupUid.toString(),
                asignacionBusqueda?.grupo?.uid!!,
                asignacionBusqueda?.grupo?.nombre!!,
                asignacionBusqueda?.grupo?.uidJuego!!,
                asignacionBusqueda?.grupo?.nombreJuego!!,
                asignacionBusqueda?.grupo?.fotoJuego!!,
                asignacionBusqueda?.usuario?.uid!!,
                asignacionBusqueda?.usuario?.foto!!,
                asignacionBusqueda?.usuario?.nombre!!
            )
            var descripcion = getView()?.findViewById<EditText>(R.id.et_publication_new_comment_description)?.text.toString()
            var titulo = getView()?.findViewById<EditText>(R.id.et_publication_new_comment_titulo)?.text.toString()


            val nuevoComentario = hashMapOf<String,Any?>(
                "fechaAgregacionPublicacion" to Timestamp(System.currentTimeMillis()),
                "titulo" to titulo,
                "descripcion" to descripcion,
                "asignacion" to asignacion
            )
            Log.i("firebase-firestore libreria","Se agrega fecha 1: ${Timestamp(System.currentTimeMillis())}")
            Log.i("firebase-firestore libreria","Se agrega ${nuevoComentario}")

            val db = Firebase.firestore
            val referencia = db.collection("publicacionGrupo")
                .document()
                .set(nuevoComentario)
            referencia
                .addOnSuccessListener {
                    //se sube las imagenes con los nombes
                    //includesForUploadFilesPerfil(imgJuegos)
                    //AuthUsuario.infUsuario = FirestoreInfoUsuarioDTO("",correo,descripcion,fechaNac,fotoPerfil,fotoFondo,name)
                    Toast.makeText(this.requireContext(), "${this.getString(R.string.new_publication_group_exito)}", Toast.LENGTH_SHORT).show()

                    val parametros = arrayListOf<Pair<String,*>>(
                        Pair("grupoid",groupUid)
                    )

                    getView()?.findViewById<EditText>(R.id.et_publication_new_comment_titulo)?.setText("")
                    getView()?.findViewById<EditText>(R.id.et_publication_new_comment_description)?.setText("")

                    //irActividad(ActividadPerfilGrupo::class.java,parametros)
                }
                .addOnFailureListener{
                    Toast.makeText(this.requireContext(), "${this.getString(R.string.new_publication_group_fail)}", Toast.LENGTH_SHORT).show()
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