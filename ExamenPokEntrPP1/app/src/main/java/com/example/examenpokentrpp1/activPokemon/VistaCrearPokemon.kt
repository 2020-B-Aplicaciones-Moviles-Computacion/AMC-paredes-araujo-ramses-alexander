package com.example.examenpokentrpp1.activPokemon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.example.examenpokentrpp1.R
import com.example.examenpokentrpp1.model.Pokemon
import com.example.examenpokentrpp1.viewModel.PokemonViewModel
import kotlinx.android.synthetic.main.activity_vista_crear_pokemon.*

class VistaCrearPokemon : AppCompatActivity() {

    private lateinit var mPokemonViewModel: PokemonViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vista_crear_pokemon)



        mPokemonViewModel = ViewModelProvider(this).get(PokemonViewModel::class.java)


        var seleccion = 0
        val opTipo = findViewById(R.id.sp_tipo_pokemon) as Spinner
        val options = arrayOf(
                "${getText(R.string.pokemon_type_1)}",
                "${getText(R.string.pokemon_type_2)}",
                "${getText(R.string.pokemon_type_3)}",
                "${getText(R.string.pokemon_type_4)}",
                "${getText(R.string.pokemon_type_5)}",
                "${getText(R.string.pokemon_type_6)}",
                "${getText(R.string.pokemon_type_7)}",
                "${getText(R.string.pokemon_type_8)}",
                "${getText(R.string.pokemon_type_9)}",
                "${getText(R.string.pokemon_type_10)}",
                "${getText(R.string.pokemon_type_11)}",
                "${getText(R.string.pokemon_type_12)}"
        )



        opTipo.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,options)

        opTipo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                seleccion = 0
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                seleccion = position
            }

        }
        val botonCrearPokemon = findViewById<Button>(R.id.btn_crear_nuevo_pokemon)
        botonCrearPokemon.setOnClickListener{
            Log.i("creacion","vale, entra y el tipo sel es: ${seleccion}")
            insertDataToDatabase(seleccion)
            irActividad(VistaListaPokemon::class.java,null)
        }
        val botonSalirCrearPokemon = findViewById<Button>(R.id.btn_cancelar_crear_nuevo_pokemon)
        botonSalirCrearPokemon.setOnClickListener{
            irActividad(VistaListaPokemon::class.java,null)
        }
    }
    fun insertDataToDatabase(seleccion:Int){
        val nPokedex = et_crear_pokemon_nPokedex.text.toString().toInt()
        val nombre = et_crear_pokemon_nombre.text.toString()
        val tipo = seleccion
        val altura = etn_crear_pokemon_altura.text.toString().toDouble()
        val peso = etn_crear_pokemon_peso.text.toString().toDouble()
        if (true){
            val pokemon = Pokemon(0, nPokedex, nombre, tipo, altura, peso)
            mPokemonViewModel.addPokemon(pokemon)
            Toast.makeText(this, "${this.getText(R.string.toast_crear_pokemon_exitoso)}", Toast.LENGTH_LONG).show()

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
/*
    val idPokemon: Int,
    val nPokedex: Int,
    val nombre: String,
    val tipo: String,
    val altura: Double,
    val peso: Double*/
/*
private fun insertDataToDatabase() {
    Log.i("creacion", "entro a la funcion")
    val idPokemon = et_crea .text.toString()
    val nombre = et_crear_entrenador_nombre.text.toString()
    //val genero = et_crear_entrenador_genero.text.toString().toCharArray()?.get(0)
    var genero = ' '
    val radioButton: RadioButton = findViewById<RadioButton>(R.id.rb_entrenador_F)

    if (radioButton.isChecked){
        Log.i("databasess","rb Fem ")
        genero='F'
    }else{
        Log.i("databasess","rb Male ")
        genero='M'
    }
    val fechaNac = tv_crear_entrenador_fechaNac.text.toString()
    val activo = true


    if(inputCheck(idEntrenador,nombre,genero,fechaNac)){
        //Crear entrenador
        val entrenador = Entrenador(0,idEntrenador,nombre,genero,fechaNac,activo)
        //Añadir entrenador a la base de datos

        mEntrenadorViewModel.addEntrenador(entrenador)
        Log.i("creacion", "Se agrego")
        Toast.makeText(this, "Agregado Exitosamente", Toast.LENGTH_LONG).show()
    }else{
        Toast.makeText(this, "Llene todos los campos", Toast.LENGTH_LONG).show()
    }

}*/



/*
* */

/*        option = findViewById(R.id.sp_option) as Spinner
        result = findViewById(R.id.tv_result) as TextView

        val options = arrayOf("op1","op2","op3")

        option.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,options)

        option.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                result.text = "select something"
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                result.text = options.get(position)
            }

        }*/