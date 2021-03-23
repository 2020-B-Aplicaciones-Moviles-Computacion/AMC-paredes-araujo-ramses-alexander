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
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamenode1.dto.FirestoreAsignacionDTO
import com.example.gamenode1.dto.FirestoreGrupoDTO
import com.example.gamenode1.dto.FirestorePublicacionDTO
import com.facebook.FacebookSdk
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FragmentGroups: Fragment() {



    /*
    * busco en un array todos los grupos a los que pertenece el usuario
    * solo saco los id en un array aparte (ver documentacion)
    *
    * en el recycler view actual añadir una lista de los grupos a los que se pertenece*/

    //Asociamos el layout

    val arregloGrupos = arrayListOf<FirestoreGrupoDTO>()
    private var rvGroups: RecyclerView?=null
    val uidsGruposDelUsuarioActual = listOf<String>()

    val gruposDelUsuarioActual = ArrayList<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_groups, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvGroups=getView()?.findViewById(R.id.rv_groups)


        iniciarHeaderFragmentoHome()
        iniciarRecyclerViewFiltrado()
        //iniciarRecyclerViewFiltrado()
    }
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
            }
            .addOnFailureListener{
                Log.i("firebase-firestore","No encontro nada")
            }
    }
    @SuppressLint("ResourceAsColor")
    fun iniciarHeaderFragmentoHome(){
        val btnMyGroups = getView()?.findViewById<LinearLayout>(R.id.ll_groups_op_my_groups)
        val btnNewGroups = getView()?.findViewById<LinearLayout>(R.id.ll_groups_op_new_groups)
        val btnMyGroupsColor= getView()?.findViewById<TextView>(R.id.tv_groups_op_my_groups_color)
        val btnNewGroupsColor = getView()?.findViewById<TextView>(R.id.tv_groups_op_new_groups_color)
        btnMyGroupsColor?.setBackgroundColor(R.color.black)
        btnNewGroupsColor?.setBackgroundColor(0)

        btnMyGroups?.setOnClickListener{
            btnMyGroupsColor?.setBackgroundColor(R.color.black)
            btnNewGroupsColor?.setBackgroundColor(0)
            iniciarRecyclerViewFiltrado()
        }
        btnNewGroups?.setOnClickListener{
            btnMyGroupsColor?.setBackgroundColor(0)
            btnNewGroupsColor?.setBackgroundColor(R.color.black)

            iniciarRecyclerView()
        }
    }

    fun iniciarRecyclerViewFiltrado(){
        arregloGrupos.clear()
        val adapterGroups = ListAdapterGrupo()
        adapterGroups.setContext(this.requireContext())
        val recyclerViewPokemon = getView()?.findViewById<RecyclerView>(R.id.rv_groups)
        recyclerViewPokemon?.adapter = adapterGroups
        recyclerViewPokemon?.layoutManager = LinearLayoutManager(FacebookSdk.getApplicationContext())

        //--------------------------------------------

        val db = Firebase.firestore
        val referencia = db.collection("asignacion")
        referencia
            .whereEqualTo("usuario.uid", AuthUsuario.infUsuario!!.uid)
            //.orderBy("fechaPublicacion", Query.Direction.DESCENDING)
            //.whereEqualTo("usuario", FirestoreUsuarioLibreriaDTO(AuthUsuario.infUsuario!!.uid,AuthUsuario.infUsuario!!.name,AuthUsuario.infUsuario!!.fotoPerfil))
            //.where("regions", "array-contains", "west_coast");
            //.whereArrayContains("usuario", AuthUsuario.infUsuario!!.uid)
            .get()
            .addOnSuccessListener {
                Log.i("firebase-firestore","---------$it")
                for(document in it){
                    Log.i("firebase-firestore","${document.id} => ${document.data}")
                    val asignacion = document.toObject(FirestoreAsignacionDTO::class.java)
                    asignacion.uid = document.id
                    //gruposDelUsuarioActual.add(asignacion.grupo?.uid!!)
                    //aqui podriamos añadirlos
                    db.collection("grupo").document(asignacion.grupo?.uid!!).get().addOnSuccessListener { obj->
                        Log.i("firebase-firestore-entro","---------$obj")

                        val grupo = obj.toObject(FirestoreGrupoDTO::class.java)
                        Log.i("firebase-firestore-entro","---------$obj")
                        grupo?.uid = obj.id
                        arregloGrupos.add(grupo!!)
                        adapterGroups.notifyDataSetChanged()
                    }.addOnFailureListener {

                    }
                    //--------------------------------
                    //arregloPublicaciones.add(asignacion)
                }
            }
            .addOnFailureListener{
                Log.i("firebase-firestore","No encontro nada")
            }

        adapterGroups.setData(arregloGrupos)

    }

    fun iniciarRecyclerView(){
        arregloGrupos.clear()
        val adapterGroups = ListAdapterGrupo()
        adapterGroups.setContext(this.requireContext())
        val recyclerViewPokemon = getView()?.findViewById<RecyclerView>(R.id.rv_groups)
        recyclerViewPokemon?.adapter = adapterGroups
        recyclerViewPokemon?.layoutManager = LinearLayoutManager(FacebookSdk.getApplicationContext())

        //--------------------------------------------

        val db = Firebase.firestore

        val referencia = db.collection("grupo")

        referencia
            //.whereEqualTo("libreria.uidUsuario", AuthUsuario.infUsuario!!.uid)
            //.orderBy("fechaPublicacion", Query.Direction.DESCENDING)
            //.whereEqualTo("usuario", FirestoreUsuarioLibreriaDTO(AuthUsuario.infUsuario!!.uid,AuthUsuario.infUsuario!!.name,AuthUsuario.infUsuario!!.fotoPerfil))
            //.where("regions", "array-contains", "west_coast");
            //.whereArrayContains("usuario", AuthUsuario.infUsuario!!.uid)
            .get()

            .addOnSuccessListener {
                Log.i("firebase-firestore","---------$it")
                for(document in it){
                    Log.i("firebase-firestore","${document.id} => ${document.data}")
                    val grupo = document.toObject(FirestoreGrupoDTO::class.java)
                    grupo.uid = document.id
                    arregloGrupos.add(grupo)
                    Log.i("firebase-firestore-entro","---------$document")
                    adapterGroups.notifyDataSetChanged()
                }
            }
            .addOnFailureListener{
                Log.i("firebase-firestore","No encontro nada")
            }
        adapterGroups.setData(arregloGrupos)

    }
    override fun onContextItemSelected(item: MenuItem): Boolean {
        val grupoSelec = arregloGrupos.get(item.groupId)
        return when(item?.itemId){
            888 ->{

                val parametros = arrayListOf<Pair<String,*>>(
                    Pair("grupoid",grupoSelec.uid)
                )
                irActividad(ActividadPerfilGrupo::class.java,parametros)


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