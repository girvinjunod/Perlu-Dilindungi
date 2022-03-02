package com.example.perludilindungi.ui.checkin

import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import com.example.perludilindungi.MainActivity
import com.example.perludilindungi.R
import com.example.perludilindungi.network.*
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject


class CheckIn : AppCompatActivity(), SensorEventListener {

    private lateinit var codeScanner: CodeScanner
    private var temperature = 0f
    private lateinit var sensorManager: SensorManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_check_in)
        var sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        setupPermissions()
        codeScanner()

        val backfromcheckin: Button = findViewById(R.id.backfromcheckin)
        backfromcheckin.setOnClickListener({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        })


    }


    private fun rawJSON(text:String, tv_text: TextView) {
        val service = PerluDilindungiApi
        val JsonParser: JsonParser = JsonParser()
        val jsonObject = JSONObject()
        jsonObject.put("qrCode", text)
        jsonObject.put("latitude", -6.1351855)
        jsonObject.put("longitude", 11.0323457)
        val jsonObjectString = jsonObject.toString()

        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.retrofitService.getStatus(requestBody)

            withContext(Dispatchers.Main){
                if (response.isSuccessful){
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                        JsonParser.parse(response.body()?.string())
                    )
                    Log.d("Pretty Printed JSON :", prettyJson)
                    tv_text.text = prettyJson
                }
            }
//            tv_text.text = response.toString()

        }
    }
    private fun codeScanner() {
        val scn: CodeScannerView = findViewById(R.id.scn)
        val tv_text: TextView = findViewById(R.id.tv_text)
        codeScanner = CodeScanner(this, scn)



        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                runOnUiThread {
                    rawJSON(it.text, tv_text)
                }
            }

            errorCallback = ErrorCallback {
                runOnUiThread {
                    Log.e("Main", "codeScanner: ${it.message}")
                }
            }

            scn.setOnClickListener {
                codeScanner.startPreview()
            }

        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
        loadAmbientTemperature()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
        unregisterAll()
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this, arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQ
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_REQ -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        this,
                        "You need the camera permission to use this app",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        private const val CAMERA_REQ = 101
    }

    private fun loadAmbientTemperature() {
        var sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST)
        } else {
            Toast.makeText(this, "No Ambient Temperature Sensor !", Toast.LENGTH_LONG).show()
        }
    }

    private fun unregisterAll() {
        var sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(sensorEvent: SensorEvent) {
        val temperature_text: TextView = findViewById(R.id.temperature_text)
        if (sensorEvent.values.size > 0) {
            temperature = sensorEvent.values[0]
            temperature_text.text = "$temperature°C"
//            supportActionBar!!.setTitle(getString(R.string.app_name) + " : " + temperature)
//            Toast.makeText(this, temperature.toString(), Toast.LENGTH_LONG).show()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, i: Int) {}

//    private fun rawJSON() {
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://perludilindungi.herokuapp.com/")
//            .build()
//        val service = PerluDilindungiApi
//        val result = service.retrofitService.getStatus()
//    }

}