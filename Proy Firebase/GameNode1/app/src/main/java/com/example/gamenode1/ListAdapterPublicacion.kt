package com.example.gamenode1

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.gamenode1.dto.FirestorePublicacionDTO
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class ListAdapterPublicacion(

) : RecyclerView.Adapter<ListAdapterPublicacion.MyViewHolder>(){
    private var listaPubllicaciones = emptyList<FirestorePublicacionDTO>()
    lateinit var storage: FirebaseStorage
    private lateinit var context: Context

    fun setContext(con: Context) {
        context=con
    }
    class MyViewHolder (itemView: View):RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener {
        init {
            val cardView = itemView.findViewById<CardView>(R.id.cv_layout_publicaciones)
            cardView.setOnCreateContextMenuListener(this)
        }
        override fun onCreateContextMenu(menu: ContextMenu?, view: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
            if (menu != null) {
                if (view != null) {
                    //menu.add(this.adapterPosition,121,0,"${view.context.getText(R.string.cm_op_editar)  }")
                    menu.add(this.adapterPosition,999,0,"${this.itemView.context.getString(R.string.visit_profile)}")
                    menu.add(this.adapterPosition,111,0,"${this.itemView.context.getString(R.string.view_publication)}")
                    menu.add(this.adapterPosition,777,0,"${this.itemView.context.getString(R.string.delete_publication)}")
                    //menu.add(this.adapterPosition,123,2,"${view.context.getText(R.string.cm_op_capturas)}")
                }
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.custom_row_publicaciones,
                parent,
                false
            )

        //storage = Firebase.storage
        storage = AuthUsuario.storage
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listaPubllicaciones.size
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val publicacion = listaPubllicaciones[position]
        holder.itemView.findViewById<TextView>(R.id.tv_publication_comment_title).text=publicacion.titulo
        holder.itemView.findViewById<TextView>(R.id.tv_publication_comment_date).text= publicacion.fechaPublicacion?.toDate().toString()
        holder.itemView.findViewById<TextView>(R.id.tv_publication_comment_name).text="-"+publicacion.libreria?.nombreUsuario
        holder.itemView.findViewById<TextView>(R.id.tv_publication_comment_game).text="-"+publicacion.libreria?.nombreJuego
        holder.itemView.findViewById<TextView>(R.id.tv_publication_comment_tags).text=publicacion.etiquetas.toString()
        holder.itemView.findViewById<TextView>(R.id.tv_publication_comment_description).text=publicacion.descripcion
        descargarImagenPerfil(publicacion.libreria?.fotoUsuario!!,holder.itemView.findViewById<ImageView>(R.id.iv_publication_comment_profile_picture))
        descargarImagenPerfil(publicacion.libreria?.fotoJuego!!,holder.itemView.findViewById<ImageView>(R.id.iv_publication_comment_game))
        //iv_publicacion_profile_picture
    }

    fun setData(listaPublis: List<FirestorePublicacionDTO>){
        this.listaPubllicaciones= listaPublis
        notifyDataSetChanged()
    }
    fun descargarImagenPerfil(nameImageAccount:String, imageView: ImageView) {

        //gs://moviles-computacion-gamenodepa.appspot.com/20210312_003235Fondo.jpg
        val gsReference = AuthUsuario.storage.getReferenceFromUrl("gs://moviles-computacion-gamenodepa.appspot.com/${nameImageAccount}.jpg")
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