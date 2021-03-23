package com.example.examenpokentrpp1

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import java.util.*

class SpinnerActivity : AppCompatActivity() {


    lateinit var result : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spinner)


        result = findViewById(R.id.tv_result) as TextView

        //AQUI VAMOS A VER LO DEL CALENDARIO
        val c= Calendar.getInstance()
        val year= c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        //ACCIONAMOS CON UN BTN

        val botonDate = findViewById<Button>(R.id.btn_pick_date)
        botonDate.setOnClickListener {
            var dpd = DatePickerDialog(this,
                    DatePickerDialog.OnDateSetListener {
                        view, mYear,mMonth , mDay ->
                        val mmMonth = mMonth+1
                        val date = "$mDay/$mmMonth/$mYear"
                        result.setText(date)
                    },year,month,day)
            dpd.show()
        }





    }
}