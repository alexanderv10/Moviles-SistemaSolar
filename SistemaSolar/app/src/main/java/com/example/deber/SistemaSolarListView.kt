package com.example.deber

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class SistemaSolarListView : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val listaSistemasSolares = mutableListOf<SistemaSolar>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sistema_solar_list_view)

        listView = findViewById(R.id.lv_lista_sistemas_solares)
        val btnCrearSistemaSolar = findViewById<Button>(R.id.btn_agregar_sistema_solar)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaSistemasSolares.map { it.nombre })
        listView.adapter = adapter

        registerForContextMenu(listView)
        cargarDatosDesdeBaseDeDatos()

        btnCrearSistemaSolar.setOnClickListener {
            irActividad(CrudSistemaSolar::class.java)
        }
    }

    private var posicionItemSeleccionado = -1

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_contextual, menu)
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        posicionItemSeleccionado = info.position
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_editar -> {
                val sistemaSolarSeleccionado = listaSistemasSolares[posicionItemSeleccionado]
                irActividad(CrudSistemaSolar::class.java, sistemaSolarSeleccionado)
                true
            }
            R.id.mi_eliminar -> {
                abrirDialogoEliminar()
                true
            }
            R.id.mi_planeta -> {
                val sistemaSolarSeleccionado = listaSistemasSolares[posicionItemSeleccionado]
                irActividad(PlanetaListView::class.java, sistemaSolarSeleccionado)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun abrirDialogoEliminar() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("¿Desea eliminar el Sistema Solar?")
        builder.setPositiveButton("Aceptar") { _, _ ->
            val sistemaSolarSeleccionado = listaSistemasSolares[posicionItemSeleccionado]
            val id = sistemaSolarSeleccionado.id
            val eliminado = BaseDeDatos.tablaSistemaSolarPlaneta?.eliminarSistemaSolar(id)
            if (eliminado == true) {
                cargarDatosDesdeBaseDeDatos()
            }
        }
        builder.setNegativeButton("Cancelar", null)
        builder.create().show()
    }

    private fun cargarDatosDesdeBaseDeDatos() {
        val sistemasSolares = BaseDeDatos.tablaSistemaSolarPlaneta?.obtenerTodosLosSistemasSolares()
        listaSistemasSolares.clear()
        if (sistemasSolares != null) {
            listaSistemasSolares.addAll(sistemasSolares)
        }
        adapter.clear()
        adapter.addAll(listaSistemasSolares.map { it.nombre })
        adapter.notifyDataSetChanged()
    }

    private fun irActividad(clase: Class<*>, sistemaSolar: SistemaSolar? = null) {
        val intent = Intent(this, clase)
        if (sistemaSolar != null) {
            intent.putExtra("modo", "editar")
            intent.putExtra("sistemaSolar", sistemaSolar)
        } else {
            intent.putExtra("modo", "crear")
        }
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            cargarDatosDesdeBaseDeDatos()
        }
    }
}
