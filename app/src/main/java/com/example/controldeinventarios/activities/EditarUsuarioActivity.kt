package com.example.controldeinventarios.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.controldeinventarios.api
import com.example.controldeinventarios.databinding.ActivityEditarUsuarioBinding
import com.example.controldeinventarios.models.Usuarios
import com.example.controldeinventarios.preferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.ResourceObserver
import io.reactivex.schedulers.Schedulers

class EditarUsuarioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditarUsuarioBinding
var usuarioid = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar!!.setTitle("Editar Usuario")
        usuarioid = intent.getLongExtra("usuarioid", 0L)
        api.detalleUsuario("Bearer "+ preferencesHelper.tokenApi!!,usuarioid.toString())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .subscribe(object : ResourceObserver<Usuarios>() {
                override fun onNext(usuariosResponse: Usuarios) {
                    binding.etNombre.setText(usuariosResponse.nombre)
                    binding.etEmail.setText(usuariosResponse.email)
                    binding.etApellidoPaterno.setText(usuariosResponse.apellido_paterno)
                    binding.etApellidoMaterno.setText(usuariosResponse.apellido_materno)
                    binding.etTipoDeUsuario.setText(usuariosResponse.tipo_usuario)
                }
                override fun onError(e: Throwable) {}
                override fun onComplete() {}
            })

        binding.bActualizar.setOnClickListener {
            api.actualizarUsuario(
                "Bearer "+preferencesHelper.tokenApi!!,
                usuarioid.toString(),
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