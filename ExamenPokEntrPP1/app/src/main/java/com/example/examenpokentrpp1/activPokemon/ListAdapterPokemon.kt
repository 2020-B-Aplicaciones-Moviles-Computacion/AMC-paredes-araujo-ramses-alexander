package com.example.examenpokentrpp1.activPokemon

import android.content.Context
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.examenpokentrpp1.R
import com.example.examenpokentrpp1.model.Pokemon
import kotlinx.android.synthetic.main.custom_row_pokemon.view.*

class ListAdapterPokemon(

) : RecyclerView.Adapter<ListAdapterPokemon.MyViewHolder>() {

    private var listaPokemon = emptyList<Pokemon>()
    private lateinit var context: Context

    fun setContext(con: Context) {
        context=con
    }



    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener{

        init {
            val cardView = itemView.findViewById<CardView>(R.id.cv_layout_pokemon)
            cardView.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(menu: ContextMenu?, view: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
            if (menu != null) {
                //menu = view.findViewById<ContextMenu>(R.menu.menu) as ContextMenu?
                if (view != null) {
                    menu.add(this.adapterPosition,121,0,"${view.context?.getText(R.string.cm_op_editar)}")
                    menu.add(this.adapterPosition,122,1,"${view.context?.getText(R.string.cm_op_eliminar)}")//Eliminar
                }//Editar

            }
        }

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListAdapterPokemon.MyViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.custom_row_pokemon,
                parent,
                false
            )
        return ListAdapterPokemon.MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listaPokemon.size
    }



    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val pokemon = listaPokemon[position]
        holder.itemView.tv_cr_pokemon_npokedex.text = pokemon.nPokedex.toString()
        holder.itemView.tv_cr_pokemon_nombre.text = pokemon.nombre
        //val context = s
        //---------------------------------------------------------
        val options = arrayOf(
                "${ context.getText(R.string.pokemon_type_1)}",
                "${context.getText(R.string.pokemon_type_2)}",
                "${context.getText(R.string.pokemon_type_3)}",
                "${context.getText(R.string.pokemon_type_4)}",
                "${context.getText(R.string.pokemon_type_5)}",
                "${context.getText(R.string.pokemon_type_6)}",
                "${context.getText(R.string.pokemon_type_7)}",
                "${context.getText(R.string.pokemon_type_8)}",
                "${context.getText(R.string.pokemon_type_9)}",
                "${context.getText(R.string.pokemon_type_10)}",
                "${context.getText(R.string.pokemon_type_11)}",
                "${context.getText(R.string.pokemon_type_12)}"
        )
        holder.itemView.tv_cr_pokemon_tipo.text = options.get(pokemon.tipo)
        //------------------------------------------------------------
        holder.itemView.tv_cr_pokemon_altura.text = pokemon.altura.toString()
        holder.itemView.tv_cr_pokemon_peso.text = pokemon.peso.toString()
    }
    fun setData(listaPokemon: List<Pokemon>){
        this.listaPokemon=listaPokemon
        notifyDataSetChanged()
    }
}