package com.example.examenpokentrpp1.activPokemon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.examenpokentrpp1.MainActivity
import com.example.examenpokentrpp1.R
import com.example.examenpokentrpp1.model.Pokemon
import com.example.examenpokentrpp1.viewModel.CapturaViewModel
import com.example.examenpokentrpp1.viewModel.PokemonViewModel

class VistaListaPokemon : AppCompatActivity() {

    private lateinit var mPokemonViewModel: PokemonViewModel
    private lateinit var mmCapturaViewModel: CapturaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vista_lista_pokemon)

        iniciarRecyclerView()

        val botonCrearPokemon = findViewById<Button>(R.id.btn_crear_pokemon)
        botonCrearPokemon.setOnClickListener{
            irActividad(VistaCrearPokemon::class.java,null)
        }
        val botonRegresarPrincipal = findViewById<Button>(R.id.btn_lista_pokemon_regreso_principal)
        botonRegresarPrincipal.setOnClickListener{
            irActividad(MainActivity::class.java,null)
        }

    }

    fun iniciarRecyclerView(){
        val adapterPokemon = ListAdapterPokemon()
        adapterPokemon.setContext(this)
        val recyclerViewPokemon = findViewById<RecyclerView>(R.id.rv_lista_pokemon)
        recyclerViewPokemon.adapter = adapterPokemon
        recyclerViewPokemon.layoutManager = LinearLayoutManager(applicationContext)


        // UserViewModel
        mmCapturaViewModel = ViewModelProvider(this).get(CapturaViewModel::class.java)
        mPokemonViewModel = ViewModelProvider(this).get(PokemonViewModel::class.java)
        mPokemonViewModel.getPokemonList().observe(this, Observer {
                listaPokemon ->
            adapterPokemon.setData(listaPokemon)
        })

    }


    override fun onContextItemSelected(item: MenuItem): Boolean {
        val pokemon: Pokemon? = mPokemonViewModel.getPokemonList().value?.get(item.groupId)
        return when(item?.itemId){
            121 ->{

                val parametros = arrayListOf<Pair<String,*>>(
                    Pair("pokemon",pokemon)
                )
                irActividad(VistaEditarPokemon::class.java,parametros)
                //irActividad(VistaEditarEntrenador::class.java,parametros)
                return true
            }
            122->{
                if (pokemon != null) {
                    deletePokemon(pokemon)
                }
                return true
            }
            else-> super.onContextItemSelected(item)
        }

    }

    private fun deletePokemon(pokemon: Pokemon){
        val builder = AlertDialog.Builder(this)
        val aux = pokemon
        var a:String = getText(R.string.op_si).toString()//-----------CAMBIAR
        builder.setPositiveButton(a){_,_ ->
            mmCapturaViewModel.deleteCapturasPokemon(pokemon.idPokemon)
            mPokemonViewModel.deletePokemon(pokemon)
            Toast.makeText(this,"${getText(R.string.toast_lista_pokemon_mensaje_borrado_exitoso)}",
                Toast.LENGTH_LONG).show()
        }
        builder.setNegativeButton(getText(R.string.op_no)){_,_->//-----------CAMBIAR

        }
        builder.setTitle("${getText(R.string.ad_lista_pokemon_eliminar)}")//-----------CAMBIAR
        builder.setMessage("${getText(R.string.ad_lista_pokemon_mensaje_seg)} ${aux.nombre}?")
        builder.create().show()
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