package com.example.deber

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class CrudPlaneta : AppCompatActivity() {

    private fun mostrarSnackbar(texto: String) {
        Snackbar.make(findViewById(R.id.cl_crud_planeta), texto, Snackbar.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crud_planeta)

        val btnGuardar = findViewById<Button>(R.id.btn_guardar_planeta)
        val inputNombre = findViewById<EditText>(R.id.input_nombre_planeta)
        val inputDistancia = findViewById<EditText>(R.id.input_distancia_sol)
        val inputTipoPlaneta = findViewById<EditText>(R.id.input_tipo_planeta)
        val inputDiametro = findViewById<EditText>(R.id.input_diametro)
        val inputLatitud = findViewById<EditText>(R.id.input_latitud)
        val inputLongitud = findViewById<EditText>(R.id.input_longitud)

        val modo = intent.getStringExtra("modo") ?: "crear"
        val planeta: Planeta? = intent.getParcelableExtra("planeta")
        val sistemaSolarId = intent.getIntExtra("sistemaSolarId", -1)

        if (sistemaSolarId == -1) {
            mostrarSnackbar("Error: No se proporcionó un ID válido del Sistema Solar.")
            finish()
            return
        }

        if (modo == "editar" && planeta != null) {
            inputNombre.setText(planeta.nombre)
            inputDistancia.setText(planeta.distanciaAlSol.toString())
            inputTipoPlaneta.setText(planeta.tipoPlaneta)
            inputDiametro.setText(planeta.diametro.toString())
            inputLatitud.setText(planeta.latitud.toString())
            inputLongitud.setText(planeta.longitud.toString())
            btnGuardar.text = getString(R.string.actualizar)
        } else {
            btnGuardar.text = getString(R.string.crear)
        }

        btnGuardar.setOnClickListener {
            val nombre = inputNombre.text.toString().trim()
            val distancia = inputDistancia.text.toString().toDoubleOrNull() ?: -1.0
            val tipoPlaneta = inputTipoPlaneta.text.toString().trim()
            val diametro = inputDiametro.text.toString().toDoubleOrNull() ?: -1.0
            val latitud = inputLatitud.text.toString().toDoubleOrNull()
            val longitud = inputLongitud.text.toString().toDoubleOrNull()

            // Validación de datos reales
            if (nombre.isEmpty() || tipoPlaneta.isEmpty() || distancia <= 0 || diametro <= 0) {
                mostrarSnackbar("Por favor, completa todos los campos con valores válidos.")
                return@setOnClickListener
            }

            if (latitud == null || longitud == null || latitud !in -90.0..90.0 || longitud !in -180.0..180.0) {
                mostrarSnackbar("Por favor, ingresa coordenadas reales. Latitud entre -90 y 90, y longitud entre -180 y 180.")
                return@setOnClickListener
            }

            if (modo == "crear") {
                val respuesta = BaseDeDatos.tablaSistemaSolarPlaneta?.crearPlaneta(
                    nombre, tipoPlaneta, distancia, diametro, false, latitud, longitud, sistemaSolarId
                )
                if (respuesta == true) {
                    mostrarSnackbar("Planeta creado correctamente.")
                    setResult(RESULT_OK)
                    finish()
                } else {
                    mostrarSnackbar("Error al crear el planeta. Verifica los datos.")
                }
            } else if (modo == "editar" && planeta != null) {
                val respuesta = BaseDeDatos.tablaSistemaSolarPlaneta?.actualizarPlaneta(
                    nombre, distancia, tipoPlaneta, diametro, planeta.habitable, latitud, longitud, planeta.id
                )
                if (respuesta == true) {
                    mostrarSnackbar("Planeta actualizado correctamente.")
                    setResult(RESULT_OK)
                    finish()
                } else {
                    mostrarSnackbar("Error al actualizar el planeta.")
                }
            }
        }
    }
}
