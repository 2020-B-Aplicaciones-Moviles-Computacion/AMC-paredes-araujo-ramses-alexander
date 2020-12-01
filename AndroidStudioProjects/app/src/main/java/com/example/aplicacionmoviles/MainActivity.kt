package com.example.aplicacionmoviles

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonCicloVida = findViewById<Button>(R.id.button_ir_ciclo_vida)
        buttonCicloVida.setOnClickListener {
            irCicloVida()
        }

    }
    fun irCicloVida(){
        val intentExplicito = Intent(
            this,
            ACicloVida:: class.java
        )
        startActivity(intentExplicito)
    }
}