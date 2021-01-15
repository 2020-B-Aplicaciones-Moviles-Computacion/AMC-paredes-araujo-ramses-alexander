package com.example.examenpokentrpp1.activEntren

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import android.util.Log
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.example.examenpokentrpp1.R
import com.example.examenpokentrpp1.model.Entrenador
import com.example.examenpokentrpp1.viewModel.EntrenadorViewModel
import kotlinx.android.synthetic.main.activity_vista_crear_entrenador.*
import java.util.*
import kotlin.collections.ArrayList

class VistaCrearEntrenador : AppCompatActivity() {

    private lateinit var mEntrenadorViewModel: EntrenadorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vista_crear_entrenador)


        mEntrenadorViewModel = ViewModelProvider(this).get(EntrenadorViewModel::class.java)
        //fecha
        val c= Calendar.getInstance()
        val year= c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val tvFecha = findViewById<TextView>(R.id.tv_crear_entrenador_fechaNac)

        val botonSelFecha = findViewById<Button>(R.id.btn_crear_entrenador_sel_fecha)
        botonSelFecha.setOnClickListener{
            var dpd = DatePickerDialog(this,
                    DatePickerDialog.OnDateSetListener {
                        view, mYear,mMonth , mDay ->
                        val mmMonth = mMonth+1
                        val date = "$mDay/$mmMonth/$mYear"
                        tvFecha.setText(date)
                    },year,month,day)
            dpd.show()
        }

        val botonCrearNuevoEntrenador = findViewById<Button>(R.id.btn_crear_nuevo_entrenador)
        botonCrearNuevoEntrenador.setOnClickListener{
            insertDataToDatabase()
            vaciarCampos()
            irActividad(VistaListaEntrenadores::class.java,null)
        }

        val botonCancelarCrearEntrenador = findViewById<Button>(R.id.btn_cancelar_crear_entrenador)
        botonCancelarCrearEntrenador.setOnClickListener{
            irActividad(VistaListaEntrenadores::class.java,null)
        }
    }

    private fun insertDataToDatabase() {
        Log.i("creacion", "entro a la funcion")
        val idEntrenador = et_crear_entrenador_id.text.toString()
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
            val entrenador = Entrenador(0, idEntrenador, nombre, genero, fechaNac, activo)
            //Añadir entrenador a la base de datos

            mEntrenadorViewModel.addEntrenador(entrenador)
            Log.i("creacion", "Se agrego entrenador: ${entrenador.nombre}")
            Toast.makeText(this, "${getText(R.string.toast_crear_entrenador_exitoso)}", Toast.LENGTH_LONG).show()
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
    private fun vaciarCampos(){
        val idEntrenador = findViewById<EditText>(R.id.et_crear_entrenador_id)
        val nombre = findViewById<EditText>(R.id.et_crear_entrenador_nombre)
        val fechaNac = findViewById<TextView>(R.id.tv_crear_entrenador_fechaNac)
        idEntrenador.setText("")
        nombre.setText("")
        fechaNac.setText("Fecha")
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