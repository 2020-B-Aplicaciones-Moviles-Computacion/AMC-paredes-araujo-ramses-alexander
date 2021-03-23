package com.example.examenpokentrpp1.activCapturasEntrenador

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
import com.example.examenpokentrpp1.R
import com.example.examenpokentrpp1.activEntren.VistaEditarEntrenador
import com.example.examenpokentrpp1.activEntren.VistaListaEntrenadores
import com.example.examenpokentrpp1.model.Captura
import com.example.examenpokentrpp1.model.Entrenador
import com.example.examenpokentrpp1.model.Pokemon
import com.example.examenpokentrpp1.viewModel.CapturaViewModel
import com.example.examenpokentrpp1.viewModel.PokemonViewModel
import kotlinx.android.synthetic.main.activity_vista_lista_capturas_entrenador.*

class VistaListaCapturasEntrenador : AppCompatActivity() {

    private lateinit var mmCapturaViewModel: CapturaViewModel
    private lateinit var mPokemonViewModel: PokemonViewModel

    private lateinit var arrayListPokemon : ArrayList<Pokemon>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vista_lista_capturas_entrenador)
        arrayListPokemon = ArrayList<Pokemon>()
        //arrayListPokemon.add(Pokemon(0,2,"xd","xddd",0.6,0.7))
       // mEntrenadorViewModel = ViewModelProvider(this).get(EntrenadorViewModel::class.java)
        //Obtengo el entrenador que se me paso por intent
        val entrenador = intent.getParcelableExtra<Entrenador>("entrenador")
        if (entrenador != null) {
            Log.i("menu-recib","Nombre: ${entrenador.nombre}")
        }
        if (entrenador != null) {
            this.tv_ci_entrenador_lista_capturas.setText(entrenador.idEntrenador)
            this.tv_nombre_entrenador_lista_capturas.setText(entrenador.nombre)
        }


        if (entrenador != null) {
            iniciarRecyclerView(entrenador.idBaseEntrenador)
        }

        //botones
        val parametros = arrayListOf<Pair<String,*>>(
            Pair("entrenador",entrenador)
        )
        val btnCrearCaptura = findViewById<Button>(R.id.btn_crear_captura_entrenador)
        btnCrearCaptura.setOnClickListener{

            irActividad(VistaCrearCaptura::class.java,parametros)
        }
        val btnRegresarListadoEntrenador = findViewById<Button>(R.id.btn_lista_capturas_entrenador_regresar)
        btnRegresarListadoEntrenador.setOnClickListener{
            irActividad(VistaListaEntrenadores::class.java,null)
        }
        //irActividad(VistaCrearCaptura::class.java,parametros)
    }

    fun iniciarRecyclerView(idEntrenador:Int){
        val adapterCaptura = ListAdapterCaptura()
        val recyclerViewCapturas = findViewById<RecyclerView>(R.id.rv_lista_capturas_entrenador)
        //paso la lista de pokemon a el adaptador para poder mostrar sus nombres

        mPokemonViewModel = ViewModelProvider(this).get(PokemonViewModel::class.java)
        mPokemonViewModel.getPokemonList().observe(this, Observer {
                listaPokemon ->
            //-----------------------------------------------------------
            if(!listaPokemon.isEmpty()){
                adapterCaptura.setDataListaPokemon(listaPokemon)
                Log.i("prueba","se agrega siguiente ${listaPokemon.get(0).nombre}")
                for (a in listaPokemon){
                    Log.i("prueba","se la lista ${a.nombre}")

                }
            }

            //------------------------------------------------------
            recyclerViewCapturas.adapter = adapterCaptura
            recyclerViewCapturas.layoutManager = LinearLayoutManager(applicationContext)

            //USER VIEW MODEL //
            mmCapturaViewModel = ViewModelProvider(this).get(CapturaViewModel::class.java)
            mmCapturaViewModel.getCapturaList().observe(this, Observer {
                    listaCapturas ->
                var listaCapturasDeEntrenador : ArrayList<Captura> = ArrayList()
                for(elemento in listaCapturas){
                    if (elemento.idBaseEntrenador==idEntrenador){
                        listaCapturasDeEntrenador.add(elemento)
                    }
                }
                adapterCaptura.setData(listaCapturasDeEntrenador)
            })
        })
    }



    override fun onContextItemSelected(item: MenuItem): Boolean {
        val captura: Captura? = mmCapturaViewModel.getCapturaList().value?.get(item.groupId)
        return when(item?.itemId){
            111->{
                if (captura != null) {
                    deleteCaptura(captura)
                }
                return true
            }
            else-> super.onContextItemSelected(item)
        }

    }



    private fun deleteCaptura(captura: Captura){
        val builder = AlertDialog.Builder(this)
        var a:String = getText(R.string.op_si).toString()
        builder.setPositiveButton(a){_,_ ->
            mmCapturaViewModel.deleteCaptura(captura)//getText(R.string.toast_lista_entrenadores_mensaje_borrado_exitoso)}
            Toast.makeText(this,"${getText(R.string.toast_lista_capturas_mensaje_borrado_exitoso)}", Toast.LENGTH_LONG).show()
        }
        builder.setNegativeButton(getText(R.string.op_no)){_,_->

        }
        builder.setTitle("${getText(R.string.crear_captura_titulo)}")
        builder.setMessage("${getText(R.string.ad_lista_capturas_mensaje_seg)}")
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