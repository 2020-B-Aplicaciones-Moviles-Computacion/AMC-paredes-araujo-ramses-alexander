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
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamenode1.dto.FirestoreComentarioDTO
import com.example.gamenode1.dto.FirestorePublicacionDTO
import com.facebook.FacebookSdk
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FragmentPublicationComments : Fragment() {
    //Asociamos el layout
    val arregloComentarios = arrayListOf<FirestoreComentarioDTO>()
    private var rvPublicaciones: RecyclerView?=null
    private var publicacionUid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            publicacionUid = it.getString("publicacionUid")
        }
        Log.i("publicacion-id"," onCreate publicacionUid: ${publicacionUid}")
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_publication_comments, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        Log.i("publicacion-id"," onViewCreated publicacionUid: ${publicacionUid}")
        iniciarRecyclerView()
    }

    fun iniciarRecyclerView(){
        val adapterComentarios = ListAdapterComentario()
        adapterComentarios.setContext(this.requireContext())
        val recyclerViewPokemon = getView()?.findViewById<RecyclerView>(R.id.rv_publication_comments)
        recyclerViewPokemon?.adapter = adapterComentarios
        recyclerViewPokemon?.layoutManager = LinearLayoutManager(FacebookSdk.getApplicationContext())

        //--------------------------------------------

        val db = Firebase.firestore

        val referencia = db.collection("comentarioPublicacion")
        referencia
                .whereEqualTo("publicacion.uid", publicacionUid.toString())
                .orderBy("fechaAgregacionComentario", Query.Direction.DESCENDING)
                //.whereEqualTo("usuario", FirestoreUsuarioLibreriaDTO(AuthUsuario.infUsuario!!.uid,AuthUsuario.infUsuario!!.name,AuthUsuario.infUsuario!!.fotoPerfil))
                //.where("regions", "array-contains", "west_coast");
                //.whereArrayContains("usuario", AuthUsuario.infUsuario!!.uid)
                .get()

                .addOnSuccessListener {
                    Log.i("firebase-firestore","---------$it")
                    for(document in it){
                        Log.i("firebase-firestore","${document.id} => ${document.data}")
                        val comentario = document.toObject(FirestoreComentarioDTO::class.java)
                        Log.i("firebase-firestore-object"," El comentario es : ${comentario.descripcion}")
                        comentario.uid = document.id
                        arregloComentarios.add(comentario)
                        adapterComentarios.notifyDataSetChanged()
                    }
                }
                .addOnFailureListener{
                    Log.i("firebase-firestore","No encontro nada")
                }
        adapterComentarios.setData(arregloComentarios)

    }
    override fun onContextItemSelected(item: MenuItem): Boolean {
        val comentarioSelec = arregloComentarios.get(item.groupId)
        return when(item?.itemId){
            444 ->{

                val parametros = arrayListOf<Pair<String,*>>(
                    Pair("publicacionSelec",comentarioSelec.uid)
                )
                deleteComentario(comentarioSelec)
                //irActividad(ActividadAgregarComentarioPublicacion::class.java,parametros)
                //irActividad(VistaEditarEntrenador::class.java,parametros)
                return true
            }
            122->{
                /* if (pokemon != null) {
                     deletePokemon(pokemon)
                 }*/
                return true
            }
            else-> super.onContextItemSelected(item)
        }

    }


    private fun deleteComentario(comentarioSelec:FirestoreComentarioDTO){
        val builder = AlertDialog.Builder(this.requireContext())
        //var a:String = getText(R.string.op_si).toString()

        //-------------------------------------------------------


        if (comentarioSelec.usuario?.uid==AuthUsuario.infUsuario?.uid){

            builder.setPositiveButton("${this.getString(R.string.mensaje_si)}"){_,_ ->
                //aqui va el borrado
                eliminarComentario(comentarioSelec)

            }
            builder.setNegativeButton("${this.getString(R.string.mensaje_no)}"){_,_->

            }

            builder.setTitle("${this.getString(R.string.titulo_eliminar_comentario)}")
            builder.setMessage("${this.getString(R.string.mensaje_seguridad_eliminar)}")
            builder.create().show()
        }else{

            builder.setPositiveButton("${this.getString(R.string.mensaje_ok)}"){_,_ ->
                //aqui va el borrado

            }

            builder.setTitle("${this.getString(R.string.titulo_eliminar_comentario)}")
            builder.setMessage("${this.getString(R.string.mensaje_eliminar_fail_dueño)}")
            builder.create().show()
        }

        //------------------------------------------------------
    }
    fun eliminarComentario(comentario:FirestoreComentarioDTO){
        var uidEliminar = comentario.uid
        val db = Firebase.firestore

        db.collection("comentarioPublicacion").document(uidEliminar).delete()
            .addOnSuccessListener {
                Log.i("firebase-firestore-eliminacion","SE ELIMINO")
                Toast.makeText(this.requireContext(),"Deleted succefull",Toast.LENGTH_LONG).show()


                val parametros = arrayListOf<Pair<String,*>>(
                    Pair("publicacionSelec",comentario.publicacion?.uid)
                )
                irActividad(ActividadAgregarComentarioPublicacion::class.java,parametros)

            }.addOnFailureListener{

                Log.i("firebase-firestore-eliminacion","NO SE ELIMINO")
                Toast.makeText(this.requireContext(),"Deleted fail",Toast.LENGTH_LONG).show()
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