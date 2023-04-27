package com.example.controldeinventarios.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.controldeinventarios.R
import com.example.controldeinventarios.api
import com.example.controldeinventarios.databinding.ActivityAgregarMermaBinding
import com.example.controldeinventarios.models.Articulos
import com.example.controldeinventarios.preferencesHelper
import com.fasterxml.jackson.module.kotlin.jsonMapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.ResourceObserver
import io.reactivex.schedulers.Schedulers

class AgregarMermaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAgregarMermaBinding
    private lateinit var spinner: Spinner
    val tiposMerma = arrayOf<String?>("Caducidad", "Dañado en embajale", "Dañado en sucursal")
    private lateinit var spinnerArticulos: Spinner
    var articulos = mutableListOf("Seleccionar articulo")
    private lateinit var a: List<Articulos>
    var aId: Long = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarMermaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar!!.setTitle("Merma")
        spinner = binding.sTipoMerma
        val adapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this@AgregarMermaActivity, android.R.layout.simple_spinner_item, tiposMerma)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinnerArticulos = binding.sArticulos
        val adapterArticulos: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this@AgregarMermaActivity, android.R.layout.simple_spinner_item,
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
        binding.bGuardar.setOnClickListener {
            if(spinnerArticulos.selectedItem.toString() == "Seleccionar articulo" || binding.eCantidad.text.toString() == ""){
                Toast.makeText(this@AgregarMermaActivity, "Los campos no son correctos", Toast.LENGTH_SHORT).show()
            }else{
                api.guardarMermas(
                    "Bearer "+ preferencesHelper.tokenApi!!,
                    aId,
                    binding.eCantidad.text.toString().toInt(),
                    spinner.selectedItem.toString(),
                )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(object : ResourceObserver<Any>() {
                        override fun onNext(genericResponse: Any) {
                            Toast.makeText(this@AgregarMermaActivity, "Merma agregada correctamente", Toast.LENGTH_SHORT).show()
                        }
                        override fun onError(e: Throwable) {}
                        override fun onComplete() {}
                    })
            }
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(tiposMerma.get(position) == "Caducidad"){

                }else{

                }
                if(tiposMerma.get(position) == "Dañado en embajale"){
                    binding.tTipoDano.visibility = View.VISIBLE
                    binding.sTipoDano.visibility = View.VISIBLE
                }else{
                    binding.tTipoDanoSucursal.visibility = View.GONE
                    binding.sTipoDanoSucursal.visibility = View.GONE
                }
                if(tiposMerma.get(position) == "Dañado en sucursal"){
                    binding.tTipoDanoSucursal.visibility = View.VISIBLE
                    binding.sTipoDanoSucursal.visibility = View.VISIBLE
                }else{
                    binding.tTipoDano.visibility = View.GONE
                    binding.sTipoDano.visibility = View.GONE
                }
            }
        }
    }
}