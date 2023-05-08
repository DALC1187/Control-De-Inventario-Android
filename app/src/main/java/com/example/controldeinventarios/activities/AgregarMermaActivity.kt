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
import com.example.controldeinventarios.models.GenericResponse
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
    private lateinit var spinnerDano: Spinner
    val tiposDano = arrayOf<String?>("Fractura", "Abolladura", "Ponchadura")
    private lateinit var spinnerCambioProveedor: Spinner
    val siNo = arrayOf<String?>("Si", "No")
    private lateinit var spinnerArticulosCambio: Spinner
    var aIdCambio: Long = 0L
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
        spinnerArticulosCambio = binding.sArticulosCambio
        val adapterArticulosCambio: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this@AgregarMermaActivity, android.R.layout.simple_spinner_item,
                articulos as List<String?>
            )
        adapterArticulosCambio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
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
                    spinnerArticulosCambio.adapter = adapterArticulosCambio
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
        spinnerArticulosCambio.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position != 0){
                    aIdCambio = a.get(position - 1).id
                }
            }
        }
        binding.bGuardar.setOnClickListener {
            if(spinner.selectedItem.toString() == "Caducidad"){
                if(spinnerArticulos.selectedItem.toString() == "Seleccionar articulo" || binding.eCantidad.text.toString() == ""){
                    Toast.makeText(this@AgregarMermaActivity, "Los campos no son correctos", Toast.LENGTH_SHORT).show()
                }else{
                    api.guardarMermas(
                        "Bearer "+ preferencesHelper.tokenApi!!,
                        aId,
                        binding.eCantidad.text.toString().toInt(),
                        spinner.selectedItem.toString(),
                        null,
                        null,
                        null,
                        null
                    )
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(object : ResourceObserver<GenericResponse>() {
                            override fun onNext(genericResponse: GenericResponse) {
                                if(genericResponse.result == "ok") {
                                    aId = 0L
                                    binding.eCantidad.setText("")
                                    binding.sArticulos.setSelection(0)
                                    binding.sTipoMerma.setSelection(0)
                                    Toast.makeText(this@AgregarMermaActivity, "Merma agregada correctamente", Toast.LENGTH_SHORT).show()
                                }else{
                                    Toast.makeText(this@AgregarMermaActivity, "La cantidad de articulos proporcionada es mayor contra la que se tiene en stock.", Toast.LENGTH_LONG).show()
                                }
                            }
                            override fun onError(e: Throwable) {}
                            override fun onComplete() {}
                        })
                }
            }

            if(spinner.selectedItem.toString() == "Dañado en embajale" || spinner.selectedItem.toString() == "Dañado en sucursal"){
                if(spinnerCambioProveedor.selectedItem.toString() == "Si"){
                    if(spinnerArticulos.selectedItem.toString() == "Seleccionar articulo" || binding.eCantidad.text.toString().toIntOrNull() == null || spinnerDano.selectedItem.toString() == "" || spinnerCambioProveedor.selectedItem.toString() == "" || spinnerArticulosCambio.selectedItem.toString() == "Seleccionar articulo" || binding.eCantidadCambio.text.toString().toIntOrNull() == null){
                        Toast.makeText(this@AgregarMermaActivity, "Los campos no son correctos", Toast.LENGTH_SHORT).show()
                    }else{
                        api.guardarMermas(
                            "Bearer "+ preferencesHelper.tokenApi!!,
                            aId,
                            binding.eCantidad.text.toString().toInt(),
                            spinner.selectedItem.toString(),
                            spinnerDano.selectedItem.toString(),
                            spinnerCambioProveedor.selectedItem.toString(),
                            aIdCambio,
                            binding.eCantidadCambio.text.toString().toInt()
                        )
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.newThread())
                            .subscribe(object : ResourceObserver<GenericResponse>() {
                                override fun onNext(genericResponse: GenericResponse) {
                                    if(genericResponse.result == "ok") {
                                        aId = 0L
                                        binding.eCantidad.setText("")
                                        spinner.setSelection(0)
                                        binding.sTipoDano.setSelection(0)
                                        binding.sCambioProveedor.setSelection(0)
                                        aIdCambio = 0L
                                        binding.eCantidadCambio.setText("")
                                        Toast.makeText(this@AgregarMermaActivity, "Merma agregada correctamente", Toast.LENGTH_SHORT).show()
                                    }else{
                                        Toast.makeText(this@AgregarMermaActivity, "La cantidad de articulos proporcionada es mayor contra la que se tiene en stock.", Toast.LENGTH_LONG).show()
                                    }
                                }
                                override fun onError(e: Throwable) {}
                                override fun onComplete() {}
                            })
                    }
                }else{
                    if(spinnerArticulos.selectedItem.toString() == "Seleccionar articulo" || binding.eCantidad.text.toString() == "" || spinnerDano.selectedItem.toString() == "" || spinnerCambioProveedor.selectedItem.toString() == ""){
                        Toast.makeText(this@AgregarMermaActivity, "Los campos no son correctos", Toast.LENGTH_SHORT).show()
                    }else{
                        api.guardarMermas(
                            "Bearer "+ preferencesHelper.tokenApi!!,
                            aId,
                            binding.eCantidad.text.toString().toInt(),
                            spinner.selectedItem.toString(),
                            spinnerDano.selectedItem.toString(),
                            spinnerCambioProveedor.selectedItem.toString(),
                            null,
                            null
                        )
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.newThread())
                            .subscribe(object : ResourceObserver<GenericResponse>() {
                                override fun onNext(genericResponse: GenericResponse) {
                                    if(genericResponse.result == "ok") {
                                        aId = 0L
                                        binding.eCantidad.setText("")
                                        spinner.setSelection(0)
                                        binding.sTipoDano.setSelection(0)
                                        binding.sCambioProveedor.setSelection(0)
                                        Toast.makeText(this@AgregarMermaActivity, "Merma agregada correctamente", Toast.LENGTH_SHORT).show()
                                    }else{
                                        Toast.makeText(this@AgregarMermaActivity, "La cantidad de articulos proporcionada es mayor contra la que se tiene en stock.", Toast.LENGTH_LONG).show()
                                    }
                                }
                                override fun onError(e: Throwable) {}
                                override fun onComplete() {}
                            })
                    }
                }
            }

        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(tiposMerma.get(position) == "Caducidad"){
                    binding.tTipoDano.visibility = View.GONE
                    binding.sTipoDano.visibility = View.GONE
                    binding.tCambioProveedor.visibility = View.GONE
                    binding.sCambioProveedor.visibility = View.GONE
                    binding.tArticulosCambio.visibility = View.GONE
                    binding.sArticulosCambio.visibility = View.GONE
                    binding.tCantidadCambio.visibility = View.GONE
                    binding.eCantidadCambio.visibility = View.GONE
                    binding.sTipoDano.setSelection(0)
                    binding.sCambioProveedor.setSelection(0)
                    binding.sArticulosCambio.setSelection(0)
                    binding.eCantidadCambio.setText("")
                }else if(tiposMerma.get(position) == "Dañado en embajale"){
                    binding.tTipoDano.visibility = View.VISIBLE
                    binding.sTipoDano.visibility = View.VISIBLE
                    binding.tCambioProveedor.visibility = View.VISIBLE
                    binding.sCambioProveedor.visibility = View.VISIBLE
                    binding.tArticulosCambio.visibility = View.VISIBLE
                    binding.sArticulosCambio.visibility = View.VISIBLE
                    binding.tCantidadCambio.visibility = View.VISIBLE
                    binding.eCantidadCambio.visibility = View.VISIBLE
                }else if(tiposMerma.get(position) == "Dañado en sucursal"){
                    binding.tTipoDano.visibility = View.VISIBLE
                    binding.sTipoDano.visibility = View.VISIBLE
                    binding.tCambioProveedor.visibility = View.VISIBLE
                    binding.sCambioProveedor.visibility = View.VISIBLE
                    binding.tArticulosCambio.visibility = View.VISIBLE
                    binding.sArticulosCambio.visibility = View.VISIBLE
                    binding.tCantidadCambio.visibility = View.VISIBLE
                    binding.eCantidadCambio.visibility = View.VISIBLE
                }
            }
        }
        spinnerDano = binding.sTipoDano
        val adapterDano: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this@AgregarMermaActivity, android.R.layout.simple_spinner_item, tiposDano)
        adapterDano.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDano.adapter = adapterDano
        spinnerCambioProveedor = binding.sCambioProveedor
        val adapterCambioProveedor: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this@AgregarMermaActivity, android.R.layout.simple_spinner_item, siNo)
        adapterCambioProveedor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCambioProveedor.adapter = adapterCambioProveedor
        spinnerCambioProveedor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(siNo.get(position) == "Si"){
                    binding.tArticulosCambio.visibility = View.VISIBLE
                    binding.sArticulosCambio.visibility = View.VISIBLE
                    binding.tCantidadCambio.visibility = View.VISIBLE
                    binding.eCantidadCambio.visibility = View.VISIBLE
                }else if(siNo.get(position) == "No"){
                    binding.tArticulosCambio.visibility = View.GONE
                    binding.sArticulosCambio.visibility = View.GONE
                    binding.tCantidadCambio.visibility = View.GONE
                    binding.eCantidadCambio.visibility = View.GONE
                }
            }
        }
    }
}