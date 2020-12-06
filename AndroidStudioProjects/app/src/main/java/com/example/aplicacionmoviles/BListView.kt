package com.example.aplicacionmoviles

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView

class BListView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b_list_view)
        val listaNumeros = BaseDatosMemoria.arregloEnteros
        val listaEntrenadores = BaseDatosMemoria.arregloEntrenadores

        //listaNumeros.add(1)
        //listaNumeros.add(2)
        //listaNumeros.add(3)
        //listaNumeros.add(4)

        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,// Layout (xml visual) existe en Android
            listaEntrenadores
        )
        val listView = findViewById<ListView>(R.id.lv_entrenador)
        listView.adapter = adaptador
        //listaNumeros.add(5)
        //listaNumeros.add(6)
        adaptador.notifyDataSetChanged()
        val botonAñadirLV = findViewById<Button>(R.id.btn_anadir_item_lv)
        botonAñadirLV.setOnClickListener {
            añadirListView2(adaptador,BEntrenador("1","1"),listaEntrenadores)
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

}