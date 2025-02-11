package com.example.deber

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapaActivity : AppCompatActivity(), OnMapReadyCallback {

    private var latitud: Double = 0.0
    private var longitud: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)

        // Obtener las coordenadas de la intención
        latitud = intent.getDoubleExtra("latitud", 0.0)
        longitud = intent.getDoubleExtra("longitud", 0.0)

        // Inicializar el fragmento de mapa
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        // Crear el objeto LatLng con las coordenadas recibidas
        val ubicacion = LatLng(latitud, longitud)

        // Añadir un marcador en la ubicación
        googleMap.addMarker(
            MarkerOptions().position(ubicacion).title("Ubicación del planeta")
        )

        // Mover la cámara hacia la ubicación y hacer zoom
        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(ubicacion, 15f) // Zoom más alto para acercar
        )
    }
}