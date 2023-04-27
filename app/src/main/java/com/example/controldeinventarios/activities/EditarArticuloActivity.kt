package com.example.controldeinventarios.activities

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.controldeinventarios.api
import com.example.controldeinventarios.databinding.ActivityEditarArticuloBinding
import com.example.controldeinventarios.models.Articulos
import com.example.controldeinventarios.preferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.ResourceObserver
import io.reactivex.schedulers.Schedulers

class EditarArticuloActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditarArticuloBinding
var articuloid = 0L
    private lateinit var spinner: Spinner
    val clasificacion = arrayOf<String?>("Normal", "Mas vendido", "Menos vendido")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarArticuloBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar!!.setTitle("Editar Articulo")
        articuloid = intent.getLongExtra("articuloID", 0L)
        spinner = binding.sClasificacion
        val adapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this@EditarArticuloActivity, R.layout.simple_spinner_item, clasificacion)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        api.detalleArticulos("Bearer "+ preferencesHelper.tokenApi!!,articuloid.toString())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .subscribe(object : ResourceObserver<Articulos>() {
                override fun onNext(articulosResponse: Articulos) {
                    binding.etNombre.setText(articulosResponse.nombre)
                    binding.etCostoPieza.setText(articulosResponse.costoPieza.toString())
                    binding.etPiezasPorPaquete.setText(articulosResponse.numPiezaPaquete.toString())
                    binding.etStockInicial.setText(articulosResponse.stockInicial.toString())
                    spinner.setSelection(clasificacion.indexOfFirst { it == articulosResponse.clasificacion })
                }
                override fun onError(e: Throwable) {}
                override fun onComplete() {}
            })

        binding.bActualizar.setOnClickListener {
            if(binding.etNombre.text.toString() == "" || binding.etCostoPieza.text.toString().toDoubleOrNull() == null || binding.etPiezasPorPaquete.text.toString().toIntOrNull() == null || binding.etStockInicial.text.toString().toIntOrNull() == null){
                Toast.makeText(this@EditarArticuloActivity, "Los campos no son correctos", Toast.LENGTH_SHORT).show()
            }else{
                api.actualizarArticulos(
                    "Bearer "+preferencesHelper.tokenApi!!,
                    articuloid.toString(),
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
                            Toast.makeText(this@EditarArticuloActivity, "Articulo actualizado correctamente", Toast.LENGTH_SHORT).show()
                        }
                        override fun onError(e: Throwable) {}
                        override fun onComplete() {}
                    })
            }
        }







    }
}