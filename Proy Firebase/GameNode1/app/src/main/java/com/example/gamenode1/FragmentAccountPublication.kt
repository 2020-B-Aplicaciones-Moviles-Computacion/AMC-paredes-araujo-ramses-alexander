package com.example.gamenode1

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
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamenode1.dto.FirestorePublicacionDTO
import com.facebook.FacebookSdk
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class FragmentAccountPublication : Fragment() {
    val arregloPublicaciones = arrayListOf<FirestorePublicacionDTO>()

    lateinit var storage: FirebaseStorage
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_account_publications_list, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniciarRecyclerView()
        //------------------------------------------------------
        val llAddNewLibrary = getView()?.findViewById<LinearLayout>(R.id.ll_account_publication_add_new_publication)
        llAddNewLibrary?.setOnClickListener{
            Log.i("libreria","valio")
            //crearFragmento(FragmentAccountNewLibrary())
            // crearFragmento(FragmentAccountNewLibrary())
            irActividad(ActividadAgregarNuevaPublicacion::class.java)
        }

    }

    fun iniciarRecyclerView(){
        val adapterPublicaciones = ListAdapterPublicacion()
        adapterPublicaciones.setContext(this.requireContext())
        val recyclerViewPokemon = getView()?.findViewById<RecyclerView>(R.id.rv_account_publications)
        recyclerViewPokemon?.adapter = adapterPublicaciones
        recyclerViewPokemon?.layoutManager = LinearLayoutManager(FacebookSdk.getApplicationContext())

        //--------------------------------------------

        val db = Firebase.firestore

        val referencia = db.collection("publicacion")

        referencia
            .whereEqualTo("libreria.uidUsuario", AuthUsuario.infUsuario!!.uid)
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
                    publicacion.uid = document.id
                    arregloPublicaciones.add(publicacion)
                    adapterPublicaciones.notifyDataSetChanged()
                }
            }
            .addOnFailureListener{
                Log.i("firebase-firestore","No encontro nada")
            }
        adapterPublicaciones.setData(arregloPublicaciones)

    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val publicacionSelec = arregloPublicaciones.get(item.groupId)
        return when(item?.itemId){
            111 ->{

                val parametros = arrayListOf<Pair<String,*>>(
                    Pair("publicacionSelec",publicacionSelec.uid)
                )
                irActividad(ActividadAgregarComentarioPublicacion::class.java,parametros)

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

            builder.setPositiveButton("YES"){_,_ ->
                //aqui va el borrado
                eliminarPublicacion(publicacionSelec)

            }
            builder.setNegativeButton("NO"){_,_->

            }

            builder.setTitle("Delete publication")
            builder.setMessage("Are you sure?")
            builder.create().show()
        }else{

            builder.setPositiveButton("OK"){_,_ ->
                //aqui va el borrado

            }

            builder.setTitle("Delete publication")
            builder.setMessage("You are not the owner")
            builder.create().show()
        }

    }
    fun eliminarPublicacion(publicacion: FirestorePublicacionDTO){
        var uidEliminar = publicacion.uid
        val db = Firebase.firestore

        db.collection("publicacion").document(uidEliminar).delete()
            .addOnSuccessListener {
                Log.i("firebase-firestore-eliminacion","SE ELIMINO")
                Toast.makeText(this.requireContext(),"Deleted succefull", Toast.LENGTH_LONG).show()

                //---------------------------------------------------------------------
                db.collection("comentarioPublicacion").
                    whereEqualTo("publicacion.uid",uidEliminar)
                    .get()
                    .addOnSuccessListener {
                        for(document in it){
                            db.collection("comentarioPublicacion").document(document.id).delete()
                        }
                        Toast.makeText(this.requireContext(),"Deleted all the coments", Toast.LENGTH_LONG).show()
                    }.addOnFailureListener {

                    }
                //---------------------------------------------------------------------------

                irActividad(ActividadMenuPrincipal::class.java)

            }.addOnFailureListener{

                Log.i("firebase-firestore-eliminacion","NO SE ELIMINO")
                Toast.makeText(this.requireContext(),"Deleted fail", Toast.LENGTH_LONG).show()
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