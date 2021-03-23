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
import com.example.gamenode1.dto.FirestoreLibreriaDTO
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class ListAdapterLibreria(

) : RecyclerView.Adapter<ListAdapterLibreria.MyViewHolder>() {
    private var listaJuegosLibreria = emptyList<FirestoreLibreriaDTO>()
    lateinit var storage: FirebaseStorage

    private lateinit var context: Context

    fun setContext(con: Context) {
        context=con
    }

    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener {
        init {
            val cardView = itemView.findViewById<CardView>(R.id.cv_layout_libreria)
            cardView.setOnCreateContextMenuListener(this)
        }
        override fun onCreateContextMenu(menu: ContextMenu?, view: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
            if (menu != null) {
                if (view != null) {
                    //menu.add(this.adapterPosition,121,0,"${view.context.getText(R.string.cm_op_editar)  }")
                    //menu.add(this.adapterPosition,111,0,"Delete")
                    //menu.add(this.adapterPosition,123,2,"${view.context.getText(R.string.cm_op_capturas)}")
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapterLibreria.MyViewHolder {
        val itemView = LayoutInflater
                .from(parent.context)
                .inflate(
                        R.layout.custom_row_library_games,
                        parent,
                        false
                )

        storage = Firebase.storage
        return ListAdapterLibreria.MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listaJuegosLibreria.size
    }

    override fun onBindViewHolder(holder: ListAdapterLibreria.MyViewHolder, position: Int) {
        val juegoLibreria = listaJuegosLibreria[position]
        //holder.itemView.tv_libreria_titulo = juegoLibreria.juego?.nombre
        holder.itemView.findViewById<TextView>(R.id.tv_libreria_titulo).setText(juegoLibreria.juego?.nombre)
        holder.itemView.findViewById<TextView>(R.id.tv_libreria_developer).setText(juegoLibreria.juego?.desarrolladora)
        holder.itemView.findViewById<TextView>(R.id.tv_libreria_type).setText(juegoLibreria.juego?.tipo)
        holder.itemView.findViewById<TextView>(R.id.tv_publication_comment_date).setText(
            juegoLibreria.fechaAgregacion?.toDate().toString())
        //descargarImagenPerfil(juegoLibreria.juego?.fotoJuego!!,holder.itemView.findViewById<ImageView>(R.id.iv_libreria))
        val imageView = holder.itemView.findViewById<ImageView>(R.id.iv_libreria)
        val gsReference = storage.getReferenceFromUrl("gs://moviles-computacion-gamenodepa.appspot.com/${juegoLibreria.juego?.fotoJuego}.jpg")
        var bitmap: Bitmap? = null
        // 1024 * 1024 * 5   800000
        gsReference.getBytes(1024 * 1024 * 5 ).addOnSuccessListener {
            bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            Log.i("image","se obtuvo la imagen perfil")

            // val imageView =  findViewById<ImageView>(R.id.iv_account_profile_picture)
            imageView?.setImageBitmap(bitmap)

        }.addOnFailureListener{

        }



       /* holder.itemView.tv_
                .text = (position+1).toString()
        holder.itemView.tv_cr_entrenador_numCed.text = entrenador.idEntrenador
        holder.itemView.tv_cr_entrenador_nombre.text = entrenador.nombre
        holder.itemView.tv_cr_entrenador_genero.text = entrenador.genero.toString()
        holder.itemView.tv_cr_entrenador_fecha.text = entrenador.fechaNac
        holder.itemView.tv_cr_entrenador_activo.text = entrenador.activo.toString()*/
    }

    fun setData(listaJuegos: List<FirestoreLibreriaDTO>){
        this.listaJuegosLibreria= listaJuegos
        notifyDataSetChanged()
    }
    fun descargarImagenPerfil(nameImageAccount:String, imageView: ImageView) {

        //gs://moviles-computacion-gamenodepa.appspot.com/20210312_003235Fondo.jpg
        val gsReference = storage.getReferenceFromUrl("gs://moviles-computacion-gamenodepa.appspot.com/${nameImageAccount}.jpg")
        var bitmap: Bitmap? = null

        gsReference.getBytes(800000).addOnSuccessListener {
            bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            Log.i("image","se obtuvo la imagen perfil")

           // val imageView =  findViewById<ImageView>(R.id.iv_account_profile_picture)
            imageView?.setImageBitmap(bitmap)

        }.addOnFailureListener{

        }

    }
}