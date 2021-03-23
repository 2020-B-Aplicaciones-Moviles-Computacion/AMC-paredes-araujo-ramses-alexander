package com.example.gamenode1.dto

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamenode1.ListAdapterGroupPublication
import com.example.gamenode1.R
import com.facebook.FacebookSdk
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FragmentGroupPublicaciones : Fragment() {

    /*
        var titulo:String ="",
        var descripcion:String="",
        var asignacion:FirestorePublicacionAsignacionDTO?=null,
        val fechaAgregacionPublicacion:com.google.firebase.Timestamp? = null*/
    //Asociamos el layout

    val arregloPublicacionesGr = arrayListOf<FirestorePublicacionGrupoDTO>()
    private var groupUid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            groupUid = it.getString("grupoUID")
        }
        Log.i("publicacion-id"," onCreate publicacionUid: ${groupUid}")
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_grupo_publlicaciones, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        Log.i("publicacion-id"," onViewCreated publicacionUid: ${groupUid}")
        iniciarRecyclerView()
    }
    fun iniciarRecyclerView(){
        val adapterPublicaciones = ListAdapterGroupPublication()
        adapterPublicaciones.setContext(this.requireContext())
        val recyclerViewPokemon = getView()?.findViewById<RecyclerView>(R.id.rv_publicaciones_grupo)
        recyclerViewPokemon?.adapter = adapterPublicaciones
        recyclerViewPokemon?.layoutManager = LinearLayoutManager(FacebookSdk.getApplicationContext())

        //--------------------------------------------

        val db = Firebase.firestore

        val referencia = db.collection("publicacionGrupo")
        referencia
                .whereEqualTo("asignacion.grupoUid", groupUid)
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
                        adapterPublicaciones.notifyDataSetChanged()
                    }
                }
                .addOnFailureListener{
                    Log.i("firebase-firestore","No encontro nada")
                }
        adapterPublicaciones.setData(arregloPublicacionesGr)

    }
}