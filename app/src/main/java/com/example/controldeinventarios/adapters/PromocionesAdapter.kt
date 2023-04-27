package com.example.controldeinventarios.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.controldeinventarios.activities.EditarPromocionesActivity
import com.example.controldeinventarios.api
import com.example.controldeinventarios.databinding.ViewholderPromocionesBinding
import com.example.controldeinventarios.models.Promociones
import com.example.controldeinventarios.preferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.ResourceObserver
import io.reactivex.schedulers.Schedulers

class PromocionesAdapter(
    private val context: Context,
    var promociones: List<Promociones>
): RecyclerView.Adapter<PromocionesAdapter.ViewHolder>() {


    class ViewHolder(
        val binding: ViewholderPromocionesBinding,
        val context: Context
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(promociones: Promociones) = with(itemView){
            binding.nombre.text = promociones.nombre
            binding.descripcion.text = promociones.descripcion
            binding.vigencia.text = "${promociones.vigenciaInicial} - ${promociones.vigenciaFinal}"

            binding.bEliminar.setOnClickListener {
               api.eliminarPromociones("Bearer "+ preferencesHelper.tokenApi!!,promociones.id.toString())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(object : ResourceObserver<Any>() {
                        override fun onNext(genericResponse: Any) {
                            Toast.makeText(context, "Promoción eliminada correctamente", Toast.LENGTH_SHORT).show()
                        }
                        override fun onError(e: Throwable) {}
                        override fun onComplete() {}
                    })
            }
            binding.bEditar.setOnClickListener {
                val intent = Intent(context, EditarPromocionesActivity::class.java)
                intent.putExtra("promocionID", promociones.id)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderPromocionesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(promociones[position])
    }

    override fun getItemCount(): Int {
        return promociones.size
    }


}