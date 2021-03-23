package com.example.examenpokentrpp1.activCapturasEntrenador

import android.content.Context
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.examenpokentrpp1.R
import com.example.examenpokentrpp1.activEntren.ListAdapterEntrenador
import com.example.examenpokentrpp1.activPokemon.VistaListaPokemon
import com.example.examenpokentrpp1.model.Captura
import com.example.examenpokentrpp1.model.Entrenador
import com.example.examenpokentrpp1.model.Pokemon
import com.example.examenpokentrpp1.viewModel.CapturaViewModel
import com.example.examenpokentrpp1.viewModel.PokemonViewModel
import kotlinx.android.synthetic.main.custom_row_capturas.view.*

class ListAdapterCaptura(): RecyclerView.Adapter<ListAdapterCaptura.MyViewHolder>() {

    private var listaCapturas = emptyList<Captura>()
    private var listaPokemon = emptyList<Pokemon>()

    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener {
        init {
            val cardView = itemView.findViewById<CardView>(R.id.cv_layout_capturas)
            cardView.setOnCreateContextMenuListener(this)
        }



        override fun onCreateContextMenu(
            menu: ContextMenu?,
            view: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            if (menu != null) {
                if (view != null) {
                    menu.add(this.adapterPosition,111,1,"${view.context.getText(R.string.cm_op_eliminar)}")
                }
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListAdapterCaptura.MyViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.custom_row_capturas,
                parent,
                false
            )
        return ListAdapterCaptura.MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listaCapturas.size
    }

    override fun onBindViewHolder(holder: ListAdapterCaptura.MyViewHolder, position: Int) {
        val captura = listaCapturas[position]
        holder.itemView.tv_cr_contador_capturas.text = (position+1).toString()
        //Aqui obtengo datos del pokemon y pongo su numero y nombre

        holder.itemView.tv_nPokedex_listado_capturas.text=""
        holder.itemView.tv_nombre_pokemon_listado_capturas.text=""

        if (listaPokemon!=null){
            for (pokemon in listaPokemon){
                if (captura.idBasePokemon== pokemon.idPokemon){
                    holder.itemView.tv_nPokedex_listado_capturas.text=pokemon.nPokedex.toString()
                    holder.itemView.tv_nombre_pokemon_listado_capturas.text=pokemon.nombre
                }
            }
        }
        //--------------------------------------------------------
        holder.itemView.tv_genero_listado_capturas.text= captura.generoPokemon.toString()
        holder.itemView.tv_nivel_listado_capturas.text= captura.nivel.toString()
        holder.itemView.tv_fecha_listado_capturas.text=captura.fechaCaptura
        holder.itemView.tv_tipo_pokeball_listado_capturas.text=captura.tipoPokeball
        holder.itemView.tv_life_points_listado_capturas.text = captura.lifePoints.toString()
        holder.itemView.tv_fuerza_listado_capturas.text=captura.fuerza.toString()

    }
    fun setData(listaCapturas: List<Captura>){
        this.listaCapturas= listaCapturas
        notifyDataSetChanged()
    }
    fun setDataListaPokemon(listaPokemon: List<Pokemon>){
        this.listaPokemon= listaPokemon
    }
}