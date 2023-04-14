package com.example.controldeinventarios.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

        binding.eUsuario.setText("administrador@gmail.com")
        binding.eContrasena.setText("secret")

        binding.bIniciar.setOnClickListener {
            api.login(binding.eUsuario.text.toString(), binding.eContrasena.text.toString())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .subscribe(object : ResourceObserver<Login>() {
                override fun onNext(t: Login) {
                    preferencesHelper.tokenApi = t.token
                    val intent = Intent(this@MainActivity, UsuarioActivity::class.java)
                    startActivity(intent)
                } override fun onError(e: Throwable) {}override fun onComplete() {}
            })












        }
    }
}