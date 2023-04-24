package com.example.controldeinventarios.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.controldeinventarios.activities.EditarUsuarioActivity
import com.example.controldeinventarios.api
import com.example.controldeinventarios.databinding.ViewholderUsuariosBinding
import com.example.controldeinventarios.models.Usuarios
import com.example.controldeinventarios.preferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.ResourceObserver
import io.reactivex.schedulers.Schedulers

class UsuariosAdapter(
    private val context: Context,
    var usuarios: List<Usuarios>
) : RecyclerView.Adapter<UsuariosAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderUsuariosBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(usuarios[position])
    }

    override fun getItemCount(): Int {
        return usuarios.size
    }

    class ViewHolder(
        val binding: ViewholderUsuariosBinding,
        val context: Context
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(usuario: Usuarios) = with(itemView){
            binding.nombre.text = usuario.nombre
            binding.apellidoPaterno.text = usuario.apellido_paterno
            binding.apellidoMaterno.text = usuario.apellido_materno
            binding.tipoUsuario.text = usuario.tipo_usuario
            binding.bEliminar.setOnClickListener {
            api.eliminarUsuarios("Bearer "+preferencesHelper.tokenApi!!,usuario.id.toString())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .subscribe(object : ResourceObserver<Any>() {
                override fun onNext(genericResponse: Any) {
                    Toast.makeText(context, "Usuario eliminado correctamente", Toast.LENGTH_SHORT).show()
                }
                override fun onError(e: Throwable) {}
                override fun onComplete() {}
            })
            }
            binding.bEditar.setOnClickListener {
                val intent = Intent(context, EditarUsuarioActivity::class.java)
                intent.putExtra("usuarioid", usuario.id)
                context.startActivity(intent)
            }
        }
    }
}