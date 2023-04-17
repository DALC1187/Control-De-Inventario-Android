package com.example.controldeinventarios.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarArticuloBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar!!.setTitle("Editar Articulo")
        articuloid = intent.getLongExtra("articuloID", 0L)
        api.detalleArticulos("Bearer "+ preferencesHelper.tokenApi!!,articuloid.toString())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .subscribe(object : ResourceObserver<Articulos>() {
                override fun onNext(articulosResponse: Articulos) {
                    binding.etNombre.setText(articulosResponse.nombre)
                    binding.etCostoPieza.setText(articulosResponse.costoPieza.toString())
                    binding.etPiezasPorPaquete.setText(articulosResponse.numPiezaPaquete.toString())
                    binding.etStockInicial.setText(articulosResponse.stockInicial.toString())
                }
                override fun onError(e: Throwable) {}
                override fun onComplete() {}
            })

        binding.bActualizar.setOnClickListener {
            api.actualizarArticulos(
                "Bearer "+preferencesHelper.tokenApi!!,
                articuloid.toString(),
                binding.etNombre.text.toString(),
                binding.etCostoPieza.text.toString().toDouble(),
                binding.etPiezasPorPaquete.text.toString().toInt(),
                binding.etStockInicial.text.toString().toInt()
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(object : ResourceObserver<Any>() {
                    override fun onNext(genericResponse: Any) {}
                    override fun onError(e: Throwable) {}
                    override fun onComplete() {}
                })
        }







    }
}