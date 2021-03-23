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
import com.example.gamenode1.dto.FirestoreComentarioDTO
import com.example.gamenode1.dto.FirestorePublicacionDTO
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class ListAdapterComentario(

) : RecyclerView.Adapter<ListAdapterComentario.MyViewHolder>(){

    private var listaComentarios = emptyList<FirestoreComentarioDTO>()
    lateinit var storage: FirebaseStorage
    private lateinit var context: Context
    //cv_layout_comentarios
    fun setContext(con: Context) {
        context=con
    }
    class MyViewHolder  (itemView: View):RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener  {
        init {
            val cardView = itemView.findViewById<CardView>(R.id.cv_layout_comentarios)
            cardView.setOnCreateContextMenuListener(this)
        }
        override fun onCreateContextMenu(menu: ContextMenu?, view: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
            if (menu != null) {
                if (view != null) { // getString(R.string.eliminar_comentario)
                    menu.add(this.adapterPosition,444,0,"${ this.itemView.context.getString(R.string.eliminar_comentario) }")
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapterComentario.MyViewHolder {

        val itemView = LayoutInflater
                .from(parent.context)
                .inflate(
                        R.layout.custom_row_comentarios,
                        parent,
                        false
                )

        storage = Firebase.storage
        return ListAdapterComentario.MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listaComentarios.size
    }

    override fun onBindViewHolder(holder: ListAdapterComentario.MyViewHolder, position: Int) {
        val comentario = listaComentarios[position]
        holder.itemView.findViewById<TextView>(R.id.tv_publication_comment_list_user_name).setText(comentario.usuario?.nombre)
        holder.itemView.findViewById<TextView>(R.id.tv_publication_comment_list_date).setText(comentario.fechaAgregacionComentario?.toDate().toString())
        holder.itemView.findViewById<TextView>(R.id.tv_publication_comment_list_content).setText(comentario.descripcion)
        descargarImagenPerfil(comentario.usuario?.foto!!,holder.itemView.findViewById<ImageView>(R.id.iv_publication_comment_list_picture))
    }
    fun setData(listaComments: List<FirestoreComentarioDTO>){
        this.listaComentarios= listaComments
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