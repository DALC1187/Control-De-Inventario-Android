package com.example.controldeinventarios.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.controldeinventarios.R
import com.example.controldeinventarios.api
import com.example.controldeinventarios.databinding.ActivityAgregarInventarioBinding
import com.example.controldeinventarios.models.Articulo
import com.example.controldeinventarios.models.Articulos
import com.example.controldeinventarios.models.GenericResponse
import com.example.controldeinventarios.preferencesHelper
import com.fasterxml.jackson.module.kotlin.jsonMapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.ResourceObserver
import io.reactivex.schedulers.Schedulers

class AgregarInventarioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAgregarInventarioBinding
    private lateinit var spinnerArticulos: Spinner
    var articulos = mutableListOf("Seleccionar articulo")
    private lateinit var a: List<Articulos>
    var aId: Long = 0L
    var articulosInventario = mutableListOf<Articulo>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarInventarioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar!!.setTitle("Inventario")
        binding.bCancelar.setOnClickListener { finish() }
        binding.bAgregar.setOnClickListener {
            if(articulosInventario.size != a.size){
                Toast.makeText(this@AgregarInventarioActivity, "Necesitas agregar todos los artículos", Toast.LENGTH_SHORT).show()
            }else{
                api.agregarInventario(
                    "Bearer "+ preferencesHelper.tokenApi!!,
                    jsonMapper().writeValueAsString(articulosInventario),
                    1
                )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(object : ResourceObserver<GenericResponse>() {
                        override fun onNext(genericResponse: GenericResponse) {
                            aId = 0L
                            articulosInventario = mutableListOf<Articulo>()
                            binding.eCantidad.setText("")
                            binding.tArticulos.setText("")
                            binding.sArticulos.setSelection(0)
                            if(genericResponse.result == "ok") {
                                Toast.makeText(this@AgregarInventarioActivity, "Inventario agregado correctamente", Toast.LENGTH_SHORT).show()
                                finish()
                            }else{
                                Toast.makeText(this@AgregarInventarioActivity, "La cantidad de articulos proporcionada es mayor contra la que se tiene en stock.", Toast.LENGTH_LONG).show()
                            }
                        }
                        override fun onError(e: Throwable) {}
                        override fun onComplete() {}
                    })
            }
        }
        spinnerArticulos = binding.sArticulos
        val adapterArticulos: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this@AgregarInventarioActivity, android.R.layout.simple_spinner_item,
                articulos as List<String?>
            )
        adapterArticulos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        api.obtenerArticulos("Bearer "+ preferencesHelper.tokenApi!!)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .subscribe(object : ResourceObserver<List<Articulos>>() {
                override fun onNext(articulosResponse: List<Articulos>) {
                    articulosResponse.forEach {
                        articulos.add(it.nombre)
                    }
                    a = articulosResponse

                    spinnerArticulos.adapter = adapterArticulos
                }
                override fun onError(e: Throwable) {}
                override fun onComplete() {}
            })
        spinnerArticulos.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position != 0){
                    aId = a.get(position - 1).id
                }
            }
        }
        binding.bAgregarArticulo.setOnClickListener {
            if(spinnerArticulos.selectedItem.toString() == "Seleccionar articulo" || binding.eCantidad.text.toString() == ""){
                Toast.makeText(this@AgregarInventarioActivity, "Los campos no son correctos", Toast.LENGTH_SHORT).show()
            }else{
                if(articulosInventario.filter { it.id == aId}.size == 0){
                    articulosInventario.add(Articulo(aId,spinnerArticulos.selectedItem.toString(), binding.eCantidad.text.toString().toInt()))
                    binding.tArticulos.append("${spinnerArticulos.selectedItem.toString()}: ${binding.eCantidad.text.toString()}\n")
                    binding.sArticulos.setSelection(0)
                    binding.eCantidad.setText("")
                }else{
                    Toast.makeText(this@AgregarInventarioActivity, "El articulo ya fue agregado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}