package com.example.deber

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class SqliteHelper(context: Context?) : SQLiteOpenHelper(context, "sistema_solar_db", null, 2) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("PRAGMA foreign_keys = ON;")

        val scriptCrearTablaSistemaSolar = """
            CREATE TABLE SistemaSolar(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                descripcion TEXT NOT NULL,
                edad INTEGER NOT NULL,
                tipoEstrella TEXT NOT NULL,
                esHabitable BOOLEAN NOT NULL
            )
        """
        db?.execSQL(scriptCrearTablaSistemaSolar)

        val scriptCrearTablaPlaneta = """
            CREATE TABLE Planeta (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                tipoPlaneta TEXT NOT NULL,
                distanciaAlSol REAL NOT NULL,
                diametro REAL NOT NULL,
                habitable BOOLEAN,
                latitud REAL NOT NULL,
                longitud REAL NOT NULL,
                sistemaSolarId INTEGER,
                FOREIGN KEY(sistemaSolarId) REFERENCES SistemaSolar(id) ON DELETE CASCADE
            )
        """
        db?.execSQL(scriptCrearTablaPlaneta)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db?.execSQL("DROP TABLE IF EXISTS Planeta")
            db?.execSQL("DROP TABLE IF EXISTS SistemaSolar")
            onCreate(db)
        }
    }

    // CRUD para Sistema Solar
    fun crearSistemaSolar(
        nombre: String,
        descripcion: String,
        edad: Int,
        tipoEstrella: String,
        esHabitable: Boolean
    ): Boolean {
        val db = writableDatabase
        val valores = ContentValues().apply {
            put("nombre", nombre)
            put("descripcion", descripcion)
            put("edad", edad)
            put("tipoEstrella", tipoEstrella)
            put("esHabitable", esHabitable)
        }
        val resultado = db.insert("SistemaSolar", null, valores)
        db.close()
        return resultado != -1L
    }

    fun obtenerTodosLosSistemasSolares(): List<SistemaSolar> {
        val lista = mutableListOf<SistemaSolar>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM SistemaSolar", null)
        if (cursor.moveToFirst()) {
            do {
                val sistemaSolar = SistemaSolar(
                    id = cursor.getInt(0),
                    nombre = cursor.getString(1),
                    descripcion = cursor.getString(2),
                    edad = cursor.getInt(3),
                    tipoEstrella = cursor.getString(4),
                    esHabitable = cursor.getInt(5) == 1
                )
                lista.add(sistemaSolar)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }

    fun actualizarSistemaSolar(
        nombre: String,
        descripcion: String,
        edad: Int,
        tipoEstrella: String,
        esHabitable: Boolean,
        id: Int
    ): Boolean {
        val db = writableDatabase
        val valores = ContentValues().apply {
            put("nombre", nombre)
            put("descripcion", descripcion)
            put("edad", edad)
            put("tipoEstrella", tipoEstrella)
            put("esHabitable", esHabitable)
        }
        val resultado = db.update("SistemaSolar", valores, "id=?", arrayOf(id.toString()))
        db.close()
        return resultado > 0
    }

    fun eliminarSistemaSolar(id: Int): Boolean {
        val db = writableDatabase
        val resultado = db.delete("SistemaSolar", "id=?", arrayOf(id.toString()))
        db.close()
        return resultado > 0
    }

    // CRUD para Planeta
    fun crearPlaneta(
        nombre: String,
        tipoPlaneta: String,
        distanciaAlSol: Double,
        diametro: Double,
        habitable: Boolean,
        latitud: Double,
        longitud: Double,
        sistemaSolarId: Int
    ): Boolean {
        val db = writableDatabase
        val valores = ContentValues().apply {
            put("nombre", nombre)
            put("tipoPlaneta", tipoPlaneta)
            put("distanciaAlSol", distanciaAlSol)
            put("diametro", diametro)
            put("habitable", habitable)
            put("latitud", latitud)
            put("longitud", longitud)
            put("sistemaSolarId", sistemaSolarId)
        }

        return try {
            val resultado = db.insertOrThrow("Planeta", null, valores)
            db.close()
            resultado != -1L
        } catch (e: Exception) {
            Log.e("SqliteHelper", "Error al insertar el planeta: ${e.message}")
            db.close()
            false
        }
    }

    fun obtenerPlanetasPorSistemaSolar(sistemaSolarId: Int): List<Planeta> {
        val lista = mutableListOf<Planeta>()
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM Planeta WHERE sistemaSolarId = ?",
            arrayOf(sistemaSolarId.toString())
        )
        if (cursor.moveToFirst()) {
            do {
                val planeta = Planeta(
                    id = cursor.getInt(0),
                    nombre = cursor.getString(1),
                    tipoPlaneta = cursor.getString(2),
                    distanciaAlSol = cursor.getDouble(3),
                    diametro = cursor.getDouble(4),
                    habitable = cursor.getInt(5) == 1,
                    latitud = cursor.getDouble(6),
                    longitud = cursor.getDouble(7),
                    sistemaSolarId = cursor.getInt(8)
                )
                lista.add(planeta)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }

    fun actualizarPlaneta(
        nombre: String,
        distanciaAlSol: Double,
        tipoPlaneta: String,
        diametro: Double,
        habitable: Boolean,
        latitud: Double,
        longitud: Double,
        id: Int
    ): Boolean {
        val db = writableDatabase
        val valores = ContentValues().apply {
            put("nombre", nombre)
            put("distanciaAlSol", distanciaAlSol)
            put("tipoPlaneta", tipoPlaneta)
            put("diametro", diametro)
            put("habitable", habitable)
            put("latitud", latitud)
            put("longitud", longitud)
        }
        val resultado = db.update("Planeta", valores, "id=?", arrayOf(id.toString()))
        db.close()
        return resultado > 0
    }

    fun eliminarPlaneta(id: Int): Boolean {
        val db = writableDatabase
        val resultado = db.delete("Planeta", "id=?", arrayOf(id.toString()))
        db.close()
        return resultado > 0
    }
}
