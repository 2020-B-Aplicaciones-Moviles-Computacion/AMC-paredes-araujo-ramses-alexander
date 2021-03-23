package com.example.gamenode1

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.gamenode1.dto.FirestoreGrupoDTO
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class ListAdapterGrupo(

) : RecyclerView.Adapter<ListAdapterGrupo.MyViewHolder>(){
    private var listaGrupos = emptyList<FirestoreGrupoDTO>()
    lateinit var storage: FirebaseStorage
    private lateinit var context: Context

    fun setContext(con: Context) {
        context=con
    }
    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener
    {
        init {
            val cardView = itemView.findViewById<CardView>(R.id.cv_group)
            cardView.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(menu: ContextMenu?, view: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
            if (menu != null) {
                if (view != null) {
                    //menu.add(this.adapterPosition,121,0,"${view.context.getText(R.string.cm_op_editar)  }")
                    menu.add(this.adapterPosition,888,0,"${this.itemView.context.getString(R.string.visit_group)}")
                    //menu.add(this.adapterPosition,123,2,"${view.context.getText(R.string.cm_op_capturas)}")
                }
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListAdapterGrupo.MyViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.custom_row_groups,
                parent,
                false
            )

        //storage = Firebase.storage
        storage= AuthUsuario.storage
        return ListAdapterGrupo.MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listaGrupos.size
    }

    override fun onBindViewHolder(holder: ListAdapterGrupo.MyViewHolder, position: Int) {
        val grupo = listaGrupos[position]
        holder.itemView.findViewById<TextView>(R.id.tv_group_name).text=grupo.nombre
        holder.itemView.findViewById<TextView>(R.id.tv_group_description).text=grupo.descripcion
        holder.itemView.findViewById<TextView>(R.id.tv_group_game).text=grupo.juego?.nombre
        descargarImagenPerfil(grupo.juego?.foto!!,holder.itemView.findViewById<ImageView>(R.id.iv_group_game_of_group))
    }
    fun setData(listaPublis: List<FirestoreGrupoDTO>){
        this.listaGrupos= listaPublis
        notifyDataSetChanged()
    }
    fun descargarImagenPerfil(nameImageAccount:String, imageView: ImageView) {

        //gs://moviles-computacion-gamenodepa.appspot.com/20210312_003235Fondo.jpg
        val gsReference = storage.getReferenceFromUrl("gs://moviles-computacion-gamenodepa.appspot.com/${nameImageAccount}.jpg")
        var bitmap: Bitmap? = null
        // 1024*1024*5 800000
        gsReference.getBytes(1024*1024*5).addOnSuccessListener {
            bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            Log.i("image","se obtuvo la imagen perfil")

            // val imageView =  findViewById<ImageView>(R.id.iv_account_profile_picture)
            imageView?.setImageBitmap(bitmap)

        }.addOnFailureListener{

        }

    }

}