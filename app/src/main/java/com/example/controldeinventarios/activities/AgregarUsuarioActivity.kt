package com.example.controldeinventarios.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.controldeinventarios.api
import com.example.controldeinventarios.databinding.ActivityAgregarUsuarioBinding
import com.example.controldeinventarios.preferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.ResourceObserver
import io.reactivex.schedulers.Schedulers

class AgregarUsuarioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAgregarUsuarioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar!!.setTitle("Agregar Usuario")
        binding.bGuardar.setOnClickListener {
            api.guardarUsuario(
            "Bearer "+ preferencesHelper.tokenApi!!,
            binding.etNombre.text.toString(),
            binding.etApellidoPaterno.text.toString(),
            binding.etApellidoMaterno.text.toString(),
            binding.etTipoDeUsuario.text.toString(),
            binding.etEmail.text.toString(),
            binding.etPassword.text.toString()
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