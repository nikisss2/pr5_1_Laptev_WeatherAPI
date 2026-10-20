package com.example.pr5_weather_laptev

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import org.json.JSONObject
import java.util.Locale
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private lateinit var city1: AppCompatEditText
    private lateinit var btnGet: AppCompatButton
    private lateinit var tvCity: AppCompatTextView
    private lateinit var tvTemp: AppCompatTextView
    private lateinit var tvDesc: AppCompatTextView
    private lateinit var tvPressure: AppCompatTextView
    private lateinit var tvWind: AppCompatTextView
    private lateinit var tvHumidity: AppCompatTextView

    private val key = "e4dfe713dfb66c542f38440e88209677"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        city1 = findViewById(R.id.city1)
        btnGet = findViewById(R.id.btnGet)
        tvCity = findViewById(R.id.tvCity)
        tvTemp = findViewById(R.id.tvTemp)
        tvDesc = findViewById(R.id.tvDesc)
        tvPressure = findViewById(R.id.tvPressure)
        tvWind = findViewById(R.id.tvWind)
        tvHumidity = findViewById(R.id.tvHumidity)

        btnGet.setOnClickListener {
            val city = city1.text.toString().trim()
            getResult(city)
        }
    }

    private fun getResult(city: String) {
        if (city.isNotEmpty()) {
            val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$key&units=metric&lang=ru"
            val queue = Volley.newRequestQueue(this)
            val stringRequest = StringRequest(
                Request.Method.GET,
                url,
                { response ->
                    Log.d("MyLog", "Response: $response")
                    try {
                        val obj = JSONObject(response)
                        val cityName = obj.getString("name")

                        val main = obj.getJSONObject("main")
                        val temp = main.getDouble("temp")
                        val pressure = main.getInt("pressure")
                        val pressureMm = (pressure * 0.750062).roundToInt()
                        val humidity = main.getInt("humidity")

                        val wind = obj.getJSONObject("wind")
                        val windSpeed = wind.getDouble("speed")

                        val weatherArray = obj.getJSONArray("weather")
                        val desc = if (weatherArray.length() > 0) {
                            weatherArray.getJSONObject(0).getString("description")
                        } else {
                            ""
                        }

                        Log.d("MyLog", "Город: $cityName, Температура: $temp, Давление: $pressure, Ветер: $windSpeed")

                        tvCity.text = getString(R.string.city_label, cityName)
                        tvTemp.text = getString(R.string.temp_label, String.format(Locale.US, "%.1f", temp))
                        tvDesc.text = getString(R.string.desc_label, desc.replaceFirstChar { it.uppercase() })
                        tvPressure.text = getString(R.string.pressure_label, pressure.toString(), pressureMm.toString())
                        tvWind.text = getString(R.string.wind_label, windSpeed.toString())
                        tvHumidity.text = getString(R.string.humidity_label, humidity.toString())

                    } catch (e: Exception) {
                        Log.d("MyLog", "Parse error: ${e.message}")
                        Toast.makeText(this, getString(R.string.network_error, e.message ?: ""), Toast.LENGTH_SHORT).show()
                    }
                },
                { error ->
                    Log.d("MyLog", "Volley error: $error")
                    val errorMsg = if (error.networkResponse?.statusCode == 404) {
                        getString(R.string.city_not_found)
                    } else {
                        getString(R.string.network_error, error.message ?: "")
                    }
                    Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show()
                }
            )
            queue.add(stringRequest)
        } else {
            val sn = Snackbar.make(findViewById(android.R.id.content), R.string.empty_city_error, Snackbar.LENGTH_LONG)
            sn.setActionTextColor(Color.RED)
            sn.show()
        }
    }
}
