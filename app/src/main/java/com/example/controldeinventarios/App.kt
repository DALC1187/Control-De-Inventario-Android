package com.example.controldeinventarios

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val preferencesHelper: PreferencesHelper by lazy { App.prefs!! }
val api: Api by lazy { App.api!! }

class App : Application() {

    companion object {
        var prefs: PreferencesHelper? = null
        var api: Api? = null
        var context: Context? = null
        var shareInstance: App? = null
    }

    @SuppressLint("CheckResult")
    override fun onCreate() {
        super.onCreate()

        prefs = PreferencesHelper(applicationContext)
        shareInstance = this
        context = this

        //Instantiate Logging interceptor
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BASIC

        //Build HTTP Client
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        //Build Retrofit Client
        val retrofit = Retrofit.Builder()
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://192.168.1.12:8019/api/")
            .build()

        api = retrofit.create(Api::class.java)
    }
}