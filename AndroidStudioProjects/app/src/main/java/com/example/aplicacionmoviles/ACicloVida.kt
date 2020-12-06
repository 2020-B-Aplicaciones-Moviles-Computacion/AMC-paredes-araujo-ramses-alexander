package com.example.aplicacionmoviles

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

class ACicloVida : AppCompatActivity() {
    var total =0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a_ciclo_vida)
        Log.i("ciclo-vida","onCreate")
        val botonSumar = findViewById<Button>(R.id.btn_ciclo_vida)
        val textoTotal  = findViewById<TextView>(R.id.txv_ciclo_vida)

        botonSumar
            .setOnClickListener {
                total = total +1
                textoTotal.text = total.toString()
            }

    }

    override fun onSaveInstanceState(outState: Bundle) {

        Log.i("ciclo-vida","onSaveInstanceState")
        if(outState !=null){
            outState.run {
                //Aqui guardamos
                //Guardamos cualquier primitivo
                putInt("totalGuardado",total)
            }
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.i("ciclo-vida","onRestoreInstanceState")
        val totalRecuperado: Int? = savedInstanceState.getInt("totalGuardado")
        if (totalRecuperado!=null){
            this.total = totalRecuperado
            val textoTotal = findViewById<TextView>(R.id.txv_ciclo_vida)
            textoTotal.text = total.toString()
        }
    }


    override fun onStart() {
        super.onStart()
        Log.i("ciclo-vida","onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.i("ciclo-vida","onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i("ciclo-vida","onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i("ciclo-vida","onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("ciclo-vida","onDestroy")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i("ciclo-vida","onRestart")
    }
}