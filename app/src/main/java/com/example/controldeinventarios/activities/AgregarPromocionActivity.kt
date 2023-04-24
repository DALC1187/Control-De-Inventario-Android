package com.example.controldeinventarios.activities

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.controldeinventarios.api
import com.example.controldeinventarios.databinding.ActivityAgregarPromocionBinding
import com.example.controldeinventarios.preferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.ResourceObserver
import io.reactivex.schedulers.Schedulers
import java.util.*


class AgregarPromocionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAgregarPromocionBinding
    private lateinit var fecha: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarPromocionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar!!.setTitle("Agregar Promociones")
        binding.bGuardar.setOnClickListener {
            api.guardarPromociones(
                "Bearer "+ preferencesHelper.tokenApi!!,
                binding.etNombre.text.toString(),
                binding.eDescripcion.text.toString(),
                binding.eVigencia.text.toString(),
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(object : ResourceObserver<Any>() {
                    override fun onNext(genericResponse: Any) {
                        Toast.makeText(this@AgregarPromocionActivity, "Promoción agregada correctamente", Toast.LENGTH_SHORT).show()
                    }
                    override fun onError(e: Throwable) {}
                    override fun onComplete() {}
                })
        }

        val calendario: Calendar = Calendar.getInstance()
        val anio: Int = calendario.get(Calendar.YEAR)
        val mes: Int = calendario.get(Calendar.MONTH)
        val diaDelMes: Int = calendario.get(Calendar.DAY_OF_MONTH)

        val dialogoFecha = DatePickerDialog(
            this@AgregarPromocionActivity,
            listenerDeDatePicker,
            anio,
            mes,
            diaDelMes
        )
        binding.eVigencia.setOnTouchListener { v, event ->
            dialogoFecha.show()
            return@setOnTouchListener true
        }
    }

    private val listenerDeDatePicker =
        OnDateSetListener { view, anio, mes, diaDelMes ->
            this.fecha = "$anio-$mes-$diaDelMes"
            binding.eVigencia.setText("$anio-$mes-$diaDelMes")
        }
}