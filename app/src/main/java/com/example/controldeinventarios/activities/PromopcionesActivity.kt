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
import com.example.controldeinventarios.adapters.ArticulosAdapter
import com.example.controldeinventarios.adapters.PromocionesAdapter
import com.example.controldeinventarios.api
import com.example.controldeinventarios.databinding.ActivityArticulosBinding
import com.example.controldeinventarios.databinding.ActivityPromopcionesBinding
import com.example.controldeinventarios.models.Articulos
import com.example.controldeinventarios.models.Promociones
import com.example.controldeinventarios.preferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.ResourceObserver
import io.reactivex.schedulers.Schedulers

class PromopcionesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPromopcionesBinding
    private var adapter: PromocionesAdapter? = null
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPromopcionesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar!!.setTitle("Promociones vigentes")
        recyclerView = binding.recycler
        binding.bActualizar.setOnClickListener {
            obtenerPromociones()
        }
        binding.bAgregar.setOnClickListener {
            val intent=Intent(this, AgregarPromocionActivity::class.java)
            startActivity(intent)
        }

        obtenerPromociones()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.administrador, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.usuarios -> {
                val activity= Intent(this, UsuarioActivity::class.java)
                startActivity(activity)
            }
            R.id.articulos -> {
                val activity= Intent(this, ArticulosActivity::class.java)
                startActivity(activity)
            }
            R.id.siniestros -> {
                val activity= Intent(this, AgregarSiniestrosActivity::class.java)
                startActivity(activity)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun obtenerPromociones(){
        recyclerView.visibility = View.GONE
        api.obtenerPromociones("Bearer "+ preferencesHelper.tokenApi!!)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .subscribe(object : ResourceObserver<List<Promociones>>() {
                override fun onNext(promocionesResponse: List<Promociones>) {
                    if(promocionesResponse.isNotEmpty()){
                        if(adapter == null){
                            recyclerView.visibility = View.VISIBLE
                            adapter = PromocionesAdapter(this@PromopcionesActivity, promocionesResponse)
                            val linearLayoutManager = LinearLayoutManager(this@PromopcionesActivity)
                            recyclerView.adapter = adapter
                            recyclerView.layoutManager = linearLayoutManager
                            adapter!!.notifyDataSetChanged()
                        }else{
                            recyclerView.visibility = View.VISIBLE
                            adapter!!.promociones = promocionesResponse
                            adapter!!.notifyDataSetChanged()
                        }
                    } }override fun onError(e: Throwable) {}override fun onComplete() {}
            })
    }
}