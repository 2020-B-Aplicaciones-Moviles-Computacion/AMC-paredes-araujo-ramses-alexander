package com.example.examenpokentrpp1.activEntren

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.RadioButton
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.examenpokentrpp1.R
import com.example.examenpokentrpp1.model.Entrenador
import com.example.examenpokentrpp1.viewModel.EntrenadorViewModel
import kotlinx.android.synthetic.main.activity_vista_editar_entrenador.*
import java.util.*
import kotlin.collections.ArrayList

class VistaEditarEntrenador : AppCompatActivity() {

    private lateinit var mEntrenadorViewModel: EntrenadorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vista_editar_entrenador)

        mEntrenadorViewModel = ViewModelProvider(this).get(EntrenadorViewModel::class.java)

        //Obtengo el entrenador que se me paso por intent
        val entrenador = intent.getParcelableExtra<Entrenador>("entrenador")
        if (entrenador != null) {
            Log.i("menu-recib","Nombre: ${entrenador.nombre}")
        }
        //lleno los campos con la informacion del entrenador
        this.et_editar_entrenador_id.setText(entrenador?.idEntrenador)
        this.et_editar_entrenador_nombre.setText(entrenador?.nombre)
        this.tv_editar_entrenador_fechaNac.setText(entrenador?.fechaNac)
        //genero
        //fecha
        //---------------------------------------------------------------------
        val c= Calendar.getInstance()
        var year= c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)
        var day = c.get(Calendar.DAY_OF_MONTH)

        try {
            var auxFech=0
            val tokFecha = StringTokenizer(entrenador?.fechaNac,"/")
            while (tokFecha.hasMoreTokens()) {
                when(auxFech){
                    0 ->{
                        day = tokFecha.nextToken().toInt()
                        Log.i("edicion","entra al dia ")
                    }
                    1->{
                        month = tokFecha.nextToken().toInt()-1
                        Log.i("edicion","entra al mes")
                    }
                    2->{
                        year = tokFecha.nextToken().toInt()
                        Log.i("edicion","entra al año")
                    }
                }
                auxFech++
            }
        }catch (eRead1:NumberFormatException){

        }
        //para el boton fecha
        val botonSelFecha = findViewById<Button>(R.id.btn_editar_entrenador_sel_fecha)
        botonSelFecha.setOnClickListener{
            var dpd = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener {
                        view, mYear,mMonth , mDay ->
                    val mmMonth = mMonth+1
                    val date = "$mDay/$mmMonth/$mYear"
                    tv_editar_entrenador_fechaNac.setText(date)
                },year,month,day)
            dpd.show()
        }
        //-----------------------------------------------------------------------------------------

        //BOTONES
        val btnAceptar = findViewById<Button>(R.id.btn_aceptar_editar_entrenador)
        btnAceptar.setOnClickListener{
            if (entrenador != null) {
                updateItem(entrenador.idBaseEntrenador)
                irActividad(VistaListaEntrenadores::class.java,null)
            }
        }
        val btnCancelar = findViewById<Button>(R.id.btn_cancelar_editar_entrenador)
        btnCancelar.setOnClickListener{
            irActividad(VistaListaEntrenadores::class.java,null)
        }
    }

    private fun updateItem(idEntrenadorUpd: Int ) {
        Log.i("edicion", "entro a la funcion")
        val idEntrenador = et_editar_entrenador_id.text.toString()
        val nombre = et_editar_entrenador_nombre.text.toString()
        //val genero = et_crear_entrenador_genero.text.toString().toCharArray()?.get(0)
        var genero = ' '
        val radioButtonF: RadioButton = findViewById<RadioButton>(R.id.rb_entrenador_F)

        if (radioButtonF.isChecked){
            Log.i("databasess","rb Fem ")
            genero='F'
        }else{
            Log.i("databasess","rb Male ")
            genero='M'
        }
        val fechaNac = tv_editar_entrenador_fechaNac.text.toString()
        var activo=false
        if(sw_editar_entrenador_activo.isChecked){
            activo=true
        }



        if(inputCheck(idEntrenador,nombre,genero,fechaNac)){
            //Crear entrenador
            val entrenador = Entrenador(idEntrenadorUpd, idEntrenador, nombre, genero, fechaNac, activo)
            //Actualizar el  entrenador en la base de datos

            mEntrenadorViewModel.updateEntrenador(entrenador)
            Log.i("edicion", "Se edito entrenador: ${entrenador.nombre}")
            Toast.makeText(this, "${getText(R.string.toast_editar_entrenador_exitoso)}", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this, "Llene todos los campos", Toast.LENGTH_LONG).show()
        }

    }
    private fun inputCheck(
        idEntrenador:String,
        nombre:String,
        genero:Char,
        fechaNac:String
    ):Boolean{
        return !(TextUtils.isEmpty(idEntrenador) &&
                TextUtils.isEmpty(nombre) &&
                genero.toString().equals(' ') &&
                fechaNac.equals("Fecha")
                )
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