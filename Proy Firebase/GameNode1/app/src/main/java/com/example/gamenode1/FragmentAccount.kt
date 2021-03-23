package com.example.gamenode1

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.util.ArrayList

class FragmentAccount  : Fragment() {
    lateinit var fragmentoActual: Fragment
    lateinit var fragmentoPrincipal: Fragment
    //Asociamos el layout
    lateinit var storage: FirebaseStorage
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_account, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        storage = Firebase.storage

        fragmentoPrincipal = FragmentAccountPublication()
        iniciarHeaderFragmentoAccount()

        // Iniciamos los valores
        cargarInfPerfil()

        crearFragmento(FragmentAccountPublication())

        val btnWhereAmi = getView()?.findViewById<LinearLayout>(R.id.ll_account_location_feed)
        //ActividadSeleccionarUbicacion
        btnWhereAmi?.setOnClickListener{
            irActividad(ActividadSeleccionarUbicacion::class.java)
        }
    }
    fun cargarInfPerfil(){
        val a = AuthUsuario.infUsuario?.fotoPerfil
        if (a != null) {
            descargarImagenPerfil(a)
        }
        val b = AuthUsuario.infUsuario?.fotoFondo
        if (b != null) {
            descargarImagenPortada(b)
        }
        //tv_account_name
        val tvNameProfile = getView()?.findViewById<TextView>(R.id.tv_account_name)
        tvNameProfile?.text=AuthUsuario.infUsuario?.name
        val tvDescriptionProfile = getView()?.findViewById<TextView>(R.id.tv_account_description)
        tvDescriptionProfile?.text=AuthUsuario.infUsuario?.descripcion
        //------------------------------------------------------
        val btnMenuOpciones = getView()?.findViewById<ImageView>(R.id.iv_account_profile_menu)
        btnMenuOpciones?.setOnClickListener{
            val popupMenu = PopupMenu(this.context,it)
            popupMenu.setOnMenuItemClickListener {item->
                when (item.itemId) {
                    R.id.menu_usuario_opcion_editar_perfil -> {
                        Log.i("seleccion","editar perfil")
                        irActividad(ActividadEditarUsuario::class.java)
                        true
                    }
                    R.id.menu_usuario_opcion_crear_juego -> {
                        Log.i("seleccion","crear juego")
                        irActividad(ActividadCrearJuego::class.java)
                        true
                    }
                    R.id.menu_usuario_opcion_crear_grupo -> {
                        Log.i("seleccion","crear grupo")
                        irActividad(ActividadCrearGrupo::class.java)
                        true
                    }
                    //menu_usuario_opcion_exit
                    R.id.menu_usuario_opcion_exit -> {
                        Log.i("seleccion","crear grupo")
                        irActividad(MainActivity::class.java)
                        true
                    }
                    else -> false
                }
            }
            popupMenu.inflate(R.menu.menu_opciones_usuario)
            popupMenu.show()
        }
        val textLocation = getView()?.findViewById<TextView>(R.id.tv_account_location)
        if (AuthUsuario.locacion==null){
            textLocation?.setText("${this.getString(R.string.fragment_account_ubicacion_actual)}") //fragment_account_ubicacion_actual
        }else{
            textLocation?.setText(AuthUsuario.locacion.toString())
            textLocation?.setTextSize(1,10.0.toFloat())
        }
    }

    fun crearFragmento(fragment: Fragment){
        val fragmentManager = getActivity()?.getSupportFragmentManager()
        //Transacciones
        val fragmentTransaction = fragmentManager?.beginTransaction()
        //Crear instancia de fragmento
        val primerFragmento = fragment
        //val primerFragment=PrimerFragment()
        val argumentos = Bundle()
        argumentos.putString("nombre","ramses")
        argumentos.putInt("edad",20)
        primerFragmento.arguments=argumentos

        //Añadir Fragmento
        fragmentTransaction?.replace(R.id.rl_fragment_account,primerFragmento)
        fragmentoPrincipal = primerFragmento
        fragmentTransaction?.commit()
    }


    @SuppressLint("ResourceAsColor")
    fun iniciarHeaderFragmentoAccount(){
        val btnAccountPublication = getView()?.findViewById<LinearLayout>(R.id.ll_publications_group)
        val btnAccountLibrary = getView()?.findViewById<LinearLayout>(R.id.ll_new_publication_group)
        val btnAccountPublicationColor = getView()?.findViewById<TextView>(R.id.tv_publications_group_color)
        val btnAccountLibraryColor = getView()?.findViewById<TextView>(R.id.tv_new_publication_group_color)
        btnAccountPublicationColor?.setBackgroundColor(R.color.black)
        btnAccountLibraryColor?.setBackgroundColor(0)

        btnAccountPublication?.setOnClickListener{
            btnAccountPublicationColor?.setBackgroundColor(R.color.black)
            btnAccountLibraryColor?.setBackgroundColor(0)
            crearFragmento(FragmentAccountPublication())
        }
        btnAccountLibrary?.setOnClickListener{
            btnAccountPublicationColor?.setBackgroundColor(0)
            btnAccountLibraryColor?.setBackgroundColor(R.color.black)
            crearFragmento(FragmentAccountLibrary())
            Log.i("libreria","este no es")
        }



    }

    fun descargarImagenPerfil(nameImageAccount:String) {

        //gs://moviles-computacion-gamenodepa.appspot.com/20210312_003235Fondo.jpg
        val gsReference = storage.getReferenceFromUrl("gs://moviles-computacion-gamenodepa.appspot.com/${nameImageAccount}.jpg")
        var bitmap: Bitmap? = null
        // 1024 * 1024 * 5 800000

        gsReference.getBytes(1024 * 1024 * 5).addOnSuccessListener {
            bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            Log.i("image","se obtuvo la imagen perfil")

            val imageView = getView()?.findViewById<ImageView>(R.id.iv_publication_comment_profile_picture)
            imageView?.setImageBitmap(bitmap)

        }.addOnFailureListener{

        }

    }
    fun descargarImagenPortada(nameImageAccount:String) {

        //gs://moviles-computacion-gamenodepa.appspot.com/20210312_003235Fondo.jpg iv_account_profile_picture
        val gsReference = storage.getReferenceFromUrl("gs://moviles-computacion-gamenodepa.appspot.com/${nameImageAccount}.jpg")
        var bitmap: Bitmap? = null

        gsReference.getBytes(1024 * 1024 * 5).addOnSuccessListener {
            bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            Log.i("image","se obtuvo la imagen fondo")

            val imageView = getView()?.findViewById<ImageView>(R.id.iv_account_background)
            imageView?.setImageBitmap(bitmap)

        }.addOnFailureListener{

        }

    }

    fun irActividad(
        clase: Class<*>,
        parametros: ArrayList<Pair<String,*>>? = null,
        codigo:Int? = null
    ){
        val intentExplicito = android.content.Intent(
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