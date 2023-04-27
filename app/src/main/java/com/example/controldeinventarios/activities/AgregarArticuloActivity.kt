package com.example.controldeinventarios.activities

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.controldeinventarios.api
import com.example.controldeinventarios.databinding.ActivityAgregarArticuloBinding
import com.example.controldeinventarios.preferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.ResourceObserver
import io.reactivex.schedulers.Schedulers

class AgregarArticuloActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAgregarArticuloBinding
    private lateinit var spinner: Spinner
    val clasificacion = arrayOf<String?>("Normal", "Mas vendido", "Menos vendido")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarArticuloBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar!!.setTitle("Agregar Articulos")
        spinner = binding.sClasificacion
        val adapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this@AgregarArticuloActivity, R.layout.simple_spinner_item, clasificacion)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        binding.bGuardar.setOnClickListener {

            if(binding.etNombre.text.toString() == "" || binding.etCostoPieza.text.toString().toDoubleOrNull() == null || binding.etPiezasPorPaquete.text.toString().toIntOrNull() == null || binding.etStockInicial.text.toString().toIntOrNull() == null){
                Toast.makeText(this@AgregarArticuloActivity, "Los campos no son correctos", Toast.LENGTH_SHORT).show()
            }else{
                api.guardarArticulos(
                    "Bearer "+ preferencesHelper.tokenApi!!,
                    binding.etNombre.text.toString(),
                    binding.etCostoPieza.text.toString().toDouble(),
                    binding.etPiezasPorPaquete.text.toString().toInt(),
                    binding.etStockInicial.text.toString().toInt(),
                    spinner.selectedItem.toString(),
                )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(object : ResourceObserver<Any>() {
                        override fun onNext(genericResponse: Any) {
                            Toast.makeText(this@AgregarArticuloActivity, "Articulo agregado correctamente", Toast.LENGTH_SHORT).show()
                        }
                        override fun onError(e: Throwable) {}
                        override fun onComplete() {}
                    })
            }

        }
    }
}