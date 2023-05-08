package com.example.controldeinventarios.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.controldeinventarios.api
import com.example.controldeinventarios.databinding.ActivityAgregarArticulosEntrantesBinding
import com.example.controldeinventarios.models.Articulos
import com.example.controldeinventarios.preferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.ResourceObserver
import io.reactivex.schedulers.Schedulers

class AgregarArticulosEntrantesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAgregarArticulosEntrantesBinding
    private lateinit var spinnerArticulos: Spinner
    var articulos = mutableListOf("Seleccionar articulo")
    private lateinit var a: List<Articulos>
    var aId: Long = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarArticulosEntrantesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar!!.setTitle("Articulos entrantes")
        spinnerArticulos = binding.sArticulos
        val adapterArticulos: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this@AgregarArticulosEntrantesActivity, android.R.layout.simple_spinner_item,
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
        binding.bGuardar.setOnClickListener {
            if(spinnerArticulos.selectedItem.toString() == "Seleccionar articulo" || binding.eCantidad.text.toString().toIntOrNull() == null){
                Toast.makeText(this@AgregarArticulosEntrantesActivity, "Los campos no son correctos", Toast.LENGTH_SHORT).show()
            }else{
                api.guardarArticulosEntrantes(
                    "Bearer "+ preferencesHelper.tokenApi!!,
                    aId,
                    binding.eCantidad.text.toString().toInt(),
                )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(object : ResourceObserver<Any>() {
                        override fun onNext(genericResponse: Any) {
                            aId = 0L
                            binding.sArticulos.setSelection(0)
                            binding.eCantidad.setText("")
                            Toast.makeText(this@AgregarArticulosEntrantesActivity, "Articulo añadido correctamente", Toast.LENGTH_SHORT).show()
                        }
                        override fun onError(e: Throwable) {}
                        override fun onComplete() {}
                    })
            }
        }
        spinnerArticulos.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position != 0){
                    aId = a.get(position - 1).id
                }
            }
        }
    }
}