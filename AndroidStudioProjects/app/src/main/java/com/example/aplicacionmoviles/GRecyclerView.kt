package com.example.aplicacionmoviles

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GRecyclerView : AppCompatActivity() {
    var totalLikes = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_c_recycler_view)

        val listaEntrenador = arrayListOf<BEntrenador>()
        listaEntrenador.add(
            BEntrenador("Ramses","123",
            DLiga("kanto","kanto fin"))
        )
        listaEntrenador.add(
            BEntrenador("Paredes","456",
                DLiga("kanto","kanto"))
        )
        val recyclerViewEntrenador = findViewById<RecyclerView>(
            R.id.rv_entrenadores
        )
        this.iniciarRecyclerView(
            listaEntrenador,
            this,
            recyclerViewEntrenador
        )
    }
    fun iniciarRecyclerView(
        lista: List<BEntrenador>,
        actividad: GRecyclerView,
        recyclerView: androidx.recyclerview.widget.RecyclerView
    ){
        val adaptador = FRecyclerViewAdaptadorNombreCedula(
            lista,
            actividad,
            recyclerView
        )
        recyclerView.adapter=adaptador
        recyclerView.itemAnimator= androidx.recyclerview.widget.DefaultItemAnimator()
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(actividad)
        adaptador.notifyDataSetChanged()
    }
    fun aumentarTotalLikes(){
        totalLikes= totalLikes+1
        val textView = findViewById<TextView>(
            R.id.tv_total_likes
        )
        textView.text = totalLikes.toString()
    }
}