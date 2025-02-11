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
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class PlanetaListView : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val listaPlanetas = mutableListOf<Planeta>()
    private var sistemaSolarId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planeta_list_view)

        listView = findViewById(R.id.lv_lista_planetas)
        val txtPlanetas = findViewById<TextView>(R.id.txt_view_planeta)
        val btnAgregarPlaneta = findViewById<Button>(R.id.btn_agregar_planeta)

        val sistemaSolar = intent.getParcelableExtra<SistemaSolar>("sistemaSolar")
        sistemaSolarId = sistemaSolar?.id ?: -1

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaPlanetas.map { it.nombre })
        listView.adapter = adapter

        registerForContextMenu(listView)
        cargarDatosDesdeBaseDeDatos()

        btnAgregarPlaneta.setOnClickListener {
            irActividad(CrudPlaneta::class.java)
        }
    }

    private var posicionItemSeleccionado = -1

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_contextual_planetas, menu)
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        posicionItemSeleccionado = info.position
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_editar_planeta -> {
                val planetaSeleccionado = listaPlanetas[posicionItemSeleccionado]
                irActividad(CrudPlaneta::class.java, planetaSeleccionado)
                true
            }
            R.id.mi_eliminar_planeta -> {
                abrirDialogoEliminar()
                true
            }
            R.id.mi_abrir_mapa -> {
                val planetaSeleccionado = listaPlanetas[posicionItemSeleccionado]
                abrirMapa(planetaSeleccionado.latitud, planetaSeleccionado.longitud)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun abrirDialogoEliminar() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("¿Desea eliminar el planeta?")
        builder.setPositiveButton("Aceptar") { _, _ ->
            val planetaSeleccionado = listaPlanetas[posicionItemSeleccionado]
            val id = planetaSeleccionado.id
            val eliminado = BaseDeDatos.tablaSistemaSolarPlaneta?.eliminarPlaneta(id)
            if (eliminado == true) {
                cargarDatosDesdeBaseDeDatos()
            }
        }
        builder.setNegativeButton("Cancelar", null)
        builder.create().show()
    }

    private fun cargarDatosDesdeBaseDeDatos() {
        val planetas = BaseDeDatos.tablaSistemaSolarPlaneta?.obtenerPlanetasPorSistemaSolar(sistemaSolarId)
        listaPlanetas.clear()
        if (planetas != null) {
            listaPlanetas.addAll(planetas)
        }
        adapter.clear()
        adapter.addAll(listaPlanetas.map { it.nombre })
        adapter.notifyDataSetChanged()
    }

    private fun irActividad(clase: Class<*>, planeta: Planeta? = null) {
        val intent = Intent(this, clase)
        intent.putExtra("sistemaSolarId", sistemaSolarId)
        if (planeta != null) {
            intent.putExtra("modo", "editar")
            intent.putExtra("planeta", planeta)
        } else {
            intent.putExtra("modo", "crear")
        }
        startActivityForResult(intent, 1)
    }

    private fun abrirMapa(latitud: Double, longitud: Double) {
        val intent = Intent(this, MapaActivity::class.java)
        intent.putExtra("latitud", latitud)
        intent.putExtra("longitud", longitud)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            cargarDatosDesdeBaseDeDatos()
        }
    }
}
