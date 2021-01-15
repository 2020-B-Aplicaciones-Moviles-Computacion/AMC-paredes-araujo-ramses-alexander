package com.example.examenpokentrpp1.activEntren


import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.examenpokentrpp1.R
import com.example.examenpokentrpp1.model.Entrenador
import kotlinx.android.synthetic.main.custom_row_entrenadores2.view.*


class ListAdapterEntrenador(

) : RecyclerView.Adapter<ListAdapterEntrenador.MyViewHolder>(

    ) {

    private var listaEntrenadores = emptyList<Entrenador>()

    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView),View.OnCreateContextMenuListener{

        init {
            val cardView = itemView.findViewById<CardView>(R.id.cv_layout_entrenadores)
            cardView.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(menu: ContextMenu?, view: View?, menuInfo: ContextMenuInfo?) {
            //val string: String = getString(,"")

            if (menu != null) {
                if (view != null) {
                    menu.add(this.adapterPosition,121,0,"${view.context.getText(R.string.cm_op_editar)  }")
                    menu.add(this.adapterPosition,122,1,"${view.context.getText(R.string.cm_op_eliminar)}")
                    menu.add(this.adapterPosition,123,2,"${view.context.getText(R.string.cm_op_capturas)}")
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapterEntrenador.MyViewHolder {
        val itemView = LayoutInflater
                .from(parent.context)
                .inflate(
                        R.layout.custom_row_entrenadores2,
                        parent,
                        false
                )
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listaEntrenadores.size
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val entrenador = listaEntrenadores[position]
        holder.itemView.tv_cr_entrenador_contador.text = (position+1).toString()
        holder.itemView.tv_cr_entrenador_numCed.text = entrenador.idEntrenador
        holder.itemView.tv_cr_entrenador_nombre.text = entrenador.nombre
        holder.itemView.tv_cr_entrenador_genero.text = entrenador.genero.toString()
        holder.itemView.tv_cr_entrenador_fecha.text = entrenador.fechaNac
        holder.itemView.tv_cr_entrenador_activo.text = entrenador.activo.toString()
    }

    fun setData(listaEntrenadoress: List<Entrenador>){
        this.listaEntrenadores= listaEntrenadoress
        notifyDataSetChanged()
    }
}
