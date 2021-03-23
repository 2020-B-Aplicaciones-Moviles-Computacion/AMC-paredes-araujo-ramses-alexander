package com.example.gamenode1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.gamenode1.dto.FirestoreInfoUsuarioDTO
import com.example.gamenode1.dto.FirestoreUsuarioDTO
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import javax.xml.datatype.DatatypeConstants.SECONDS
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {

    val CODIGO_INICIO_SECION = 102
    lateinit var textoNoLogueado:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textoNoLogueado = this.getString(R.string.principal_inicia_sesion)
        cambiarTextoYBotones()
        val db = Firebase.firestore

        db.collection("users")
                .get()
                .addOnSuccessListener { result ->
                    iniciarBotones()
                    for (document in result) {
                        Log.i("firebase-muestra", "${document.id} => ${document.data}")
                    }
                }
                .addOnFailureListener { exception ->
                    iniciarBotones()
                    Log.i("firebase-muestra", "Error getting documents.", exception)
                }

    }
    fun iniciarBotones(){
        val buttonIngresoNormal = findViewById<Button>(R.id.btn_ingreso_normal)

        buttonIngresoNormal.setOnClickListener{

            //Validacion de si existe o no el usuario
            val instanciaAut = FirebaseAuth.getInstance()
            if (instanciaAut.currentUser!=null){
                //Verificar si el usuario es nuevo, buscando en la bse de datos de fire store
                //irActividad()
                Log.i("firebase-firestore-nuevo","Correo: "+ instanciaAut.currentUser?.email.toString())
                //irActividad(ActividadCrearUsuario::class.java)
//-----------------------------------------------------------------------------------------------------------------
                //nuevoCrearPerfil(correo:String): FirestoreInfoUsuarioDTO
                //val a =nuevoCrearPerfil(instanciaAut.currentUser?.email.toString())
                //cargarInfPerfil(instanciaAut.currentUser?.email.toString())
                //Thread.sleep(3_000)
                if (AuthUsuario.infUsuario!=null){
                    irActividad(ActividadMenuPrincipal::class.java)
                }else{
                    Log.i("existe","el usuario esta ingresando por primera vez, pasara a seleccionar su informacion")
                    irActividad(ActividadCrearUsuario::class.java)
                }
                //referencia.

//-----------------------------------------------------------------------------------------------------------------

            }else{
                solicitarIngresarAplicativo()
                setearUsuarioFirebase()
            }
        }
        val buttonSalir = findViewById<Button>(R.id.btn_ingreso_salir)
        buttonSalir.setOnClickListener{
            solicitarSalirAplicativo()
            //irActividad(ProbarSalir::class.java)
        }

    }
    fun cargarInfPerfil(correo:String){
        Log.i("firebase-firestore","XXXXXX ESTA ENTRANDO A CARGAR con : ${correo}")
        val db = Firebase.firestore
        val referencia = db.collection("infUsuario")
        referencia
            .whereEqualTo("correo", correo)
            .get()
            .addOnSuccessListener {
                for (document in it){
                    Log.i("firebase-firestore","XXXXXX ${document.data}  ID${document.id}")
                    AuthUsuario.infUsuario = document.toObject(FirestoreInfoUsuarioDTO::class.java)
                    AuthUsuario.infUsuario!!.uid=document.id
                    Log.i("firebase-firestore","Se obtuvo  ${AuthUsuario.infUsuario!!.uid}  ")

                }
            }.addOnFailureListener{
                Log.i("firebase-firestore","Failure : ${it}")
            }
    }
    fun cargarUsuarioUID(uid:String): FirestoreInfoUsuarioDTO?{
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
                    Log.i("firebase-firestore","Se obtuvo  ${AuthUsuario.infUsuario!!.uid}  ")


            }.addOnFailureListener{
                Log.i("firebase-firestore","Failure : ${it}")
            }
        return usuarioBuscado
    }

    fun solicitarIngresarAplicativo(){
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.FacebookBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())
        val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTosAndPrivacyPolicyUrls(
                    "https://example.com/terms.html",
                    "https://example.com/privacy.html")
                .build(),
            CODIGO_INICIO_SECION)
    }

    fun solicitarSalirAplicativo(){
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener{
                val texto = findViewById<TextView>(R.id.tv_inicio_sesion)

                //texto.text = textoNoLogueado
                AuthUsuario.usuario = null
                AuthUsuario.infUsuario=null
                AuthUsuario.locacion=null
                cambiarTextoYBotones()
                //mostrarBotones()
                //val textRoles = findViewById<TextView>(R.id.tv_roles)
                //textRoles.text=""
                Log.i("firebase-login","Salio del app")
            }
        val botonIngreso = findViewById<Button>(R.id.btn_ingreso_normal)
        botonIngreso.setText("${this.getString(R.string.principal_ingresar)}")
    }
    fun setearUsuarioFirebase() {
        val instanciaAuth = FirebaseAuth.getInstance()
        val usuarioLocal = instanciaAuth.currentUser
        if (usuarioLocal!=null){
            if(usuarioLocal.email!= null){
                //uid, emain
                val usuarioFirebase = UsuarioFirebase(usuarioLocal.uid,usuarioLocal.email!!,usuarioLocal.displayName,null)
                AuthUsuario.usuario = usuarioFirebase
                cargarRolesUsuario(usuarioFirebase.email)
                cargarInfPerfil(usuarioFirebase.email)
                //mostrarRolesEnPantalla()
                val text = findViewById<TextView>(R.id.tv_inicio_sesion)
                text.setText("${this.getString(R.string.principal_bienvenido)}:\n ${usuarioFirebase.name}")
                Log.i("inicio","entro o esta entrando autenticandose en FIREBASE")
            }
        }
        //cargarInfPerfil(AuthUsuario.usuario!!.email)
    }

    fun cargarRolesUsuario(uid: String){
        val db = Firebase.firestore
        val referencia =db.collection("usuario").document(uid)
        referencia.get().addOnSuccessListener {
            Log.i("firebase-firestore","--Datos: ${it.data}")
            val fireStoreUsuario= it.toObject(FirestoreUsuarioDTO::class.java)
            AuthUsuario.usuario?.roles = fireStoreUsuario?.roles
            //mostrarRolesEnPantalla()
        }.addOnFailureListener{
            Log.i("firebase-firestore","Fallo cargar usuario")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            CODIGO_INICIO_SECION->{
                if(resultCode ==  Activity.RESULT_OK){
                    val usuario = IdpResponse.fromResultIntent(data)
                    if(usuario?.isNewUser==true){
                        if(usuario?.email!=null){
                            val db = Firebase.firestore
                            val nuevoUsuario = hashMapOf<String,Any>(
                                "roles" to arrayListOf("usuario")
                            )
                            db.collection("usuario")
                                .document(usuario.email.toString())
                                .set(nuevoUsuario)
                                .addOnSuccessListener {
                                    Log.i("firebase-firestore","Se creo")
                                }.addOnFailureListener{
                                    Log.i("firebase-firestore","Fallo")
                                }
                        }
                    }

                    val texto = findViewById<TextView>(R.id.tv_inicio_sesion)
                    cambiarTextoYBotones()
                    setearUsuarioFirebase()
                    //mostrarBotones()

                }
                //BAuthUsuario.usuario = usuario as FirebaseUser


            }
            else->{
             //   val texto = findViewById<TextView>(R.id.textView)
               // texto.text = "El usuario cancelo"
            }
        }

    }
    fun cambiarTextoYBotones(){
        val instanciaAut = FirebaseAuth.getInstance()
        val botonIngreso = findViewById<Button>(R.id.btn_ingreso_normal)

        val texto = findViewById<TextView>(R.id.tv_inicio_sesion)
        if (instanciaAut.currentUser!=null){
            texto.text = "${this.getString(R.string.principal_bienvenido)}:\n ${instanciaAut.currentUser?.displayName}"
            botonIngreso.setText("${this.getString(R.string.principal_acceder)}")
            Log.i("inicio","entro con sesion FIREBASE")
            setearUsuarioFirebase()
            //cargarInfPerfil(AuthUsuario.usuario?.email.toString())
        }else{
            texto.text = textoNoLogueado.toString()
            Log.i("inicio","entro sin sesion FIREBASE")
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