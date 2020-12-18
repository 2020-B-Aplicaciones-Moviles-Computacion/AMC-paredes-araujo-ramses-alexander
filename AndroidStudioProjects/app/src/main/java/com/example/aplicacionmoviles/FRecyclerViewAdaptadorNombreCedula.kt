package com.example.aplicacionmoviles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import org.w3c.dom.Text

class FRecyclerViewAdaptadorNombreCedula (
    private val listaEntrenador:  List<BEntrenador>,
    private val contexto:Class<*>,
    private val recyclerView: androidx.recyclerview.widget.RecyclerView
): androidx.recyclerview.widget.RecyclerView.Adapter<
        FRecyclerViewAdaptadorNombreCedula.MyViewHolder
        >(){
    inner class MyViewHolder(view: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(view){
        val nombreTextView: TextView
        val cedulaTextView: TextView
        val likesTextView: TextView
        val accionButton: Button
        var numeroLikes =0
        init {
            nombreTextView = view.findViewById(R.id.tv_nombre)
            cedulaTextView= view.findViewById(R.id.tv_cedula)
            likesTextView= view.findViewById(R.id.tv_likes)
            accionButton = view.findViewById(R.id.btn_dar_like)
            accionButton.setOnClickListener {
                this.añadirLike()
            }
        }
        fun añadirLike(){
            this.numeroLikes = this.numeroLikes+1
            likesTextView.text = this.numeroLikes.toString()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.recycler_view_vista,
                parent,
                false
            )
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return  listaEntrenador.size
    }

    override fun onBindViewHolder(holder: FRecyclerViewAdaptadorNombreCedula.MyViewHolder, position: Int) {
        val usuario = listaEntrenador[position]
        holder.nombreTextView.text = usuario.nombre
        holder.cedulaTextView.text = usuario.descripcion
        holder.accionButton.text = "Like ${usuario.nombre}"
        holder.likesTextView.text = "0"
    }
}