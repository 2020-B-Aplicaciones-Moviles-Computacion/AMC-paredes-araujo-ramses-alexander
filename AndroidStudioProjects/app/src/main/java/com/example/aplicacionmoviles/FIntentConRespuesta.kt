package com.example.aplicacionmoviles

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Button

class FIntentConRespuesta : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_f_intent_con_respuesta)
        val botonSeleccionarContacto = findViewById<Button>(
            R.id.btn_selecccionar_contacto
        )
        botonSeleccionarContacto
            .setOnClickListener {
                val intentConRespuesta = Intent(
                    Intent.ACTION_PICK,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                )
                startActivityForResult(intentConRespuesta,304)
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            304->{
                if(resultCode== RESULT_OK){
                    Log.i("resultado","Si selecciono contacto")
                    var uri = data?.data
                    if(uri!=null){
                        val cursor = contentResolver.query(
                                uri,
                            null,
                            null,
                            null,
                            null,
                            null
                                )
                        cursor?.moveToFirst()
                        val indiceTelefono = cursor?.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER
                        )
                        val telefono = cursor?.getString(
                            indiceTelefono!!
                        )
                        cursor?.close()
                        Log.i("resultado","Telefono ${telefono}")
                    }

                }else{
                    Log.i("resultado","No selecciono un contacto")
                }

            }
        }
    }
}