package com.example.gamenode1

import android.app.DownloadManager
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamenode1.dto.FirestoreLibreriaDTO
import com.facebook.FacebookSdk.getApplicationContext
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.firestore.Query

class FragmentAccountLibrary : Fragment() {

    val arregloVideojuegos = arrayListOf<FirestoreLibreriaDTO>()
    lateinit var fragmentoPrincipal: Fragment
    lateinit var storage: FirebaseStorage

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_account_library_list, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        iniciarRecyclerView()


        //ll_account_library_add_new_library
        val llAddNewLibrary = getView()?.findViewById<LinearLayout>(R.id.ll_account_library_add_new_library)
        llAddNewLibrary?.setOnClickListener{
            Log.i("libreria","valio")
            //crearFragmento(FragmentAccountNewLibrary())
           // crearFragmento(FragmentAccountNewLibrary())
            irActividad(ActividadAgregarJuegoBiblioteca::class.java)
        }

        //------------------------------------------------------

    }

    fun iniciarRecyclerView(){
        val adapterJuegosLibreria = ListAdapterLibreria()
        adapterJuegosLibreria.setContext(this.requireContext())
        val recyclerViewPokemon = getView()?.findViewById<RecyclerView>(R.id.rv_account_library)
        recyclerViewPokemon?.adapter = adapterJuegosLibreria
        recyclerViewPokemon?.layoutManager = LinearLayoutManager(getApplicationContext())

        //--------------------------------------------

        val db = Firebase.firestore

        val referencia = db.collection("libreria")
        //fechaAgregacion
        referencia
                .whereEqualTo("usuario.uid", AuthUsuario.infUsuario!!.uid)
                //.orderBy("usuario.uid")
                .orderBy("fechaAgregacion",Query.Direction.DESCENDING)
            //.whereEqualTo("usuario.uid", AuthUsuario.infUsuario!!.uid)
            //.whereEqualTo("usuario", FirestoreUsuarioLibreriaDTO(AuthUsuario.infUsuario!!.uid,AuthUsuario.infUsuario!!.name,AuthUsuario.infUsuario!!.fotoPerfil))
            //.where("regions", "array-contains", "west_coast");
            //.whereArrayContains("usuario", AuthUsuario.infUsuario!!.uid)
            .get()

            .addOnSuccessListener {
                Log.i("firebase-firestore","---------$it")
                for(document in it){
                    Log.i("firebase-firestore","${document.id} => ${document.data}")
                    val juegoLibreria = document.toObject(FirestoreLibreriaDTO::class.java)
                    juegoLibreria.uid = document.id
                    arregloVideojuegos.add(juegoLibreria)
                    adapterJuegosLibreria.notifyDataSetChanged()
                }
            }
            .addOnFailureListener{
                Log.i("firebase-firestore","No encontro nada")
            }
        adapterJuegosLibreria.setData(arregloVideojuegos)

        // UserViewModel
       /* mmCapturaViewModel = ViewModelProvider(this).get(CapturaViewModel::class.java)
        mPokemonViewModel = ViewModelProvider(this).get(PokemonViewModel::class.java)
        mPokemonViewModel.getPokemonList().observe(this, Observer {
                listaPokemon ->
            adapterPokemon.setData(listaPokemon)
        })*/

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