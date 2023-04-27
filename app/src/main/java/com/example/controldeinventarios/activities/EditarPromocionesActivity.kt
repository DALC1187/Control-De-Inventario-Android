package com.example.controldeinventarios.activities

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.controldeinventarios.R
import com.example.controldeinventarios.api
import com.example.controldeinventarios.databinding.ActivityEditarArticuloBinding
import com.example.controldeinventarios.databinding.ActivityEditarPromocionesBinding
import com.example.controldeinventarios.models.Articulos
import com.example.controldeinventarios.models.Promociones
import com.example.controldeinventarios.preferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.ResourceObserver
import io.reactivex.schedulers.Schedulers
import java.util.*

class EditarPromocionesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditarPromocionesBinding
    private lateinit var dialogoFecha: DatePickerDialog
    private lateinit var dialogoFechaFin: DatePickerDialog
    private lateinit var fecha: String
    var promocionid = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarPromocionesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar!!.setTitle("Editar Promoción")
        promocionid = intent.getLongExtra("promocionID", 0L)
        api.detallePromociones("Bearer "+ preferencesHelper.tokenApi!!,promocionid.toString())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .subscribe(object : ResourceObserver<Promociones>() {
                override fun onNext(promocionesResponse: Promociones) {
                    binding.etNombre.setText(promocionesResponse.nombre)
                    binding.eDescripcion.setText(promocionesResponse.descripcion)
                    binding.eVigencia.setText(promocionesResponse.vigenciaInicial)
                    binding.eVigenciaFin.setText(promocionesResponse.vigenciaFinal)

                    val fecha = promocionesResponse.vigenciaInicial.split("-")
                    val anio: Int = fecha[0].toInt()
                    val mes: Int = fecha[1].toInt()
                    val diaDelMes: Int = fecha[2].toInt()

                    dialogoFecha = DatePickerDialog(
                        this@EditarPromocionesActivity,
                        listenerDeDatePicker,
                        anio,
                        mes,
                        diaDelMes
                    )

                    val fechaFin = promocionesResponse.vigenciaFinal.split("-")
                    val anioFin: Int = fechaFin[0].toInt()
                    val mesFin: Int = fechaFin[1].toInt()
                    val diaDelMesFin: Int = fechaFin[2].toInt()

                    dialogoFechaFin = DatePickerDialog(
                        this@EditarPromocionesActivity,
                        listenerDeDatePickerFin,
                        anioFin,
                        mesFin,
                        diaDelMesFin
                    )
                }
                override fun onError(e: Throwable) {}
                override fun onComplete() {}
            })

        binding.bActualizar.setOnClickListener {
            if(binding.etNombre.text.toString() == "" || binding.eDescripcion.text.toString() == "" || binding.eVigencia.text.toString() == "" || binding.eVigenciaFin.text.toString() == ""){
                Toast.makeText(this@EditarPromocionesActivity, "Los campos no son correctos", Toast.LENGTH_SHORT).show()
            }else{
                api.actualizarPromociones(
                    "Bearer "+ preferencesHelper.tokenApi!!,
                    promocionid.toString(),
                    binding.etNombre.text.toString(),
                    binding.eDescripcion.text.toString(),
                    binding.eVigencia.text.toString(),
                    binding.eVigenciaFin.text.toString(),
                )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(object : ResourceObserver<Any>() {
                        override fun onNext(genericResponse: Any) {
                            Toast.makeText(this@EditarPromocionesActivity, "Promoción actualizada correctamente", Toast.LENGTH_SHORT).show()
                        }
                        override fun onError(e: Throwable) {}
                        override fun onComplete() {}
                    })
            }
        }

        binding.eVigencia.setOnTouchListener { v, event ->
            dialogoFecha.show()
            return@setOnTouchListener true
        }

        binding.eVigenciaFin.setOnTouchListener { v, event ->
            dialogoFechaFin.show()
            return@setOnTouchListener true
        }

    }

    private val listenerDeDatePicker =
        DatePickerDialog.OnDateSetListener { view, anio, mes, diaDelMes ->
            this.fecha = "$anio-$mes-$diaDelMes"
            binding.eVigencia.setText("$anio-$mes-$diaDelMes")
        }

    private val listenerDeDatePickerFin =
        DatePickerDialog.OnDateSetListener { view, anio, mes, diaDelMes ->
            this.fecha = "$anio-$mes-$diaDelMes"
            binding.eVigenciaFin.setText("$anio-$mes-$diaDelMes")
        }
}