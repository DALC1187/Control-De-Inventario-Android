package com.example.controldeinventarios.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.controldeinventarios.databinding.ActivityArticulosBinding

class ArticulosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArticulosBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticulosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar!!.setTitle("Articulos")
    }
}