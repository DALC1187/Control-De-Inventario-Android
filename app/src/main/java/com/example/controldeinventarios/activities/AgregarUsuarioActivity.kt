package com.example.controldeinventarios.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.controldeinventarios.api
import com.example.controldeinventarios.databinding.ActivityAgregarUsuarioBinding
import com.example.controldeinventarios.preferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.ResourceObserver
import io.reactivex.schedulers.Schedulers


class AgregarUsuarioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAgregarUsuarioBinding
    private lateinit var spinner: Spinner
    val tiposUsuarios = arrayOf<String?>("ADMINISTRADOR", "EMPLEADO")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar!!.setTitle("Agregar Usuario")
        spinner = binding.sTipoDeUsuario
        val adapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this@AgregarUsuarioActivity, android.R.layout.simple_spinner_item, tiposUsuarios)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        binding.bGuardar.setOnClickListener {

            if(binding.etNombre.text.toString() == "" || binding.etApellidoPaterno.text.toString() == "" || binding.etApellidoMaterno.text.toString() == "" || binding.etPassword.text.toString() == "" || !(android.util.Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString()).matches())){
                Toast.makeText(this@AgregarUsuarioActivity, "Los campos no son correctos", Toast.LENGTH_SHORT).show()
            }else{
                api.guardarUsuario(
                    "Bearer "+ preferencesHelper.tokenApi!!,
                    binding.etNombre.text.toString(),
                    binding.etApellidoPaterno.text.toString(),
                    binding.etApellidoMaterno.text.toString(),
                    spinner.selectedItem.toString(),
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString()
                )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(object : ResourceObserver<Any>() {
                        override fun onNext(genericResponse: Any) {
                            binding.etNombre.setText("")
                            binding.etApellidoPaterno.setText("")
                            binding.etApellidoMaterno.setText("")
                            binding.etEmail.setText("")
                            binding.etPassword.setText("")
                            binding.sTipoDeUsuario.setSelection(0)
                            Toast.makeText(this@AgregarUsuarioActivity, "Usuario agregado correctamente", Toast.LENGTH_SHORT).show()
                        }
                        override fun onError(e: Throwable) {}
                        override fun onComplete() {}
                    })
            }
        }
    }
}