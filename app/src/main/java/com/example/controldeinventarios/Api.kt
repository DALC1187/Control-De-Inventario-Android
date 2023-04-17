package com.example.controldeinventarios

import com.example.controldeinventarios.models.Articulos
import com.example.controldeinventarios.models.Login
import com.example.controldeinventarios.models.Usuarios
import io.reactivex.Observable
import retrofit2.http.*

interface Api {

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Observable<Login>

    @GET("usuarios")
    fun obtenerUsuarios(
        @Header("Authorization") token: String,
    ): Observable<List<Usuarios>>

    @FormUrlEncoded
    @POST("usuarios")
    fun guardarUsuario(
        @Header("Authorization") token: String,
        @Field("nombre") nombre: String,
        @Field("apellido_paterno") apellidopaterno: String,
        @Field("apellido_materno") apellidoMaterno: String,
        @Field("tipo_usuario") tipoUsuario: String,
        @Field("email") email: String,
        @Field("password") password: String?,
    ): Observable<Any>

    @FormUrlEncoded
    @PUT("usuarios/{usuarioid}")
    fun actualizarUsuario(
        @Header("Authorization") token: String,
        @Path("usuarioid") usuarioid: String,
        @Field("nombre") nombre: String,
        @Field("apellido_paterno") apellidopaterno: String,
        @Field("apellido_materno") apellidoMaterno: String,
        @Field("tipo_usuario") tipoUsuario: String,
        @Field("email") email: String,
        @Field("password") password: String?,
    ): Observable<Any>

    @GET("usuarios/{usuarioid}")
    fun detalleUsuario(
        @Header("Authorization") token: String,
        @Path("usuarioid") usuarioid: String
    ): Observable<Usuarios>

    @DELETE("usuarios/{usuarioid}")
    fun eliminarUsuarios(
        @Header("Authorization") token: String,
        @Path("usuarioid") usuarioid: String
    ): Observable<Any>

    @GET("articulos")
    fun obtenerArticulos(
        @Header("Authorization") token: String,
    ): Observable<List<Articulos>>

    @FormUrlEncoded
    @POST("articulos")
    fun guardarArticulos(
        @Header("Authorization") token: String,
        @Field("nombre") nombre: String,
        @Field("costoPieza") costoPieza: Double,
        @Field("numPiezaPaquete") numPiezaPaquete: Int,
        @Field("stockInicial") stockInicial: Int,
    ): Observable<Any>

    @FormUrlEncoded
    @PUT("articulos/{articulosid}")
    fun actualizarArticulos(
        @Header("Authorization") token: String,
        @Path("articulosid") usuarioid: String,
        @Field("nombre") nombre: String,
        @Field("costoPieza") costoPieza: Double,
        @Field("numPiezaPaquete") numPiezaPaquete: Int,
        @Field("stockInicial") stockInicial: Int,
    ): Observable<Any>

    @GET("articulos/{articulosid}")
    fun detalleArticulos(
        @Header("Authorization") token: String,
        @Path("articulosid") usuarioid: String
    ): Observable<Articulos>

    @DELETE("articulos/{articulosid}")
    fun eliminarArticulos(
        @Header("Authorization") token: String,
        @Path("articulosid") usuarioid: String
    ): Observable<Any>
}