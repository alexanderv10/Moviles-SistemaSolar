package com.example.deber

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        BaseDeDatos.tablaSistemaSolarPlaneta = SqliteHelper(this)

        val btnVerSistemasSolares = findViewById<Button>(R.id.btn_ver_sistemas_solares)
        btnVerSistemasSolares.setOnClickListener {
            irActividad(SistemaSolarListView::class.java)
        }
    }

    private fun irActividad(clase: Class<*>) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }
}
