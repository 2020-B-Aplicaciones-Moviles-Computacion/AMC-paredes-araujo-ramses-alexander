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
import com.example.gamenode1.dto.FirestorePublicacionGrupoDTO
import com.google.firebase.storage.FirebaseStorage

class ListAdapterGroupPublication(

): RecyclerView.Adapter<ListAdapterGroupPublication.MyViewHolder>() {

    private var listaPublicacionesGrupo = emptyList<FirestorePublicacionGrupoDTO>()

    lateinit var storage: FirebaseStorage
    private lateinit var context: Context

    fun setContext(con: Context) {
        context=con
    }
    class MyViewHolder (itemView: View):RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener{
        init {
            val cardView = itemView.findViewById<CardView>(R.id.cv_publicacion_grupo)
            cardView.setOnCreateContextMenuListener(this)
        }
        override fun onCreateContextMenu(menu: ContextMenu?, view: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
            if (menu != null) {
                if (view != null) {
                    //menu.add(this.adapterPosition,121,0,"${view.context.getText(R.string.cm_op_editar)  }")
                    //menu.add(this.adapterPosition,888,0,"Visit Group")
                    //menu.add(this.adapterPosition,123,2,"${view.context.getText(R.string.cm_op_capturas)}")
                }
            }
        }

    }

    override fun onCreateViewHolder( //cv_publicacion_grupo
        parent: ViewGroup,
        viewType: Int
    ): ListAdapterGroupPublication.MyViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.custom_row_publicaciones_grupo,
                parent,
                false
            )

        //storage = Firebase.storage
        storage= AuthUsuario.storage
        return ListAdapterGroupPublication.MyViewHolder(itemView)

    }

    override fun getItemCount(): Int {
        return  listaPublicacionesGrupo.size
    }

    override fun onBindViewHolder(holder: ListAdapterGroupPublication.MyViewHolder, position: Int) {
        val publicacionGrupo = listaPublicacionesGrupo[position]
        holder.itemView.findViewById<TextView>(R.id.tv_group_publication_group_name).setText(publicacionGrupo.asignacion?.grupoNombre)
        holder.itemView.findViewById<TextView>(R.id.tv_group_publication_comment_title).setText(publicacionGrupo.titulo)
        holder.itemView.findViewById<TextView>(R.id.tv_group_publication_comment_date).setText(publicacionGrupo.fechaAgregacionPublicacion?.toDate().toString())
        holder.itemView.findViewById<TextView>(R.id.tv_group_publication_comment_description).setText(publicacionGrupo.descripcion)
        holder.itemView.findViewById<TextView>(R.id.tv_group_publication_comment_name).setText("-"+publicacionGrupo.asignacion?.usuarioNombre)
        holder.itemView.findViewById<TextView>(R.id.tv_group_publication_comment_game).setText("-"+publicacionGrupo.asignacion?.juegoNombre)
        descargarImagenPerfil(publicacionGrupo.asignacion?.usuarioFoto!!,holder.itemView.findViewById<ImageView>(R.id.iv_group_publication_comment_profile_picture))
        descargarImagenPerfil(publicacionGrupo.asignacion?.juegoFoto!!,holder.itemView.findViewById<ImageView>(R.id.iv_group_publication_comment_game))


        //iv_group_publication_comment_profile_picture
    }
    fun setData(listaPublis: List<FirestorePublicacionGrupoDTO>){
        this.listaPublicacionesGrupo= listaPublis
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