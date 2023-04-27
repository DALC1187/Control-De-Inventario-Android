package com.example.controldeinventarios.models

data class Promociones (
    var id: Long,
    var nombre: String,
    var descripcion: String,
    var vigenciaInicial: String,
    var vigenciaFinal: String,
)