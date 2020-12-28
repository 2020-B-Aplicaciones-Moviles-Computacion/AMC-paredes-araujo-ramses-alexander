package com.example.aplicacionmoviles

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AlertDialog

class BListView : AppCompatActivity() {
    var posItemSeleccionado = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b_list_view)
        val listaNumeros = BaseDatosMemoria.arregloEnteros
        val listaEntrenadores = BaseDatosMemoria.arregloEntrenadores

        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,// Layout (xml visual) existe en Android
            listaEntrenadores
        )
        val listView = findViewById<ListView>(R.id.lv_entrenador)
        listView.adapter = adaptador
        //listaNumeros.add(5)
        //listaNumeros.add(6)
        /* Lo comentado es referente al mensaje de si y no
        listView.setOnItemLongClickListener { parent, view, position, id ->
            Log.i("intent-explicito","Hola: ${position}  ID: ${id}")

            val builder = AlertDialog.Builder(this)
           //builder.setMessage("Hola")
            var seleccionUsuario = booleanArrayOf(
                true,
                false,
                false
            )
            val opciones = resources.getStringArray(R.array.string_array_opciones_dialogo)
            builder.setMultiChoiceItems(
                opciones,
                seleccionUsuario,
                {
                    dialog,which,isCheked->
                    Log.i("intent-explicito","Selecciono: ${which} ${isCheked}")
                }
            )
            builder.setPositiveButton(
                    "Si",
                    DialogInterface.OnClickListener{
                    dialog, which ->
                        Log.i("intent-explicito","SI")
                    }
                )
            builder.setNegativeButton("No",null)
            val dialogo = builder.create()
            dialogo.show()

            return@setOnItemLongClickListener true
        }*/


        adaptador.notifyDataSetChanged()
        registerForContextMenu(listView)
        val botonAñadirLV = findViewById<Button>(R.id.btn_anadir_item_lv)
        botonAñadirLV.setOnClickListener {
            añadirListView2(adaptador,BEntrenador("1","1",DLiga("f","f")),listaEntrenadores)
        }
    }
    fun añadirListView(
        adaptador: ArrayAdapter<Int>,
        item: Int,
        arreglo: ArrayList<Int>
    ){
        arreglo.add(item)
        adaptador.notifyDataSetChanged()
    }
    fun añadirListView2(
        adaptador: ArrayAdapter<BEntrenador>,
        item: BEntrenador,
        arreglo: ArrayList<BEntrenador>
    ){
        arreglo.add(item)
        adaptador.notifyDataSetChanged()
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.menu,menu)
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val id = info.position
        posItemSeleccionado = id
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when(item?.itemId){
            R.id.mi_editar ->{
                Log.i(
                    "intent-explicito","Editar" +
                        " ${BaseDatosMemoria.arregloEntrenadores[posItemSeleccionado]}"
                )
                return true
            }
            R.id.mi_eliminar->{
                Log.i(
                        "intent-explicito","Eliminar" +
                        " ${BaseDatosMemoria.arregloEntrenadores[posItemSeleccionado]}"
                )
                return true
            }
            else-> super.onContextItemSelected(item)
        }

    }

}