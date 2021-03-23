package com.example.gamenode1

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class ActividadMenuPrincipal : AppCompatActivity() {
    lateinit var fragmentoPrincipal: Fragment
    lateinit var storage:FirebaseStorage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_menu_principal)
        //storage = Firebase.storage
        storage = AuthUsuario.storage
        //AuthUsuario.storage
        //storage.reference.activeDownloadTasks.clear()

        //Mandar a cargar lo que necesito
        //cargarEnMemoria()
        //wait para que cargue todo
        Thread.sleep(2_000)
        iniciarHeaderPrincipal()
        crearFragmento(FragmentHome())
    }



    @SuppressLint("ResourceAsColor")
    fun iniciarHeaderPrincipal(){
        val btnPrincipalHome = findViewById<LinearLayout>(R.id.ll_principal_home)
        val btnPrincipalSearch = findViewById<LinearLayout>(R.id.ll_principal_search)
        val btnPrincipalGroups = findViewById<LinearLayout>(R.id.ll_principal_groups)
        val btnPrincipalAccount = findViewById<LinearLayout>(R.id.ll_principal_account)
        val btnPrincipalHomeColor = findViewById<TextView>(R.id.tv_principal_home_color)
        val btnPrincipalSearchColor = findViewById<TextView>(R.id.tv_principal_search_color)
        val btnPrincipalGroupsColor = findViewById<TextView>(R.id.tv_principal_groups_color)
        val btnPrincipalAccountColor = findViewById<TextView>(R.id.tv_principal_account_color)

        btnPrincipalHomeColor.setBackgroundColor(R.color.black)
        btnPrincipalSearchColor.setBackgroundColor(0)
        btnPrincipalGroupsColor.setBackgroundColor(0)
        btnPrincipalAccountColor.setBackgroundColor(0)

        btnPrincipalHome.setOnClickListener{
            btnPrincipalHomeColor.setBackgroundColor(R.color.black)
            btnPrincipalSearchColor.setBackgroundColor(0)
            btnPrincipalGroupsColor.setBackgroundColor(0)
            btnPrincipalAccountColor.setBackgroundColor(0)
            //AuthUsuario.storage.getReference().getStream().cancel()
            //AuthUsuario.storage.reference.getStream().cancel()
            crearFragmento(FragmentHome())
        }
        btnPrincipalSearch.setOnClickListener{
            btnPrincipalHomeColor.setBackgroundColor(0)
            btnPrincipalSearchColor.setBackgroundColor(R.color.black)
            btnPrincipalGroupsColor.setBackgroundColor(0)
            btnPrincipalAccountColor.setBackgroundColor(0)
            //AuthUsuario.storage.getReference().getStream().cancel()
            //AuthUsuario.storage.reference.getStream().cancel()
            crearFragmento(FragmentSearch())
        }
        btnPrincipalGroups.setOnClickListener{
            btnPrincipalHomeColor.setBackgroundColor(0)
            btnPrincipalSearchColor.setBackgroundColor(0)
            btnPrincipalGroupsColor.setBackgroundColor(R.color.black)
            btnPrincipalAccountColor.setBackgroundColor(0)
            //AuthUsuario.storage.getReference().getStream().cancel()
            //AuthUsuario.storage.reference.getStream().cancel()
            crearFragmento(FragmentGroups())
        }
        btnPrincipalAccount.setOnClickListener{
            btnPrincipalHomeColor.setBackgroundColor(0)
            btnPrincipalSearchColor.setBackgroundColor(0)
            btnPrincipalGroupsColor.setBackgroundColor(0)
            btnPrincipalAccountColor.setBackgroundColor(R.color.black)
            //AuthUsuario.storage.getReference().getStream().cancel()
            //AuthUsuario.storage.reference.getStream().cancel()
            crearFragmento(FragmentAccount())
        }

    }

    fun crearFragmento(fragment: Fragment,argumentos :Bundle?=null){
        val fragmentManager = supportFragmentManager
        //Transacciones
        val fragmentTransaction = fragmentManager.beginTransaction()
        //Crear instancia de fragmento
        val primerFragmento = fragment

        primerFragmento.arguments=argumentos

        //Añadir Fragmento
        fragmentTransaction.replace(R.id.rl_fragment_principal,primerFragmento)
        fragmentoPrincipal = primerFragmento
        fragmentTransaction.commit()
    }
}