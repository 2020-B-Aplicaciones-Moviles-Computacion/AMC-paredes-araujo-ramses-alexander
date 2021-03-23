package com.example.gamenode1

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamenode1.dto.FirestoreComentarioDTO
import com.example.gamenode1.dto.FirestoreGrupoDTO
import com.example.gamenode1.dto.FirestoreInfoUsuarioDTO
import com.facebook.FacebookSdk
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FragmentSearch : Fragment() {
    //Asociamos el layout
   /* val arregloComentarios = arrayListOf<FirestoreComentarioDTO>()
    private var rvPublicaciones: RecyclerView?=null*/
    val arregloUsuarios = arrayListOf<FirestoreInfoUsuarioDTO>()
    val arregloGrupos = arrayListOf<FirestoreGrupoDTO>()
    private var publicacionUid: String? = null
    private var opcionBusqueda = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("publicacion-id"," onCreate publicacionUid: ${publicacionUid}")
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_search, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        Log.i("publicacion-id"," onViewCreated publicacionUid: ${publicacionUid}")
        iniciarOpciones()
        cargarBarraSearch()

    }

    @SuppressLint("ResourceAsColor")
    fun iniciarOpciones(){
        val llUsers = getView()?.findViewById<LinearLayout>(R.id.ll_publications_group)
        val llgroups = getView()?.findViewById<LinearLayout>(R.id.ll_search_groups)
        val tvUsersColor = getView()?.findViewById<TextView>(R.id.tv_search_users_color)
        val tvGroupsColor = getView()?.findViewById<TextView>(R.id.tv_search_groups_color)
        tvUsersColor?.setBackgroundColor(R.color.black)
        tvGroupsColor?.setBackgroundColor(0)
        llUsers?.setOnClickListener {
            tvUsersColor?.setBackgroundColor(R.color.black)
            tvGroupsColor?.setBackgroundColor(0)
            opcionBusqueda = 1
        }
        llgroups?.setOnClickListener {
            tvUsersColor?.setBackgroundColor(0)
            tvGroupsColor?.setBackgroundColor(R.color.black)
            opcionBusqueda = 2
        }
    }
    fun cargarBarraSearch(){
        val etSearch = getView()?.findViewById<EditText>(R.id.et_search_searcher)
        etSearch?.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                // if the event is a key down event on the enter button
                Log.i("search- pre","${etSearch.text}")
                if (event.action == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    // perform action on key press
                    Log.i("search-","${etSearch.text}")
                    if(opcionBusqueda==1){
                        Log.i("search-","Buscando ${etSearch.text} en usuarios")
                        iniciarRecyclerViewUsuarios(etSearch.text.toString())
                    }else{
                        iniciarRecyclerViewUsuariosw(etSearch.text.toString())
                        Log.i("search-","Buscando ${etSearch.text} en grupos")

                    }


                    return true
                }
                return false
            }
        })
    }

    fun iniciarRecyclerViewUsuarios(busqueda:String){
        val adapterUsuarios = ListAdapterUsuarioSearch()
        adapterUsuarios.setContext(this.requireContext())
        val recyclerViewPokemon = getView()?.findViewById<RecyclerView>(R.id.rv_search)
        recyclerViewPokemon?.adapter = adapterUsuarios
        recyclerViewPokemon?.layoutManager = LinearLayoutManager(FacebookSdk.getApplicationContext())

        //--------------------------------------------
        arregloUsuarios.clear()
        //arregloUsuarios = arrayListOf<FirestoreInfoUsuarioDTO>()
        val db = Firebase.firestore

        val referencia = db.collection("infUsuario")
        referencia
            .orderBy("name").startAt(busqueda).endAt(busqueda+'\uf8ff')
                //ref.child('usuario').orderByChild('nombre').startAt(nombre).endAt(nombre+'\uf8ff')
                //ref.collection('usuario').orderBy('nombre').startAt(nombre).endAt(nombre+'\uf8ff'
            //.whereEqualTo("publicacion.uid", publicacionUid.toString())
            //.orderBy("fechaAgregacionComentario", Query.Direction.DESCENDING)
            //.whereEqualTo("usuario", FirestoreUsuarioLibreriaDTO(AuthUsuario.infUsuario!!.uid,AuthUsuario.infUsuario!!.name,AuthUsuario.infUsuario!!.fotoPerfil))
            //.where("regions", "array-contains", "west_coast");
            //.whereArrayContains("usuario", AuthUsuario.infUsuario!!.uid)
            .get()

            .addOnSuccessListener {
                Log.i("firebase-firestore","---------$it")
                for(document in it){
                    Log.i("firebase-firestore","${document.id} => ${document.data}")
                    val usuario = document.toObject(FirestoreInfoUsuarioDTO::class.java)
                    Log.i("firebase-firestore-object"," El comentario es : ${usuario.descripcion}")
                    usuario.uid = document.id
                    arregloUsuarios.add(usuario)
                    adapterUsuarios.notifyDataSetChanged()
                }
            }
            .addOnFailureListener{
                Log.i("firebase-firestore","No encontro nada")
            }
        Log.i("firebase-firestore  fin ","Se paso ${arregloUsuarios}")
        adapterUsuarios.setData(arregloUsuarios)

    }


    fun iniciarRecyclerViewUsuariosw(busqueda:String){
        val adapterGrupos = ListAdapterGrupoSearch()
        adapterGrupos.setContext(this.requireContext())
        val recyclerViewPokemon = getView()?.findViewById<RecyclerView>(R.id.rv_search)
        recyclerViewPokemon?.adapter = adapterGrupos
        recyclerViewPokemon?.layoutManager = LinearLayoutManager(FacebookSdk.getApplicationContext())

        //--------------------------------------------
        arregloGrupos.clear()
        //arregloUsuarios = arrayListOf<FirestoreInfoUsuarioDTO>()
        val db = Firebase.firestore

        val referencia = db.collection("grupo")
        referencia
                .orderBy("nombre").startAt(busqueda).endAt(busqueda+'\uf8ff')
                //ref.child('usuario').orderByChild('nombre').startAt(nombre).endAt(nombre+'\uf8ff')
                //ref.collection('usuario').orderBy('nombre').startAt(nombre).endAt(nombre+'\uf8ff'
                //.whereEqualTo("publicacion.uid", publicacionUid.toString())
                //.orderBy("fechaAgregacionComentario", Query.Direction.DESCENDING)
                //.whereEqualTo("usuario", FirestoreUsuarioLibreriaDTO(AuthUsuario.infUsuario!!.uid,AuthUsuario.infUsuario!!.name,AuthUsuario.infUsuario!!.fotoPerfil))
                //.where("regions", "array-contains", "west_coast");
                //.whereArrayContains("usuario", AuthUsuario.infUsuario!!.uid)
                .get()

                .addOnSuccessListener {
                    Log.i("firebase-firestore","---------$it")
                    for(document in it){
                        Log.i("firebase-firestore","${document.id} => ${document.data}")
                        val grupo = document.toObject(FirestoreGrupoDTO::class.java)
                        //Log.i("firebase-firestore-object"," El comentario es : ${usuario.descripcion}")
                        grupo.uid = document.id
                        arregloGrupos.add(grupo)
                        adapterGrupos.notifyDataSetChanged()
                    }
                }
                .addOnFailureListener{
                    Log.i("firebase-firestore","No encontro nada")
                }
        Log.i("firebase-firestore  fin ","Se paso ${arregloUsuarios[0].name}")
        adapterGrupos.setData(arregloGrupos)

    }
    override fun onContextItemSelected(item: MenuItem): Boolean {
        val usuarioSelect = arregloUsuarios.get(item.groupId)
        val grupoSelect = arregloGrupos.get(item.groupId)

        return when(item?.itemId){
            999 ->{

                val parametros = arrayListOf<Pair<String,*>>(
                    Pair("useruid",usuarioSelect.uid)
                )
                irActividad(ActividadPerfilInvitado::class.java,parametros)
                //irActividad(ActividadAgregarComentarioPublicacion::class.java,parametros)
                //irActividad(VistaEditarEntrenador::class.java,parametros)
                return true
            }
            888->{
                val parametros = arrayListOf<Pair<String,*>>(
                        Pair("grupoid",grupoSelect.uid)
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