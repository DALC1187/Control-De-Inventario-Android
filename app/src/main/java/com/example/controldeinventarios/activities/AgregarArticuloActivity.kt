package com.example.controldeinventarios.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.controldeinventarios.api
import com.example.controldeinventarios.databinding.ActivityAgregarArticuloBinding
import com.example.controldeinventarios.preferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.ResourceObserver
import io.reactivex.schedulers.Schedulers

class AgregarArticuloActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAgregarArticuloBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarArticuloBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar!!.setTitle("Agregar Articulos")
        binding.bGuardar.setOnClickListener {
            api.guardarArticulos(
            "Bearer "+ preferencesHelper.tokenApi!!,
            binding.etNombre.text.toString(),
            binding.etCostoPieza.text.toString().toDouble(),
            binding.etPiezasPorPaquete.text.toString().toInt(),
            binding.etStockInicial.text.toString().toInt(),
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