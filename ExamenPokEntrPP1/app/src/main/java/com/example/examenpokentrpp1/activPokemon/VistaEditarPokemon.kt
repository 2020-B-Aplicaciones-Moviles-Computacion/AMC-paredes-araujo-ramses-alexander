package com.example.examenpokentrpp1.activPokemon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.examenpokentrpp1.R
import com.example.examenpokentrpp1.model.Pokemon
import com.example.examenpokentrpp1.viewModel.PokemonViewModel
import kotlinx.android.synthetic.main.activity_vista_editar_pokemon.*

class VistaEditarPokemon : AppCompatActivity() {

    private lateinit var mPokemonViewModel: PokemonViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vista_editar_pokemon)

        mPokemonViewModel= ViewModelProvider(this).get(PokemonViewModel::class.java)

        //Obtengo el pokemon que se paso por intent
        val pokemon = intent.getParcelableExtra<Pokemon>("pokemon")

            Log.i("edit","Nombre: ${pokemon!!.nombre}")
        this.et_editar_pokemon_nombre.setText(pokemon.nombre)
        this.et_editar_pokemon_nPokedex.setText(pokemon.nPokedex.toString())
        this.sp_tipo_pokemon_editar.setSelection(0)
        this.etn_editar_pokemon_altura.setText(pokemon.altura.toString())
        this.etn_editar_pokemon_peso.setText(pokemon.peso.toString())
/*
*/
        //----------------------------------------------------
        var seleccion = 0
        val opTipo = findViewById(R.id.sp_tipo_pokemon_editar) as Spinner

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




        //-----------------------------------------------------

        val btnAceptar = findViewById<Button>(R.id.btn_aceptar_editar_pokemon)
        btnAceptar.setOnClickListener{
            if (pokemon!=null){
                updateItem(pokemon.idPokemon,seleccion)
            }
            irActividad(VistaListaPokemon::class.java,null)
        }
        val btnRegresar = findViewById<Button>(R.id.btn_cancelar_editar_pokemon)
        btnRegresar.setOnClickListener{
            irActividad(VistaListaPokemon::class.java,null)
        }

    }
    private fun updateItem(idPokemon:Int , tipo :Int){
        Log.i("edicion", "entro a la funcion")
        val nPokedex = et_editar_pokemon_nPokedex.text.toString().toInt()
        val nombre = et_editar_pokemon_nombre.text.toString()
        val tipo = tipo
        val altura = etn_editar_pokemon_altura.text.toString().toDouble()
        val peso = etn_editar_pokemon_peso.text.toString().toDouble()

        val pokemon = Pokemon(idPokemon,nPokedex,nombre,tipo,altura,peso)
        mPokemonViewModel.updatePokemon(pokemon)
        Toast.makeText(this, "${getText(R.string.toast_pokemon_mensaje_actualizado_exitoso)}", Toast.LENGTH_LONG).show()
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