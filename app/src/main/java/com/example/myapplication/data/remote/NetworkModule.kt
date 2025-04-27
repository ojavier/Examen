package com.example.myapplication.data.remote

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {

    // La URL base de la API (sin el signo de interrogación al final)
    private const val BASE_URL = "https://api.api-ninjas.com/v1/"

    // Coloca tu clave API aquí
    private const val API_KEY = "wLVPN1zV08lJYF7uXqgyPw==zVwp6TlVcAO1NLUf"

    // Configuración del cliente OkHttp
    val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("X-Api-Key", API_KEY) // Agregar la clave de la API en los encabezados
                .build()

            // Log de la solicitud
            Log.d("API Request", "Request: ${request.url} - Headers: ${request.headers}")

            val response = chain.proceed(request)

            // Crear un ResponseBody temporal para poder leer el cuerpo y loguearlo
            val responseBody = response.body?.string()  // Leer el cuerpo de la respuesta

            // Log de la respuesta con el código de estado y el cuerpo de la respuesta
            Log.d("API Response", "Response Code: ${response.code} - Response Body: $responseBody")

            // Regresar la respuesta con el cuerpo original, ya que Retrofit necesita este cuerpo para procesarlo
            return@addInterceptor response.newBuilder()
                .body(ResponseBody.create(response.body?.contentType(), responseBody ?: ""))
                .build()
        }
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Habilitar nivel BODY para ver el cuerpo completo de las respuestas
        })
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    // Crear Retrofit con el cliente configurado
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)  // Usar el cliente OkHttp configurado con el interceptor
        .addConverterFactory(GsonConverterFactory.create()) // Usar Gson para la deserialización de respuestas
        .build()

    // Crear el servicio de la API
    val apiService: SudokuApiService = retrofit.create(SudokuApiService::class.java)
}
