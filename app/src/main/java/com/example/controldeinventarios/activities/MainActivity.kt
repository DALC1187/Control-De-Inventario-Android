package com.example.controldeinventarios.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.controldeinventarios.api
import com.example.controldeinventarios.databinding.ActivityMainBinding
import com.example.controldeinventarios.models.Login
import com.example.controldeinventarios.preferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.ResourceObserver
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //binding.eUsuario.setText("administrador@gmail.com")
        //binding.eContrasena.setText("123456")


            binding.bIniciar.setOnClickListener {
                if(!(android.util.Patterns.EMAIL_ADDRESS.matcher(binding.eUsuario.text.toString()).matches()) || binding.eContrasena.text.toString() == ""){
                    Toast.makeText(this@MainActivity, "Los campos no son correctos", Toast.LENGTH_SHORT).show()
                }else{
                    api.login(binding.eUsuario.text.toString(), binding.eContrasena.text.toString())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(object : ResourceObserver<Login>() {
                            override fun onNext(t: Login) {
                                preferencesHelper.tokenApi = t.token
                                preferencesHelper.tipoUsuario = t.user.tipo_usuario
                                val intent = Intent(this@MainActivity, ArticulosActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                startActivity(intent)
                                finish()
                            } override fun onError(e: Throwable) {
                                Toast.makeText(this@MainActivity, "Inicio de sesión invalido", Toast.LENGTH_SHORT).show()
                            }override fun onComplete() {}
                        })
                }















        }
    }
}