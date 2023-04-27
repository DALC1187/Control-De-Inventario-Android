package com.example.controldeinventarios.activities

import android.R
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.controldeinventarios.api
import com.example.controldeinventarios.databinding.ActivityAgregarPromocionBinding
import com.example.controldeinventarios.models.Articulos
import com.example.controldeinventarios.preferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.ResourceObserver
import io.reactivex.schedulers.Schedulers
import java.util.*


class AgregarPromocionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAgregarPromocionBinding
    private lateinit var fecha: String
    var articulos = mutableListOf("Seleccionar articulo")
    private lateinit var a: List<Articulos>
    var aId: Long = 0L
    private lateinit var spinnerArticulos: Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarPromocionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar!!.setTitle("Agregar Promociones")
        binding.bGuardar.setOnClickListener {
            if(binding.etNombre.text.toString() == "" || binding.eDescripcion.text.toString() == "" || binding.eVigencia.text.toString() == "" || binding.eVigenciaFin.text.toString() == "" || spinnerArticulos.selectedItem.toString() == "Seleccionar articulo" || binding.eCantidad.text.toString().toDoubleOrNull() == null || binding.etCosto.text.toString().toIntOrNull() == null){
                Toast.makeText(this@AgregarPromocionActivity, "Los campos no son correctos", Toast.LENGTH_SHORT).show()
            }else{
                api.guardarPromociones(
                    "Bearer "+ preferencesHelper.tokenApi!!,
                    binding.etNombre.text.toString(),
                    binding.eDescripcion.text.toString(),
                    binding.eVigencia.text.toString(),
                    binding.eVigenciaFin.text.toString(),
                    aId,
                    binding.etCosto.text.toString().toDouble(),
                    binding.eCantidad.text.toString().toInt()
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
        val calendarioFin: Calendar = Calendar.getInstance()
        val anioFin: Int = calendario.get(Calendar.YEAR)
        val mesFin: Int = calendario.get(Calendar.MONTH)
        val diaDelMesFin: Int = calendario.get(Calendar.DAY_OF_MONTH)
        val dialogoFechaFin = DatePickerDialog(
            this@AgregarPromocionActivity,
            listenerDeDatePickerFin,
            anio,
            mes,
            diaDelMes
        )
        binding.eVigenciaFin.setOnTouchListener { v, event ->
            dialogoFechaFin.show()
            return@setOnTouchListener true
        }
        spinnerArticulos = binding.sArticulos
        val adapterArticulos: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this@AgregarPromocionActivity, R.layout.simple_spinner_item,
                articulos as List<String?>
            )
        adapterArticulos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        api.obtenerArticulos("Bearer "+ preferencesHelper.tokenApi!!)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .subscribe(object : ResourceObserver<List<Articulos>>() {
                override fun onNext(articulosResponse: List<Articulos>) {
                    articulosResponse.forEach {
                        articulos.add(it.nombre)
                    }
                    a = articulosResponse
                    spinnerArticulos.adapter = adapterArticulos
                }
                override fun onError(e: Throwable) {}
                override fun onComplete() {}
            })

        spinnerArticulos.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position != 0){
                    aId = a.get(position - 1).id
                }
            }
        }
    }

    private val listenerDeDatePicker =
        OnDateSetListener { view, anio, mes, diaDelMes ->
            this.fecha = "$anio-$mes-$diaDelMes"
            binding.eVigencia.setText("$anio-$mes-$diaDelMes")
        }
    private val listenerDeDatePickerFin =
        OnDateSetListener { view, anio, mes, diaDelMes ->
            this.fecha = "$anio-$mes-$diaDelMes"
            binding.eVigenciaFin.setText("$anio-$mes-$diaDelMes")
        }
}