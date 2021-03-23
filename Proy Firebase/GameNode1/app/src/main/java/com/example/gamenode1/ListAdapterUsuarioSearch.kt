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
import com.example.gamenode1.dto.FirestoreInfoUsuarioDTO
import com.example.gamenode1.dto.FirestorePublicacionDTO
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class ListAdapterUsuarioSearch(

) : RecyclerView.Adapter<ListAdapterUsuarioSearch.MyViewHolder>() {
    private var listaUsuarios = emptyList<FirestoreInfoUsuarioDTO>()

    lateinit var storage: FirebaseStorage
    private lateinit var context: Context

    fun setContext(con: Context) {
        context=con
    }

    class MyViewHolder (itemView: View):RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener {
        init {
            val cardView = itemView.findViewById<CardView>(R.id.cv_search_usuario)
            cardView.setOnCreateContextMenuListener(this)
        }
        override fun onCreateContextMenu(
            menu: ContextMenu?, view: View?, menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            if (menu != null) {
                if (view != null) {
                    //menu.add(this.adapterPosition,121,0,"${view.context.getText(R.string.cm_op_editar)  }")
                    menu.add(this.adapterPosition,999,0,"${this.itemView.context.getString(R.string.visit_profile)}")
                    //menu.add(this.adapterPosition,123,2,"${view.context.getText(R.string.cm_op_capturas)}")
                }
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListAdapterUsuarioSearch.MyViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.custom_row_usuarios_search,
                parent,
                false
            )

        storage = Firebase.storage
        return ListAdapterUsuarioSearch.MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listaUsuarios.size
    }

    override fun onBindViewHolder(holder: ListAdapterUsuarioSearch.MyViewHolder, position: Int) {
        val usuario = listaUsuarios[position]
        holder.itemView.findViewById<TextView>(R.id.tv_search_usuario_nombre).text=usuario.name
        descargarImagenPerfil(usuario.fotoPerfil, holder.itemView.findViewById<ImageView>(R.id.iv_search_usuario))
    }
    fun setData(listaUsr: List<FirestoreInfoUsuarioDTO>){
        this.listaUsuarios= listaUsr
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