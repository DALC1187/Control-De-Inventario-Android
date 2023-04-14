package com.example.controldeinventarios.models

data class Usuarios (
    var id: Long,
    var email: String,
    var nombre: String,
    var apellido_paterno: String,
    var apellido_materno: String,
    var tipo_usuario: String
)