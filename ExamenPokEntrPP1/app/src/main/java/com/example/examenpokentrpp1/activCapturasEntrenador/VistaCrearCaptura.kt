package com.example.examenpokentrpp1.activCapturasEntrenador

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.examenpokentrpp1.R
import com.example.examenpokentrpp1.model.Captura
import com.example.examenpokentrpp1.model.Entrenador
import com.example.examenpokentrpp1.model.Pokemon
import com.example.examenpokentrpp1.viewModel.CapturaViewModel
import com.example.examenpokentrpp1.viewModel.EntrenadorViewModel
import com.example.examenpokentrpp1.viewModel.PokemonViewModel
import kotlinx.android.synthetic.main.activity_vista_crear_captura.*
import java.util.*
import kotlin.collections.ArrayList

class VistaCrearCaptura : AppCompatActivity() {

    private lateinit var mPokemonViewModel: PokemonViewModel
    private lateinit var mCapturaViewModel: CapturaViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vista_crear_captura)
        mPokemonViewModel = ViewModelProvider(this).get(PokemonViewModel::class.java)
        mCapturaViewModel = ViewModelProvider(this).get(CapturaViewModel::class.java)

        val entrenador = intent.getParcelableExtra<Entrenador>("entrenador")
        if (entrenador != null) {
            Log.i("menu-recib","Nombre: ${entrenador.nombre}")
        }
        if (entrenador != null) {
            this.tv_ci_entrenador_crear_captura.setText(entrenador.idEntrenador)
            this.tv_nombre_entrenador_crear_captura.setText(entrenador.nombre)
        }


        var seleccion = ""
        val opTipo = findViewById(R.id.sp_pokeball_crear_captura) as Spinner
        val options = arrayOf("PokeBall","SuperBall","UltraBall")
        opTipo.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,options)

        opTipo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                seleccion = options.get(0)
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                seleccion = options.get(position)
            }

        }
        val parametros = arrayListOf<Pair<String,*>>(
            Pair("entrenador",entrenador)
        )
        val btnCrearCaptura = findViewById<Button>(R.id.btn_ejecutar_captura)
        btnCrearCaptura.setOnClickListener{
            //aqui ejecutar la captura y regresar a principal
            /*            Log.i("creacion","vale, entra y el tipo sel es: ${seleccion}")
            insertDataToDatabase(seleccion)*/
            if (entrenador != null) {
                /*if (insertDataToDatabase(entrenador.idBaseEntrenador,seleccion)){
                    irActividad(VistaListaCapturasEntrenador::class.java,parametros)
                }*/
                insertDataToDatabase2(entrenador.idBaseEntrenador,seleccion)
            }

            irActividad(VistaListaCapturasEntrenador::class.java,parametros)
        }


        val btnCancelarCrearCapturas = findViewById<Button>(R.id.btn_cancelar_ejecutar_captura)

        btnCancelarCrearCapturas.setOnClickListener{
            //aqui solo retornar
            irActividad(VistaListaCapturasEntrenador::class.java,parametros)
        }
    }
    /*
    *
    val idCaptura: Int,
    val idBaseEntrenador: Int,
    val idBasePokemon: Int,

    val generoPokemon: Char,
    val nivel: Int,
    val fechaCaptura: String,
    val tipoPokeball: String,
    val lifePoints: Double,
    val fuerza: Double
    * */



    fun insertDataToDatabase(idBaseEntrenador: Int, pokeballSeleccion: String): Boolean{
        //Obtenemos por la lista de pokemon un pokemon aleatorio
        var listaPokemonSize: Int = 0
        var aux = true

        val listaPokemon = mPokemonViewModel.getPokemonList().observe(this, Observer {
                listaPokemon ->

            listaPokemonSize=listaPokemon.size
        })

        if (listaPokemonSize==0){
            Toast.makeText(this, "No existen pokemon.", Toast.LENGTH_LONG).show()
            aux = false
        }else{

            val idBasePokemon = (1..listaPokemonSize).random()

            Log.i("creacion","el random es: ${idBasePokemon}  desde 0  hasta ${listaPokemonSize}")

            val idEntrenador= idBaseEntrenador
            var generoPokemon: Char
            if ((0..1).random()==0){
                generoPokemon = 'M'
            }else{
                generoPokemon = 'F'
            }

            val nivel = (1..40).random()
            //Aqui conseguimos la fecha actual

            val c= Calendar.getInstance()
            val year= c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)+1
            val day = c.get(Calendar.DAY_OF_MONTH)


            val fechaCaptura ="${day}/${month}/${year}"

            //---------------------------------------
            val tipoPokeball = pokeballSeleccion

            val lifePoints = nivel*4.2
            val fuerza = nivel*2.3

            val captura = Captura(0,idBaseEntrenador,
                idBasePokemon,generoPokemon,nivel,fechaCaptura,tipoPokeball,lifePoints,fuerza)

            mCapturaViewModel.addCaptura(captura)
            Toast.makeText(this, "Pokemon capturado Exitosamente", Toast.LENGTH_LONG).show()

            aux = true

        }
        return aux

    }


//--------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------

    fun insertDataToDatabase2(idBaseEntrenador: Int, pokeballSeleccion: String){
        //Obtenemos por la lista de pokemon un pokemon aleatorio

        mPokemonViewModel.getPokemonList().observe(this, Observer {
            listaPokemon ->
            if (listaPokemon.isEmpty()){
                Toast.makeText(this, "No existen pokemon.", Toast.LENGTH_LONG).show()

            }else{
                val idBasePokemon = listaPokemon.get((0..listaPokemon.size-1).random()).idPokemon

                var generoPokemon: Char
                if ((0..1).random()==0){
                    generoPokemon = 'M'
                }else{
                    generoPokemon = 'F'
                }

                val nivel = (1..40).random()
                //Aqui conseguimos la fecha actual

                val c= Calendar.getInstance()
                val year= c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)+1
                val day = c.get(Calendar.DAY_OF_MONTH)


                val fechaCaptura ="${day}/${month}/${year}"

                //---------------------------------------
                val tipoPokeball = pokeballSeleccion

                val lifePoints = nivel*4.2
                val fuerza = nivel*2.3

                val captura = Captura(0,idBaseEntrenador,
                        idBasePokemon,generoPokemon,nivel,fechaCaptura,tipoPokeball,lifePoints,fuerza)

                mCapturaViewModel.addCaptura(captura)
                Toast.makeText(this, "${this.getText(R.string.toast_crear_capturas_mensaje_exitoso)}", Toast.LENGTH_LONG).show()
            }
        })

    }



    //------------------------------------------------------------------------------------




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


