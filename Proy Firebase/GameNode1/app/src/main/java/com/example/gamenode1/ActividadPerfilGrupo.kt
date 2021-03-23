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
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamenode1.dto.*
import com.facebook.FacebookSdk
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.sql.Timestamp

class ActividadPerfilGrupo : AppCompatActivity() {
    lateinit var storage: FirebaseStorage
    lateinit var grupoVisita: FirestoreGrupoDTO
    val arregloPublicaciones = arrayListOf<FirestorePublicacionDTO>()
    lateinit var grupoUID:String
    lateinit var asignacionuid:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_perfil_grupo)
        storage = Firebase.storage
        grupoUID = intent.getStringExtra("grupoid").toString()
        if (grupoUID != null) {
            cargarGrupoUID(grupoUID)
        }
        //en esta inicio, debido a que esta da valor al id de asignacion
        //iniciarUidAsignacion()
        //validacionPertenece()
        //tv_visit_account_return
        val btnRegresar = findViewById<TextView>(R.id.tv_visit_group_return)
        btnRegresar.setOnClickListener{
            irActividad(ActividadMenuPrincipal::class.java)
        }
        //----------------------
        //aqui verifico si pertenece al grupo o no
        // si no pertenece al grupo
        // se le activa iv_perfil_grupo_add
        //a travez del cual podra registrarse o crear su asignacion
        // una vez creado desaparecera y se activara el new publicacion
        val btnAgregar = findViewById<ImageView>(R.id.iv_perfil_grupo_add)
        btnAgregar.setOnClickListener {
            creacionAsignacion()
        }


    }
    /*
    fun iniciarUidAsignacion(){
        //si pertenece debe tener su asignacion
        //si no pertenece se le creara
        var aux = null
        val db = Firebase.firestore
        val referencia = db.collection("asignacion")
        val listaAsignacionesAux = arrayListOf<FirestoreAsignacionDTO>()
        referencia
            .whereEqualTo("grupo.uid", grupoUID)
            .whereEqualTo("usuario.uid",AuthUsuario.infUsuario?.uid.toString())
            .get()
            .addOnSuccessListener {
                for (document in it){
                    val asignacion = document.toObject(FirestoreAsignacionDTO::class.java)
                    asignacion.uid=document.id
                    listaAsignacionesAux.add(asignacion)
                }
                if(listaAsignacionesAux.isEmpty()){
                    Log.i("asignacion verificacion", "El usuario no existe")
                    Toast.makeText(this,"USER DONT EXIST IN GROUP\nIt will be created", Toast.LENGTH_LONG).show()
                    val parametros = arrayListOf<Pair<String,*>>(
                        Pair("grupoid",grupoUID)
                    )
                    //irActividad(ActividadPerfilGrupo::class.java,parametros)
                }else{
                    Log.i("asignacion verificacion", "El usuario existe")

                }
            }.addOnFailureListener {
                Log.i("asignacion verificacion", "El usuario no existe error")
                Toast.makeText(this,"USER DONT EXIST IN GROUP", Toast.LENGTH_LONG).show()
            }
    }*/


    fun creacionAsignacion(){
        //si pertenece debe tener su asignacion
        //si no pertenece se le creara
        val db = Firebase.firestore
        val referencia = db.collection("asignacion")
        val listaAsignacionesAux = arrayListOf<FirestoreAsignacionDTO>()
        referencia
            .whereEqualTo("grupo.uid", grupoUID)
            .whereEqualTo("usuario.uid",AuthUsuario.infUsuario?.uid.toString())
            .get()
            .addOnSuccessListener {
                for (document in it){
                    val asignacion = document.toObject(FirestoreAsignacionDTO::class.java)
                    asignacion.uid=document.id
                    listaAsignacionesAux.add(asignacion)
                }
                if(listaAsignacionesAux.isEmpty()){
                    Log.i("asignacion verificacion", "El usuario no existe")
                    Toast.makeText(this,"${this.getString(R.string.asignacion_no_existe)}", Toast.LENGTH_LONG).show()

                    agregarUsuarioGrupo()


                }else{

                    Log.i("asignacion verificacion", "El usuario existe no es necesario agregarlo")

                }
            }.addOnFailureListener {
                Log.i("asignacion verificacion", "El usuario no existe error")
                Toast.makeText(this,"${this.getString(R.string.asignacion_no_existe_fail)}", Toast.LENGTH_LONG).show()
            }
    }



    fun agregarUsuarioGrupo(){


        val db = Firebase.firestore
        val referencia = db.collection("grupo")
        //.whereEqualTo("fechaCreacion",fechaCreacion)
                .document(grupoUID)
                .get()
        referencia
                .addOnSuccessListener {document ->
                    //se sube las imagenes con los nombes

                    Log.i("firebase-firestore","${document.id} => ${document.data}")
                    val grupo = document.toObject(FirestoreGrupoDTO::class.java)
                    grupo?.uid = document.id
                    //aqui hago la agregacion de asignacion
                    val nuevaAsignacion = hashMapOf<String,Any?>(
                            "fechaAsignacion" to Timestamp(System.currentTimeMillis()),
                            "grupo" to FirestoreGrupoAsignacionDTO(grupo?.uid!!,grupo.nombre,grupo.juego?.uid!!,grupo.juego?.nombre!!,grupo.juego?.foto!!),
                            "usuario" to FirestoreUsuarioAsignacionDTO(AuthUsuario.infUsuario?.uid.toString(),AuthUsuario.infUsuario?.name.toString(),AuthUsuario.infUsuario?.fotoPerfil.toString())
                    )
                    db.collection("asignacion").document()
                            .set(nuevaAsignacion).addOnSuccessListener {
                                Toast.makeText(this, "${this.getString(R.string.nuevo_asignacion_creado_exito)}", Toast.LENGTH_SHORT).show()

                                //irActividad(ActividadMenuPrincipal::class.java)

                                val parametros = arrayListOf<Pair<String,*>>(
                                        Pair("grupoid",grupoUID)
                                )
                                irActividad(ActividadPerfilGrupo::class.java,parametros)
                            }.addOnFailureListener {

                            }

                    //includesForUploadFilesPerfil(imgGrupo)
                    //AuthUsuario.infUsuario = FirestoreInfoUsuarioDTO("",correo,descripcion,fechaNac,fotoPerfil,fotoFondo,name)
                    //Toast.makeText(this, "Succes Creation!! \nPlease wait.", Toast.LENGTH_SHORT).show()
                    irActividad(ActividadMenuPrincipal::class.java)
                }
                .addOnFailureListener{
                    //Toast.makeText(this, "Fallo al crear\nIntente denuevo", Toast.LENGTH_SHORT).show()
                }
    }



    //utilizada solo para activar el boton new publication
    @SuppressLint("ResourceAsColor")
    fun validacionPertenece(){
        //si pertenece debe tener su asignacion
        //si no pertenece se le creara
        val db = Firebase.firestore
        val referencia = db.collection("asignacion")
        val listaAsignacionesAux = arrayListOf<FirestoreAsignacionDTO>()
        referencia
            .whereEqualTo("grupo.uid", grupoUID)
            .whereEqualTo("usuario.uid",AuthUsuario.infUsuario?.uid.toString())
            .get()
            .addOnSuccessListener {
                for (document in it){
                    val asignacion = document.toObject(FirestoreAsignacionDTO::class.java)
                    asignacion.uid=document.id
                    listaAsignacionesAux.add(asignacion)
                }
                if(listaAsignacionesAux.isEmpty()){
                    Log.i("asignacion verificacion", "El usuario no existe")
                    Toast.makeText(this,"${this.getString(R.string.asignacion_no_existe_validacion)}", Toast.LENGTH_LONG).show()
                    val parametros = arrayListOf<Pair<String,*>>(
                        Pair("grupoid",grupoUID)
                    )
                    //irActividad(ActividadPerfilGrupo::class.java,parametros)
                }else{
                    val btnAccountPublicationColor = findViewById<TextView>(R.id.tv_publications_group_color)
                    val btnAccountLibraryColor = findViewById<TextView>(R.id.tv_new_publication_group_color)
                    btnAccountPublicationColor?.setBackgroundColor(0)
                    btnAccountLibraryColor?.setBackgroundColor(R.color.black)
                    crearFragmento(FragmentNewPublicationGroup())
                    Log.i("asignacion verificacion", "El usuario existe")

                }
            }.addOnFailureListener {
                Log.i("asignacion verificacion", "El usuario no existe error")
                //Toast.makeText(this,"USER DONT EXIST IN GROUP", Toast.LENGTH_LONG).show()
            }
    }




    fun cargarGrupoUID(uid:String){
        Log.i("firebase-firestore","XXXXXX ESTA ENTRANDO A CARGAR con : ${uid}")
        val db = Firebase.firestore
        var grupoBuscado: FirestoreGrupoDTO? = null
        val referencia = db.collection("grupo")
        referencia.document(uid)
            .get()
            .addOnSuccessListener { document->

                Log.i("firebase-firestore","XXXXXX ${document.data}  ID${document.id}")
                grupoBuscado = document.toObject(FirestoreGrupoDTO::class.java)
                grupoBuscado!!.uid=uid

                grupoVisita = grupoBuscado as FirestoreGrupoDTO

                cargarPerfil(grupoBuscado!!)

            }.addOnFailureListener{
                Log.i("firebase-firestore","Failure : ${it}")
            }
    }


    fun cargarPerfil(grupo: FirestoreGrupoDTO){

        descargarImagen(grupo.imagen,findViewById<ImageView>(R.id.iv_visit_group_background))
        descargarImagen(grupo.juego?.foto!!,findViewById<ImageView>(R.id.iv_visit_group_profile_picture))
        findViewById<TextView>(R.id.tv_visit_group_name).text=grupo.nombre
        findViewById<TextView>(R.id.tv_visit_group_description).text=grupo.descripcion
        findViewById<TextView>(R.id.tv_visit_group_game_name).text=grupo.juego?.nombre
        //---------
        iniciarHeaderFragmentoGroups()
        crearFragmento(FragmentGroupPublicaciones())
    }
    @SuppressLint("ResourceAsColor")
    fun iniciarHeaderFragmentoGroups(){
        val btnAccountPublication = findViewById<LinearLayout>(R.id.ll_publications_group)
        val btnNewPublication = findViewById<LinearLayout>(R.id.ll_new_publication_group)
        val btnAccountPublicationColor = findViewById<TextView>(R.id.tv_publications_group_color)
        val btnAccountLibraryColor = findViewById<TextView>(R.id.tv_new_publication_group_color)
        btnAccountPublicationColor?.setBackgroundColor(R.color.black)
        btnAccountLibraryColor?.setBackgroundColor(0)

        btnAccountPublication?.setOnClickListener{
            btnAccountPublicationColor?.setBackgroundColor(R.color.black)
            btnAccountLibraryColor?.setBackgroundColor(0)
            //crearFragmento(FragmentAccountPublication())
            Log.i("recyclerView inicio","esi esta entrando ")
            //crearFragmento()
            //iniciarRecyclerViewPublicaciones()
            crearFragmento(FragmentGroupPublicaciones())
        }
        btnNewPublication?.setOnClickListener{
            //me dejara seleccionar en el caso que el usuario pertenezca la grupo
            validacionPertenece()

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

    }/*
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
            .whereEqualTo("libreria.uidUsuario", grupoVisita.uid)
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

    }*/

    fun crearFragmento(fragmento: Fragment){
        val fragmentManager = supportFragmentManager
        //Transacciones
        val fragmentTransaction = fragmentManager.beginTransaction()
        //Crear instancia de fragmento
        val primerFragmento = fragmento
        //val primerFragment=PrimerFragment()
        val argumentos = Bundle()
        argumentos.putString("grupoUID",grupoUID)
        primerFragmento.arguments=argumentos

        //Añadir Fragmento
        fragmentTransaction.replace(R.id.rl_perfil_grupo,primerFragmento)
        //fragmentoActual = primerFragmento
        fragmentTransaction.commit()
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val publicacionSelec = arregloPublicaciones.get(item.groupId)
        return when(item?.itemId){
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