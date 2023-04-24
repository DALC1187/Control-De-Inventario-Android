package com.example.controldeinventarios.models

data class Articulos (
    var id: Long,
    var nombre: String,
    var costoPieza: Double,
    var numPiezaPaquete: Int,
    var stockInicial: Int,
    var clasificacion: String
)