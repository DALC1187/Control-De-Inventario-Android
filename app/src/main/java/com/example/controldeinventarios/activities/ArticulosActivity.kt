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
import com.example.controldeinventarios.api
import com.example.controldeinventarios.databinding.ActivityArticulosBinding
import com.example.controldeinventarios.models.Articulos
import com.example.controldeinventarios.preferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.ResourceObserver
import io.reactivex.schedulers.Schedulers

class ArticulosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArticulosBinding
    private var adapter: ArticulosAdapter? = null
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticulosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar!!.setTitle("Articulos")
        recyclerView = binding.recycler
        binding.bActualizar.setOnClickListener {
            obtenerArticulos()
        }
        binding.bAgregar.setOnClickListener {
            val intent=Intent(this, AgregarArticuloActivity::class.java)
            startActivity(intent)
        }
        obtenerArticulos()
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
        }
        return super.onOptionsItemSelected(item)
    }

    fun obtenerArticulos(){
        recyclerView.visibility = View.GONE
        api.obtenerArticulos("Bearer "+ preferencesHelper.tokenApi!!)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .subscribe(object : ResourceObserver<List<Articulos>>() {
                override fun onNext(articulosResponse: List<Articulos>) {
                    if(articulosResponse.isNotEmpty()){
                        if(adapter == null){
                            recyclerView.visibility = View.VISIBLE
                            adapter = ArticulosAdapter(this@ArticulosActivity, articulosResponse)
                            val linearLayoutManager = LinearLayoutManager(this@ArticulosActivity)
                            recyclerView.adapter = adapter
                            recyclerView.layoutManager = linearLayoutManager
                            adapter!!.notifyDataSetChanged()
                        }else{
                            recyclerView.visibility = View.VISIBLE
                            adapter!!.articulos = articulosResponse
                            adapter!!.notifyDataSetChanged()
                        }
                    } }override fun onError(e: Throwable) {}override fun onComplete() {}
            })
    }
}