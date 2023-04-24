package com.example.controldeinventarios.activities

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.controldeinventarios.api
import com.example.controldeinventarios.databinding.ActivityAgregarSiniestrosBinding
import com.example.controldeinventarios.preferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.ResourceObserver
import io.reactivex.schedulers.Schedulers
import java.io.ByteArrayOutputStream
import java.util.*


class AgregarSiniestrosActivity : AppCompatActivity() {
    val REQUEST_CODE = 200
    val REQUEST_CODE_SUPERVISOR = 201
    private lateinit var binding: ActivityAgregarSiniestrosBinding
    private lateinit var ministro: String
    private lateinit var supervisor: String
    private lateinit var spinner: Spinner
    val tiposSiniestros = arrayOf<String?>("Robo", "Incendio", "Inundación")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarSiniestrosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar!!.setTitle("Agregar Siniestro")
        spinner = binding.sTipoDeSiniestro
        val adapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this@AgregarSiniestrosActivity, android.R.layout.simple_spinner_item, tiposSiniestros)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        val calendario: Calendar = Calendar.getInstance()
        val anio: Int = calendario.get(Calendar.YEAR)
        val mes: Int = calendario.get(Calendar.MONTH)
        val diaDelMes: Int = calendario.get(Calendar.DAY_OF_MONTH)
        val dialogoFecha = DatePickerDialog(
            this@AgregarSiniestrosActivity,
            listenerDeDatePicker,
            anio,
            mes,
            diaDelMes
        )
        binding.eFechaSiniestro.setOnTouchListener { v, event ->
            dialogoFecha.show()
            return@setOnTouchListener true
        }
        val c: Calendar = Calendar.getInstance()
        val hora: Int = c.get(Calendar.HOUR_OF_DAY)
        val minutos: Int = c.get(Calendar.MINUTE)
        val dialogoHora = TimePickerDialog(
            this@AgregarSiniestrosActivity,
            listenerDeTimePicker,
            hora,
            minutos,
            false
        )
        binding.eHoraSiniestro.setOnTouchListener { v, event ->
            dialogoHora.show()
            return@setOnTouchListener true
        }
        binding.iBMinisterio.setOnClickListener { capturePhoto(REQUEST_CODE) }
        binding.iBSupervisor.setOnClickListener { capturePhoto(REQUEST_CODE_SUPERVISOR) }
        binding.bGuardar.setOnClickListener {
            api.guardarSiniestro(
                "Bearer "+ preferencesHelper.tokenApi!!,
                binding.eFechaSiniestro.text.toString(),
                binding.eHoraSiniestro.text.toString(),
                binding.eDescripcion.text.toString(),
                spinner.selectedItem.toString(),
                ministro,
                supervisor
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(object : ResourceObserver<Any>() {
                    override fun onNext(genericResponse: Any) {
                        Toast.makeText(this@AgregarSiniestrosActivity, "Siniestro agregado correctamente", Toast.LENGTH_SHORT).show()
                    }
                    override fun onError(e: Throwable) {}
                    override fun onComplete() {}
                })
        }
    }

    fun capturePhoto(requestCode: Int) {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_FULL_SCREEN, true)
        startActivityForResult(cameraIntent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE && data != null){
            binding.iMMinisterio.setImageBitmap(data.extras!!.get("data") as Bitmap)
            ministro = encodeImage(data.extras!!.get("data") as Bitmap)!!
        }
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_SUPERVISOR && data != null){
            binding.iMSupervisor.setImageBitmap(data.extras!!.get("data") as Bitmap)
            supervisor = encodeImage(data.extras!!.get("data") as Bitmap)!!
            Log.i("FOTO", supervisor)
        }
    }

    private val listenerDeDatePicker =
        DatePickerDialog.OnDateSetListener { view, anio, mes, diaDelMes ->
            binding.eFechaSiniestro.setText("$anio-$mes-$diaDelMes")
        }

    private val listenerDeTimePicker =
        TimePickerDialog.OnTimeSetListener { view, hora, minutos ->
            binding.eHoraSiniestro.setText("$hora:$minutos")
        }

    private fun encodeImage(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return android.util.Base64.encodeToString(b, android.util.Base64.DEFAULT)
    }
}