package com.example.gamenode1

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamenode1.dto.FirestoreAsignacionDTO
import com.example.gamenode1.dto.FirestorePublicacionDTO
import com.example.gamenode1.dto.FirestorePublicacionGrupoDTO
import com.facebook.FacebookSdk
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FragmentHome : Fragment() {
    //Asociamos el layout
    var fechaPub:com.google.firebase.Timestamp?=null
    var auxSel = 1

    val arregloPublicaciones = arrayListOf<FirestorePublicacionDTO>()
    val arregloPublicacionesGr = arrayListOf<FirestorePublicacionGrupoDTO>()
    val gruposDelUsuarioActual = ArrayList<String>()
    private var rvPublicaciones: RecyclerView?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvPublicaciones=getView()?.findViewById(R.id.rv_groups)
        iniciarHeaderFragmentoHome()
        auxSel=1
        iniciarRecyclerView()
        val btnAgregar = getView()?.findViewById<Button>(R.id.fabiii)
        btnAgregar?.setOnClickListener{
            if(auxSel==1){
                agregarPublicacionRecyclerView1()

            }else{
                agregarPublicacionRecyclerView2()
            }
        }
    }
    @SuppressLint("ResourceAsColor")
    fun iniciarHeaderFragmentoHome(){
        val btnHomeGeneral = getView()?.findViewById<LinearLayout>(R.id.ll_groups_op_my_groups)
        val btnHomeGroups = getView()?.findViewById<LinearLayout>(R.id.ll_groups_op_new_groups)
        val btnHomeGeneralColor = getView()?.findViewById<TextView>(R.id.tv_groups_op_my_groups_color)
        val btnHomeGroupsColor = getView()?.findViewById<TextView>(R.id.tv_groups_op_new_groups_color)
        btnHomeGeneralColor?.setBackgroundColor(R.color.black)
        btnHomeGroupsColor?.setBackgroundColor(0)

        btnHomeGeneral?.setOnClickListener{
            btnHomeGeneralColor?.setBackgroundColor(R.color.black)
            btnHomeGroupsColor?.setBackgroundColor(0)
            auxSel=1
            iniciarRecyclerView()
        }
        btnHomeGroups?.setOnClickListener{
            btnHomeGeneralColor?.setBackgroundColor(0)
            btnHomeGroupsColor?.setBackgroundColor(R.color.black)
            obtenerGruposUsuario()

        }
    }

    fun iniciarRecyclerView(){
        val adapterPublicaciones = ListAdapterPublicacion()
        adapterPublicaciones.setContext(this.requireContext())
        val recyclerViewPokemon = getView()?.findViewById<RecyclerView>(R.id.rv_groups)
        recyclerViewPokemon?.adapter = adapterPublicaciones
        recyclerViewPokemon?.layoutManager = LinearLayoutManager(FacebookSdk.getApplicationContext())

        //--------------------------------------------

        val db = Firebase.firestore

        val referencia = db.collection("publicacion")

        referencia
                //.whereEqualTo("libreria.uidUsuario", AuthUsuario.infUsuario!!.uid)
                .orderBy("fechaPublicacion", Query.Direction.DESCENDING)
                .limit(2)
                //.whereEqualTo("usuario", FirestoreUsuarioLibreriaDTO(AuthUsuario.infUsuario!!.uid,AuthUsuario.infUsuario!!.name,AuthUsuario.infUsuario!!.fotoPerfil))
                //.where("regions", "array-contains", "west_coast");
                //.whereArrayContains("usuario", AuthUsuario.infUsuario!!.uid)
                .get()

                .addOnSuccessListener {
                    Log.i("firebase-firestore","---------$it")
                    val publicacionLast = it.last().toObject(FirestorePublicacionDTO::class.java)
                    Log.i("firebase-firestorecon","---------last ${publicacionLast.uid}")
                    for(document in it){
                        Log.i("firebase-firestore","${document.id} => ${document.data}")
                        val publicacion = document.toObject(FirestorePublicacionDTO::class.java)
                        publicacion.uid = document.id
                        arregloPublicaciones.add(publicacion)
                        fechaPub=publicacion.fechaPublicacion
                        Log.i("firebase-firestoreco con","-------1--$fechaPub")
                        adapterPublicaciones.notifyDataSetChanged()
                    }

                }
                .addOnFailureListener{
                    Log.i("firebase-firestore","No encontro nada")
                }
        adapterPublicaciones.setData(arregloPublicaciones)

    }




    //--------------------------------------------------------------------------------------------------
    fun agregarPublicacionRecyclerView1(){
        val adapterPublicaciones = ListAdapterPublicacion()
        adapterPublicaciones.setContext(this.requireContext())
        val recyclerViewPokemon = getView()?.findViewById<RecyclerView>(R.id.rv_groups)
        recyclerViewPokemon?.adapter = adapterPublicaciones
        recyclerViewPokemon?.layoutManager = LinearLayoutManager(FacebookSdk.getApplicationContext())

        //--------------------------------------------

        val db = Firebase.firestore

        val referencia = db.collection("publicacion")

        referencia
                //.whereEqualTo("libreria.uidUsuario", AuthUsuario.infUsuario!!.uid)
                //.orderBy("fechaPublicacion", Query.Direction.DESCENDING)
                .orderBy("fechaPublicacion", Query.Direction.DESCENDING)
                .startAfter(fechaPub)
                //.startAfter(uidPub)
                .limit(2)
                //.whereEqualTo("usuario", FirestoreUsuarioLibreriaDTO(AuthUsuario.infUsuario!!.uid,AuthUsuario.infUsuario!!.name,AuthUsuario.infUsuario!!.fotoPerfil))
                //.where("regions", "array-contains", "west_coast");
                //.whereArrayContains("usuario", AuthUsuario.infUsuario!!.uid)
                .get()

                .addOnSuccessListener {
                    Log.i("firebase-firestore","---------$it")
                    val publicacionLast = it.last().toObject(FirestorePublicacionDTO::class.java)
                    Log.i("firebase-firestorecon","---------last ${publicacionLast.uid}")
                    for(document in it){
                        Log.i("firebase-firestore","${document.id} => ${document.data}")
                        val publicacion = document.toObject(FirestorePublicacionDTO::class.java)
                        publicacion.uid = document.id
                        arregloPublicaciones.add(publicacion)
                        fechaPub=publicacion.fechaPublicacion
                        Log.i("firebase-firestorEcon","---------${fechaPub}")
                        adapterPublicaciones.notifyDataSetChanged()
                    }

                }
                .addOnFailureListener{
                    Log.i("firebase-firestore","No encontro nada")
                }
        adapterPublicaciones.setData(arregloPublicaciones)

    }





    //--------------------------------------------------------------------------------------------------
    fun iniciarRecyclerView2(){
        val adapterPublicaciones = ListAdapterGroupPublication()
        adapterPublicaciones.setContext(this.requireContext())
        val recyclerViewPokemon = getView()?.findViewById<RecyclerView>(R.id.rv_groups)
        recyclerViewPokemon?.adapter = adapterPublicaciones
        recyclerViewPokemon?.layoutManager = LinearLayoutManager(FacebookSdk.getApplicationContext())

        //--------------------------------------------

        val db = Firebase.firestore

        val referencia = db.collection("publicacionGrupo")
        referencia
                .whereIn("asignacion.grupoUid",gruposDelUsuarioActual)
                .orderBy("fechaAgregacionPublicacion", Query.Direction.DESCENDING)
                .limit(1)
                //gruposDelUsuarioActual
                //.whereEqualTo("asignacion.grupoUid", groupUid)
                //.orderBy("fechaAgregacionComentario", Query.Direction.DESCENDING)
                //.whereEqualTo("usuario", FirestoreUsuarioLibreriaDTO(AuthUsuario.infUsuario!!.uid,AuthUsuario.infUsuario!!.name,AuthUsuario.infUsuario!!.fotoPerfil))
                //.where("regions", "array-contains", "west_coast");
                //.whereArrayContains("usuario", AuthUsuario.infUsuario!!.uid)
                .get()

                .addOnSuccessListener {
                    Log.i("firebase-firestore","---------$it")
                    for(document in it){
                        Log.i("firebase-firestore","${document.id} => ${document.data}")
                        val publicacion = document.toObject(FirestorePublicacionGrupoDTO::class.java)
                        Log.i("firebase-firestore-object"," El comentario es : ${publicacion.descripcion}")
                        publicacion.uid = document.id
                        arregloPublicacionesGr.add(publicacion)
                        fechaPub=publicacion.fechaAgregacionPublicacion
                        adapterPublicaciones.notifyDataSetChanged()
                    }
                }
                .addOnFailureListener{
                    Log.i("firebase-firestore","No encontro nada")
                }
        adapterPublicaciones.setData(arregloPublicacionesGr)

    }
