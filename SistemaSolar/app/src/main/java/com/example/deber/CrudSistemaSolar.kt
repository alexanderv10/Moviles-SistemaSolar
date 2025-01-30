package com.example.deber

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class CrudSistemaSolar : AppCompatActivity() {
    private fun mostrarSnackbar(texto: String) {
        Snackbar.make(findViewById(R.id.cl_crud_sistema_solar), texto, Snackbar.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crud_sistema_solar)

        val btnGuardar = findViewById<Button>(R.id.btn_guardar_sistema_solar)
        val inputNombre = findViewById<EditText>(R.id.input_nombre_sistema_solar)
        val inputDescripcion = findViewById<EditText>(R.id.input_descripcion_sistema_solar)
        val inputEdad = findViewById<EditText>(R.id.input_edad_sistema_solar)
        val inputTipoEstrella = findViewById<EditText>(R.id.input_tipo_estrella)
        val switchHabitable = findViewById<Switch>(R.id.switch_habitable)

        val modo = intent.getStringExtra("modo") ?: "crear"
        val sistemaSolar: SistemaSolar? = intent.getParcelableExtra("sistemaSolar")

        if (modo == "editar" && sistemaSolar != null) {
            inputNombre.setText(sistemaSolar.nombre)
            inputDescripcion.setText(sistemaSolar.descripcion)
            inputEdad.setText(sistemaSolar.edad.toString())
            inputTipoEstrella.setText(sistemaSolar.tipoEstrella)
            switchHabitable.isChecked = sistemaSolar.esHabitable
            btnGuardar.text = getString(R.string.actualizar)
        } else {
            btnGuardar.text = getString(R.string.crear)
        }

        btnGuardar.setOnClickListener {
            val nombre = inputNombre.text.toString().trim()
            val descripcion = inputDescripcion.text.toString().trim()
            val edad = inputEdad.text.toString().trim().toIntOrNull() ?: 0
            val tipoEstrella = inputTipoEstrella.text.toString().trim()
            val esHabitable = switchHabitable.isChecked

            if (nombre.isEmpty() || descripcion.isEmpty() || tipoEstrella.isEmpty()) {
                mostrarSnackbar("Por favor, completa todos los campos.")
                return@setOnClickListener
            }

            if (modo == "crear") {
                val respuesta = BaseDeDatos.tablaSistemaSolarPlaneta?.crearSistemaSolar(
                    nombre, descripcion, edad, tipoEstrella, esHabitable
                )
                if (respuesta == true) {
                    mostrarSnackbar("Sistema Solar creado correctamente.")
                    setResult(RESULT_OK)
                    finish()
                } else {
                    mostrarSnackbar("Error al crear el Sistema Solar.")
                }
            } else if (modo == "editar" && sistemaSolar != null) {
                val respuesta = BaseDeDatos.tablaSistemaSolarPlaneta?.actualizarSistemaSolar(
                    nombre, descripcion, edad, tipoEstrella, esHabitable, sistemaSolar.id
                )
                if (respuesta == true) {
                    mostrarSnackbar("Sistema Solar actualizado correctamente.")
                    setResult(RESULT_OK)
                    finish()
                } else {
                    mostrarSnackbar("Error al actualizar el Sistema Solar.")
                }
            }
        }
    }
}
