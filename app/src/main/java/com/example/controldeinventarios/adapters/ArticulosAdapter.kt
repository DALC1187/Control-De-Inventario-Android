package com.example.controldeinventarios.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.controldeinventarios.activities.EditarArticuloActivity
import com.example.controldeinventarios.api
import com.example.controldeinventarios.databinding.ViewholderArticulosBinding
import com.example.controldeinventarios.models.Articulos
import com.example.controldeinventarios.preferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.ResourceObserver
import io.reactivex.schedulers.Schedulers

class ArticulosAdapter(
    private val context: Context,
    var articulos: List<Articulos>
): RecyclerView.Adapter<ArticulosAdapter.ViewHolder>() {


    class ViewHolder(
        val binding: ViewholderArticulosBinding,
        val context: Context
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(articulos: Articulos) = with(itemView){
            binding.nombre.text = articulos.nombre
            binding.costoPieza.text = articulos.costoPieza.toString()
            binding.numPiezaPaquete.text = articulos.numPiezaPaquete.toString()
            binding.bEliminar.setOnClickListener {
                api.eliminarArticulos("Bearer "+ preferencesHelper.tokenApi!!,articulos.id.toString())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(object : ResourceObserver<Any>() {
                        override fun onNext(genericResponse: Any) {
                            Toast.makeText(context, "Articulo eliminado correctamente", Toast.LENGTH_SHORT).show()
                        }
                        override fun onError(e: Throwable) {}
                        override fun onComplete() {}
                    })
            }
            binding.bEditar.setOnClickListener {
                val intent = Intent(context, EditarArticuloActivity::class.java)
                intent.putExtra("articuloID", articulos.id)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderArticulosBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(articulos[position])
    }

    override fun getItemCount(): Int {
        return articulos.size
    }


}