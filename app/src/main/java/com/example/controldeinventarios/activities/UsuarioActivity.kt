package com.example.controldeinventarios.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.controldeinventarios.R
import com.example.controldeinventarios.adapters.UsuariosAdapter
import com.example.controldeinventarios.api
import com.example.controldeinventarios.databinding.ActivityUsuarioBinding
import com.example.controldeinventarios.models.Usuarios
import com.example.controldeinventarios.preferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.ResourceObserver
import io.reactivex.schedulers.Schedulers

class UsuarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUsuarioBinding
    private var adapter: UsuariosAdapter? = null
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar!!.setTitle("Usuarios")
        recyclerView = binding.recycler
        binding.bActualizar.setOnClickListener {
        obtenerUsuarios()
        }
        binding.bAgregar.setOnClickListener {
            val intent=Intent(this, AgregarUsuarioActivity::class.java)
            startActivity(intent)
        }
        obtenerUsuarios()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(preferencesHelper.tipoUsuario == "ADMINISTRADOR"){
            menuInflater.inflate(R.menu.administrador, menu)
        }else{
            menuInflater.inflate(R.menu.empleado, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.articulos -> {
            val activity= Intent(this, ArticulosActivity::class.java)
            startActivity(activity)
            }
            R.id.promociones -> {
                val activity= Intent(this, PromopcionesActivity::class.java)
                startActivity(activity)
            }
            R.id.siniestros -> {
                val activity= Intent(this, AgregarSiniestrosActivity::class.java)
                startActivity(activity)
            }
            R.id.merma -> {
                val activity= Intent(this, AgregarMermaActivity::class.java)
                startActivity(activity)
            }
            R.id.articulosEntrantes -> {
                val activity= Intent(this, AgregarArticulosEntrantesActivity::class.java)
                startActivity(activity)
            }
            R.id.inventario -> {
                val activity= Intent(this, AgregarInventarioActivity::class.java)
                startActivity(activity)
            }
            R.id.inventario8020 -> {
                val activity= Intent(this, AgregarInventario8020Activity::class.java)
                startActivity(activity)
            }
            R.id.cerrarSesion -> {
                val activity= Intent(this, MainActivity::class.java)
                activity.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(activity)
                finish()
                preferencesHelper.tokenApi = null
            }
        }
        return super.onOptionsItemSelected(item)
    }
    fun obtenerUsuarios(){
        recyclerView.visibility = View.GONE
        api.obtenerUsuarios("Bearer "+preferencesHelper.tokenApi!!)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .subscribe(object : ResourceObserver<List<Usuarios>>() {
                override fun onNext(usuariosResponse: List<Usuarios>) {
                if(usuariosResponse.isNotEmpty()){
                if(adapter == null){
                    recyclerView.visibility = View.VISIBLE
                    adapter = UsuariosAdapter(this@UsuarioActivity, usuariosResponse)
                    val linearLayoutManager = LinearLayoutManager(this@UsuarioActivity)
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = linearLayoutManager
                    adapter!!.notifyDataSetChanged()
                }else{
                    recyclerView.visibility = View.VISIBLE
                    adapter!!.usuarios = usuariosResponse
                    adapter!!.notifyDataSetChanged()
                }
                } }override fun onError(e: Throwable) {}override fun onComplete() {}
            })
    }


    override fun onRestart() {
        super.onRestart()
        obtenerUsuarios()
    }
}