//---------------------------------------------------------


    fun agregarPublicacionRecyclerView2(){
        val adapterPublicaciones = ListAdapterGroupPublication()
        adapterPublicaciones.setContext(this.requireContext())
        val recyclerViewPokemon = getView()?.findViewById<RecyclerView>(R.id.rv_groups)
        recyclerViewPokemon?.adapter = adapterPublicaciones
        recyclerViewPokemon?.layoutManager = LinearLayoutManager(FacebookSdk.getApplicationContext())

        //--------------------------------------------

        val db = Firebase.firestore

        val referencia = db.collection("publicacionGrupo")
        referencia
                .whereIn("asignacion.grupoUid",gruposDelUsuarioActual)
                .orderBy("fechaAgregacionPublicacion", Query.Direction.DESCENDING)
                .startAfter(fechaPub)
                .limit(1)
                //gruposDelUsuarioActual
                //.whereEqualTo("asignacion.grupoUid", groupUid)
                //.orderBy("fechaAgregacionComentario", Query.Direction.DESCENDING)
                //.whereEqualTo("usuario", FirestoreUsuarioLibreriaDTO(AuthUsuario.infUsuario!!.uid,AuthUsuario.infUsuario!!.name,AuthUsuario.infUsuario!!.fotoPerfil))
                //.where("regions", "array-contains", "west_coast");
                //.whereArrayContains("usuario", AuthUsuario.infUsuario!!.uid)
                .get()

                .addOnSuccessListener {
                    Log.i("firebase-firestore","---------$it")
                    for(document in it){
                        Log.i("firebase-firestore","${document.id} => ${document.data}")
                        val publicacion = document.toObject(FirestorePublicacionGrupoDTO::class.java)
                        Log.i("firebase-firestore-object"," El comentario es : ${publicacion.descripcion}")
                        publicacion.uid = document.id
                        arregloPublicacionesGr.add(publicacion)
                        fechaPub=publicacion.fechaAgregacionPublicacion
                        adapterPublicaciones.notifyDataSetChanged()
                    }
                }
                .addOnFailureListener{
                    Log.i("firebase-firestore","No encontro nada")
                }
        adapterPublicaciones.setData(arregloPublicacionesGr)

    }






