package com.example.aplicacionmoviles

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Button
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    val  CODIGO_ACUALIZAR = 102
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        BaseDatosMemoria.cargaInicialDatos()

        BaseDatosMemoria.cagarEntrenadores()

        val buttonCicloVida = findViewById<Button>(R.id.button_ir_ciclo_vida)
        buttonCicloVida.setOnClickListener {
            irActividad(ACicloVida:: class.java, null)
        }
        val buttonListView = findViewById<Button>(R.id.bnt_ir_list_view)
        buttonListView.setOnClickListener {
            irActividad(BListView:: class.java, null)
        }
        val buttonIntentExplicitoParametros = findViewById<Button>(R.id.btn_ir_intent_explicito_parametros)
        buttonIntentExplicitoParametros.setOnClickListener {
            val liga = DLiga("Kanto","Pokemon")
            val entrendaor:Parcelable = BEntrenador("Gold","Jontho",liga)
            val parametros = arrayListOf<Pair<String,*>>(
                Pair("nombre","Ramses"),
                Pair("apellido","Paredes"),
                Pair("edad",31),
                Pair("entrenador",entrendaor)
            )
            irActividad(CIntentExplicitoParametros:: class.java, parametros,CODIGO_ACUALIZAR)
        }


        //CONSULTA SQL DE USUARIO
        EBaseDeDatos.TablaUsuario = ESqliteHelperUsuario(this)
        val usuarioEncontrado=EBaseDeDatos.TablaUsuario?.consultarUsuarioPorId(1)
        Log.i("bdd","ID ${usuarioEncontrado?.id} Nombre: ${usuarioEncontrado?.nombre} " +
                "Descripcion: ${usuarioEncontrado?.descripcion}")

        if(usuarioEncontrado?.id==0){
            val resultado = EBaseDeDatos.TablaUsuario?.
            crearUsuarioFormulario(
                    "Ramses",
                    "Estudiante"
            )
            if (resultado!=null){
                if(resultado){
                    Log.i("dbb","EXITO")
                }else
                {
                    Log.i("dbb","ERROR")
                }
            }
        } else {
            val resultado = EBaseDeDatos.TablaUsuario?.
            actualizarUsuarioFormulario("Vicente",Date().toString(),1)
            if (resultado!=null){
                if (resultado){
                    Log.i("bdd","Se actualizo")
                }
            }
        }
        val votonIrFIntentConRespuesta = findViewById<Button>(
            R.id.btn_ir_intent_con_respuesta
        ).setOnClickListener { irActividad(
            FIntentConRespuesta::class.java
        ) }
        val botonIrRecyclerView = findViewById<Button>(R.id.btn_ir_recycler_view)
        botonIrRecyclerView.setOnClickListener {
            irActividad(
                GRecyclerView::class.java
            )
        }
        val botonIrHttp = findViewById<Button>(R.id.btn_ir_http)
        botonIrHttp.setOnClickListener {
            irActividad(
                HHttpActivity::class.java
            )
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
                    intentExplicito.putExtra(nombreVariable.toString(),valorVariable as Parcelable )
                }
            }
        }
        if (codigo!=null){
            startActivityForResult(intentExplicito,codigo)
        }else{
            startActivity(intentExplicito)
        }


    }

    fun irCicloVida(){
        val intentExplicito = Intent(
            this,
            ACicloVida:: class.java
        )
        startActivity(intentExplicito)
    }
    fun irListView(){
        val intentExplicito = Intent(
            this,
            BListView:: class.java
        )
        startActivity(intentExplicito)
    }

    override fun onActivityResult(
        requestCode: Int, //CODIGO PETICION - 1882
        resultCode: Int, //CODIGO RESULTADO - RESULT_OK
        data: Intent?//DATOS OPCIONALES
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            102->{
                if(resultCode== RESULT_OK){
                    Log.i("intent-explicito","Si actualizo")
                    if(data!=null){
                        val nombre = data.getStringExtra("nombre")
                        val edad = data.getIntExtra("edad",0)
                        Log.i("intent-explicito","Nombre: ${nombre}  Edad: ${edad}")

                    }

                }else{
                    Log.i("intent-explicito","No actualizo")
                }

            }
        }
    }
}