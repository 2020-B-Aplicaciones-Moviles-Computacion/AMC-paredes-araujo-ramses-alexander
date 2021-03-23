package com.example.gamenode1

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamenode1.dto.FirestoreInfoUsuarioDTO
import com.example.gamenode1.dto.FirestoreLibreriaDTO
import com.example.gamenode1.dto.FirestorePublicacionDTO
import com.facebook.FacebookSdk
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class ActividadPerfilInvitado : AppCompatActivity() {
    lateinit var storage: FirebaseStorage
    lateinit var usuarioVisita: FirestoreInfoUsuarioDTO
    val arregloPublicaciones = arrayListOf<FirestorePublicacionDTO>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_perfil_invitado)
        storage = Firebase.storage
        val userUID = intent.getStringExtra("useruid")
        if (userUID != null) {
            cargarUsuarioUID(userUID)
        }
        //tv_visit_account_return
        val btnRegresar = findViewById<TextView>(R.id.tv_visit_group_return)
        btnRegresar.setOnClickListener{
            irActividad(ActividadMenuPrincipal::class.java)
        }
    }
    fun cargarUsuarioUID(uid:String){
        Log.i("firebase-firestore","XXXXXX ESTA ENTRANDO A CARGAR con : ${uid}")
        val db = Firebase.firestore
        var usuarioBuscado: FirestoreInfoUsuarioDTO? = null
        val referencia = db.collection("infUsuario")
        referencia.document(uid)
            .get()
            .addOnSuccessListener { document->

                Log.i("firebase-firestore","XXXXXX ${document.data}  ID${document.id}")
                usuarioBuscado = document.toObject(FirestoreInfoUsuarioDTO::class.java)
                usuarioBuscado!!.uid=uid

                usuarioVisita = usuarioBuscado as FirestoreInfoUsuarioDTO

                cargarPerfil(usuarioBuscado!!)

                iniciarRecyclerViewPublicaciones()

            }.addOnFailureListener{
                Log.i("firebase-firestore","Failure : ${it}")
            }
    }


    fun cargarPerfil(usuario: FirestoreInfoUsuarioDTO){

        descargarImagen(usuario.fotoFondo,findViewById<ImageView>(R.id.iv_visit_group_background))
        descargarImagen(usuario.fotoPerfil,findViewById<ImageView>(R.id.iv_visit_group_profile_picture))
        findViewById<TextView>(R.id.tv_visit_group_name).text=usuario.name
        findViewById<TextView>(R.id.tv_visit_group_description).text=usuario.descripcion
        //---------
        iniciarHeaderFragmentoAccount()
    }
    @SuppressLint("ResourceAsColor")
    fun iniciarHeaderFragmentoAccount(){
        val btnAccountPublication = findViewById<LinearLayout>(R.id.ll_publications_group)
        val btnAccountLibrary = findViewById<LinearLayout>(R.id.ll_new_publication_group)
        val btnAccountPublicationColor = findViewById<TextView>(R.id.tv_publications_group_color)
        val btnAccountLibraryColor = findViewById<TextView>(R.id.tv_new_publication_group_color)
        btnAccountPublicationColor?.setBackgroundColor(R.color.black)
        btnAccountLibraryColor?.setBackgroundColor(0)

        btnAccountPublication?.setOnClickListener{
            btnAccountPublicationColor?.setBackgroundColor(R.color.black)
            btnAccountLibraryColor?.setBackgroundColor(0)
            //crearFragmento(FragmentAccountPublication())
            Log.i("recyclerView inicio","esi esta entrando ")
            iniciarRecyclerViewPublicaciones()
        }
        btnAccountLibrary?.setOnClickListener{
            btnAccountPublicationColor?.setBackgroundColor(0)
            btnAccountLibraryColor?.setBackgroundColor(R.color.black)
            //crearFragmento(FragmentAccountLibrary())
            iniciarRecyclerViewLibreria()
        }
    }

    fun descargarImagen(nameImageAccount:String,imageView: ImageView) {

        //gs://moviles-computacion-gamenodepa.appspot.com/20210312_003235Fondo.jpg
        val gsReference = storage.getReferenceFromUrl("gs://moviles-computacion-gamenodepa.appspot.com/${nameImageAccount}.jpg")
        var bitmap: Bitmap? = null
        // 1024 *1024 * 5  800000
        gsReference.getBytes(1024 *1024 * 5).addOnSuccessListener {
            bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            Log.i("image","se obtuvo la imagen perfil")

            //val imageView = getView()?.findViewById<ImageView>(R.id.iv_publication_comment_profile_picture)
            imageView?.setImageBitmap(bitmap)

        }.addOnFailureListener{

        }

    }
    fun iniciarRecyclerViewPublicaciones(){


        val adapterPublications = ListAdapterPublicacion()
        adapterPublications.setContext(this)
        val recyclerViewPokemon = findViewById<RecyclerView>(R.id.rv_visit_account)
        recyclerViewPokemon?.adapter = adapterPublications
        recyclerViewPokemon?.layoutManager = LinearLayoutManager(FacebookSdk.getApplicationContext())

        //--------------------------------------------

        val db = Firebase.firestore

        val referencia = db.collection("publicacion")
        referencia
            .whereEqualTo("libreria.uidUsuario", usuarioVisita.uid)
            .orderBy("fechaPublicacion", Query.Direction.DESCENDING)
            //.whereEqualTo("usuario", FirestoreUsuarioLibreriaDTO(AuthUsuario.infUsuario!!.uid,AuthUsuario.infUsuario!!.name,AuthUsuario.infUsuario!!.fotoPerfil))
            //.where("regions", "array-contains", "west_coast");
            //.whereArrayContains("usuario", AuthUsuario.infUsuario!!.uid)
            .get()

            .addOnSuccessListener {
                Log.i("firebase-firestore","---------$it")
                for(document in it){
                    Log.i("firebase-firestore","${document.id} => ${document.data}")
                    val publicacion = document.toObject(FirestorePublicacionDTO::class.java)
                    Log.i("firebase-firestore-object"," La publicacion es : ${publicacion.descripcion}")
                    publicacion.uid = document.id
                    arregloPublicaciones.add(publicacion)
                    adapterPublications.notifyDataSetChanged()
                }
            }
            .addOnFailureListener{
                Log.i("firebase-firestore","No encontro nada")
            }
        adapterPublications.setData(arregloPublicaciones)

    }
    fun iniciarRecyclerViewLibreria(){

        val arregloLibrerias = arrayListOf<FirestoreLibreriaDTO>()
        val adapterPublications = ListAdapterLibreria()
        adapterPublications.setContext(this)
        val recyclerViewPokemon = findViewById<RecyclerView>(R.id.rv_visit_account)
        recyclerViewPokemon?.adapter = adapterPublications
        recyclerViewPokemon?.layoutManager = LinearLayoutManager(FacebookSdk.getApplicationContext())

        //--------------------------------------------
        arregloPublicaciones.clear()

        val db = Firebase.firestore

        val referencia = db.collection("libreria")
        referencia
            .whereEqualTo("usuario.uid", usuarioVisita.uid)
            .orderBy("fechaAgregacion", Query.Direction.DESCENDING)
            //.whereEqualTo("usuario", FirestoreUsuarioLibreriaDTO(AuthUsuario.infUsuario!!.uid,AuthUsuario.infUsuario!!.name,AuthUsuario.infUsuario!!.fotoPerfil))
            //.where("regions", "array-contains", "west_coast");
            //.whereArrayContains("usuario", AuthUsuario.infUsuario!!.uid)
            .get()

            .addOnSuccessListener {
                Log.i("firebase-firestore","---------$it")
                for(document in it){
                    Log.i("firebase-firestore","${document.id} => ${document.data}")
                    val libreria = document.toObject(FirestoreLibreriaDTO::class.java)
                    Log.i("firebase-firestore-object"," La publicacion es : ${libreria}")
                    libreria.uid = document.id
                    arregloLibrerias.add(libreria)
                    adapterPublications.notifyDataSetChanged()
                }
            }
            .addOnFailureListener{
                Log.i("firebase-firestore","No encontro nada")
            }
        adapterPublications.setData(arregloLibrerias)

    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val publicacionSelec = arregloPublicaciones.get(item.groupId)
        return when(item?.itemId){
            111 ->{

                val parametros = arrayListOf<Pair<String,*>>(
                    Pair("publicacionSelec",publicacionSelec.uid)
                )
                irActividad(ActividadAgregarComentarioPublicacion::class.java,parametros)
                //irActividad(VistaEditarEntrenador::class.java,parametros)
                return true
            }
            777 ->{

                //delete
                deletePublicacion(publicacionSelec)

                return true
            }
            999 ->{

                val parametros = arrayListOf<Pair<String,*>>(
                    Pair("useruid",publicacionSelec.libreria?.uidUsuario)
                )
                irActividad(ActividadPerfilInvitado::class.java,parametros)

                return true
            }
            else-> super.onContextItemSelected(item)
        }

    }
    private fun deletePublicacion(publicacionSelec: FirestorePublicacionDTO){
        val builder = AlertDialog.Builder(this)

        if (publicacionSelec.libreria?.uidUsuario==AuthUsuario.infUsuario?.uid){

            builder.setPositiveButton("${this.getString(R.string.mensaje_si)}"){_,_ ->
                //aqui va el borrado
                eliminarPublicacion(publicacionSelec)

            }
            builder.setNegativeButton("${this.getString(R.string.mensaje_no)}"){_,_->

            }

            builder.setTitle("${this.getString(R.string.titulo_eliminar_publicacion)}")
            builder.setMessage("${this.getString(R.string.mensaje_seguridad_eliminar)}")
            builder.create().show()
        }else{

            builder.setPositiveButton("${this.getString(R.string.mensaje_ok)}"){_,_ ->
                //aqui va el borrado

            }

            builder.setTitle("${this.getString(R.string.titulo_eliminar_publicacion)}")
            builder.setMessage("${this.getString(R.string.mensaje_eliminar_fail_dueño)}")
            builder.create().show()
        }

    }
    fun eliminarPublicacion(publicacion: FirestorePublicacionDTO){
        var uidEliminar = publicacion.uid
        val db = Firebase.firestore

        db.collection("publicacion").document(uidEliminar).delete()
            .addOnSuccessListener {
                Log.i("firebase-firestore-eliminacion","SE ELIMINO")
                //this.getString(
                Toast.makeText(this,"${this.getString(R.string.mensaje_eliminar_publicacion_exito)}", Toast.LENGTH_LONG).show()

                //---------------------------------------------------------------------
                db.collection("comentarioPublicacion").
                whereEqualTo("publicacion.uid",uidEliminar)
                    .get()
                    .addOnSuccessListener {
                        for(document in it){
                            db.collection("comentarioPublicacion").document(document.id).delete()
                        }
                        Toast.makeText(this,"${this.getString(R.string.mensaje_eliminar_publicacion_exito_comentarios)}", Toast.LENGTH_LONG).show()
                    }.addOnFailureListener {

                    }
                //---------------------------------------------------------------------------

                irActividad(ActividadMenuPrincipal::class.java)

            }.addOnFailureListener{

                Log.i("firebase-firestore-eliminacion","NO SE ELIMINO")
                Toast.makeText(this,"${this.getString(R.string.mensaje_eliminar_fail)}", Toast.LENGTH_LONG).show()
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