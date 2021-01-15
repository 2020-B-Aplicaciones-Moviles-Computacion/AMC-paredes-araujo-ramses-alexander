package com.example.examenpokentrpp1.activEntren

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
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
import com.example.examenpokentrpp1.activCapturasEntrenador.VistaListaCapturasEntrenador
import com.example.examenpokentrpp1.model.Entrenador
import com.example.examenpokentrpp1.viewModel.CapturaViewModel
import com.example.examenpokentrpp1.viewModel.EntrenadorViewModel

class VistaListaEntrenadores : AppCompatActivity() {

    private lateinit var mEntrenadorViewModel: EntrenadorViewModel
    private lateinit var mmCapturaViewModel: CapturaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vista_lista_entrenadores)


        iniciarRecyclerView()

        //BOTONES
        val buttonCrearEntrenador = findViewById<Button>(R.id.btn_crear_entrenador)
        buttonCrearEntrenador.setOnClickListener{
            irActividad(VistaCrearEntrenador::class.java,null)
        }
        val botonRegresoPrincipal = findViewById<Button>(R.id.btn_lista_entrenador_regreso_principal)
        botonRegresoPrincipal.setOnClickListener{
            irActividad(MainActivity::class.java,null)
        }
    }
    fun iniciarRecyclerView(){
        val adapterEntrenador = ListAdapterEntrenador()
        val recyclerViewEntrenadores = findViewById<RecyclerView>(R.id.rv_lista_entrenadores)
        recyclerViewEntrenadores.adapter = adapterEntrenador
        recyclerViewEntrenadores.layoutManager = LinearLayoutManager(applicationContext)


        // UserViewModel
        mmCapturaViewModel = ViewModelProvider(this).get(CapturaViewModel::class.java)
        mEntrenadorViewModel = ViewModelProvider(this).get(EntrenadorViewModel::class.java)
        mEntrenadorViewModel.getEntrenadorList().observe(this, Observer {
                listaEntrenadores ->
            adapterEntrenador.setData(listaEntrenadores)
        })

    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val entrenador: Entrenador? = mEntrenadorViewModel.getEntrenadorList().value?.get(item.groupId)
        return when(item?.itemId){
            121 ->{
                val parametros = arrayListOf<Pair<String,*>>(
                    Pair("entrenador",entrenador)
                )
                irActividad(VistaEditarEntrenador::class.java,parametros)
                return true
            }
            122->{
                if (entrenador != null) {
                    deleteEntrenador(entrenador)
                }
                return true
            }
            123->{
                val parametros = arrayListOf<Pair<String,*>>(
                    Pair("entrenador",entrenador)
                )
                irActividad(VistaListaCapturasEntrenador::class.java,parametros)
                return true
            }
            else-> super.onContextItemSelected(item)
        }

    }

    private fun deleteEntrenador(entrenador:Entrenador){
        val builder = AlertDialog.Builder(this)
        val aux = entrenador
        var a:String = getText(R.string.op_si).toString()
        builder.setPositiveButton(a){_,_ ->
            mmCapturaViewModel.deleteCapturasEntrenador(entrenador.idBaseEntrenador)
            mEntrenadorViewModel.deleteEntrenador(entrenador)
            Toast.makeText(this,"${getText(R.string.toast_lista_entrenadores_mensaje_borrado_exitoso)}",Toast.LENGTH_LONG).show()
        }
        builder.setNegativeButton(getText(R.string.op_no)){_,_->

        }
        builder.setTitle("${getText(R.string.ad_lista_entrenadores_eliminar)}")
        builder.setMessage("${getText(R.string.ad_lista_entrenadores_mensaje_seg)} ${aux.nombre}?")
        builder.create().show()
    }





    //------------------------------------------------------------------------------
    //FUNCIONES
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