//--------------------------------------------------------------


    @SuppressLint("ResourceAsColor")
    fun obtenerGruposUsuario(){
        val db = Firebase.firestore
        val referencia = db.collection("asignacion")
        referencia
                .whereEqualTo("usuario.uid", AuthUsuario.infUsuario!!.uid)
                .get()
                .addOnSuccessListener {
                    Log.i("firebase-firestore","---------$it")
                    for(document in it){
                        Log.i("firebase-firestore","${document.id} => ${document.data}")
                        val asignacion = document.toObject(FirestoreAsignacionDTO::class.java)
                        asignacion.uid = document.id
                        gruposDelUsuarioActual.add(asignacion.grupo?.uid!!)
                        //aqui podriamos añadirlos
                        referencia.document().get().addOnSuccessListener {

                        }.addOnFailureListener {

                        }
                    }
                    if(gruposDelUsuarioActual.isEmpty()){

                        val btnHomeGeneralColor = getView()?.findViewById<TextView>(R.id.tv_groups_op_my_groups_color)
                        val btnHomeGroupsColor = getView()?.findViewById<TextView>(R.id.tv_groups_op_new_groups_color)
                        btnHomeGeneralColor?.setBackgroundColor(R.color.black)
                        btnHomeGroupsColor?.setBackgroundColor(0)
                        Toast.makeText(this.requireContext(),"the user dont have groups", Toast.LENGTH_LONG).show()

                    }else{
                        auxSel=2
                        iniciarRecyclerView2()
                    }
                }
                .addOnFailureListener{
                    Log.i("firebase-firestore","No encontro nada")
                }
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
        val builder = AlertDialog.Builder(this.requireContext())

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
            builder.setMessage("${this.getString(R.string.mensaje_seguridad_eliminar)}")
            builder.create().show()
        }

    }
    fun eliminarPublicacion(publicacion: FirestorePublicacionDTO){
        var uidEliminar = publicacion.uid
        val db = Firebase.firestore

        db.collection("publicacion").document(uidEliminar).delete()
            .addOnSuccessListener {
                Log.i("firebase-firestore-eliminacion","SE ELIMINO")
                Toast.makeText(this.requireContext(),"${this.getString(R.string.mensaje_eliminar_publicacion_exito)}", Toast.LENGTH_LONG).show()

                //---------------------------------------------------------------------
                db.collection("comentarioPublicacion").
                whereEqualTo("publicacion.uid",uidEliminar)
                    .get()
                    .addOnSuccessListener {
                        for(document in it){
                            db.collection("comentarioPublicacion").document(document.id).delete()
                        }
                        Toast.makeText(this.requireContext()," ${this.getString(R.string.mensaje_eliminar_publicacion_exito_comentarios)}", Toast.LENGTH_LONG).show()
                    }.addOnFailureListener {

                    }
                //---------------------------------------------------------------------------

                irActividad(ActividadMenuPrincipal::class.java)

            }.addOnFailureListener{

                Log.i("firebase-firestore-eliminacion","NO SE ELIMINO")
                //Toast.makeText(this.requireContext(),"Deleted fail", Toast.LENGTH_LONG).show()
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
}