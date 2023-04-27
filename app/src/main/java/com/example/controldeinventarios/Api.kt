package com.example.controldeinventarios

import com.example.controldeinventarios.models.*
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
    @POST("articulos/buscador")
    fun buscadorArticulos(
        @Header("Authorization") token: String,
        @Field("texto") text: String,
    ): Observable<List<Articulos>>

    @FormUrlEncoded
    @POST("articulos")
    fun guardarArticulos(
        @Header("Authorization") token: String,
        @Field("nombre") nombre: String,
        @Field("costoPieza") costoPieza: Double,
        @Field("numPiezaPaquete") numPiezaPaquete: Int,
        @Field("stockInicial") stockInicial: Int,
        @Field("clasificacion") clasificacion: String,
    ): Observable<Any>

    @FormUrlEncoded
    @PUT("articulos/{articulosid}")
    fun actualizarArticulos(
        @Header("Authorization") token: String,
        @Path("articulosid") usuarioid: String,
        @Field("nombre") nombre: String,
        @Field("costoPieza") costoPieza: Double,
        @Field("numPiezaPaquete") numPiezaPaquete: Int,
        @Field("clasificacion") clasificacion: String,
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

    @GET("promociones")
    fun obtenerPromociones(
        @Header("Authorization") token: String,
    ): Observable<List<Promociones>>

    @FormUrlEncoded
    @POST("promociones")
    fun guardarPromociones(
        @Header("Authorization") token: String,
        @Field("nombre") nombre: String,
        @Field("descripcion") descripcion: String,
        @Field("vigenciaInicial") vigenciaInicial: String,
        @Field("vigenciaFinal") vigenciaFinal: String,
        @Field("idArticulo") idArticulo: Long,
        @Field("costo") costo: Double,
        @Field("cantidad") cantidad: Int,
    ): Observable<Any>

    @FormUrlEncoded
    @PUT("promociones/{promocionesid}")
    fun actualizarPromociones(
        @Header("Authorization") token: String,
        @Path("promocionesid") usuarioid: String,
        @Field("nombre") nombre: String,
        @Field("descripcion") descripcion: String,
        @Field("vigenciaInicial") vigenciaInicial: String,
        @Field("vigenciaFinal") vigenciaFinal: String,
        @Field("idArticulo") idArticulo: Long,
        @Field("costo") costo: Double,
        @Field("cantidad") cantidad: Int
    ): Observable<Any>

    @GET("promociones/{promocionesid}")
    fun detallePromociones(
        @Header("Authorization") token: String,
        @Path("promocionesid") usuarioid: String
    ): Observable<Promociones>

    @DELETE("promociones/{promocionesid}")
    fun eliminarPromociones(
        @Header("Authorization") token: String,
        @Path("promocionesid") usuarioid: String
    ): Observable<Any>


    @FormUrlEncoded
    @POST("siniestros")
    fun guardarSiniestro(
        @Header("Authorization") token: String,
        @Field("fecha") fecha: String,
        @Field("hora") hora: String,
        @Field("descripcion") descripcion: String,
        @Field("tipo") tipo: String,
        @Field("hoja_ministro") hoja_ministro: String,
        @Field("hora_supervisor") hora_supervisor: String,
        @Field("articulos") articulos: String
    ): Observable<Any>


    @FormUrlEncoded
    @POST("mermas")
    fun guardarMermas(
        @Header("Authorization") token: String,
        @Field("idArticulo") idArticulo: Long,
        @Field("cantidad") cantidad: Int,
        @Field("tipoMerma") tipoMerma: String,
        @Field("tipoDano") tipoDano: String?,
        @Field("cambioProveedor") cambioProveedor: String?,
        @Field("idArticuloEntregado") idArticuloEntregado: Long?,
        @Field("cantidadEntregado") cantidadEntregado: Int?,
        ): Observable<Any>
}