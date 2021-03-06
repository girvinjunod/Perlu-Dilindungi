package com.example.perludilindungi.ui.checkin

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.*
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import com.example.perludilindungi.MainActivity
import com.example.perludilindungi.R
import com.example.perludilindungi.network.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import timber.log.Timber

class CheckIn : AppCompatActivity(), SensorEventListener {

    private lateinit var codeScanner: CodeScanner
    private var temperature = 0f
    private lateinit var sensorManager: SensorManager
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lastLocation: Location? = null
    private var hasilLat: Float? = null
    private var hasilLong: Float? = null



    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_check_in)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        setupPermissions()
        codeScanner()

        val backfromcheckin: Button = findViewById(R.id.backfromcheckin)
        backfromcheckin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()
//        loadAmbientTemperature()

    }


    private fun rawJSON(text:String) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val service = PerluDilindungiApi
        val JsonParser: JsonParser = JsonParser()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Timber.i("No permission")
            Toast.makeText(this, "Location access is required", Toast.LENGTH_SHORT).show();
        } else{
            fusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                if (task.isSuccessful && task.result != null) {
                    lastLocation = task.result
                    val jsonObject = JSONObject()
                    jsonObject.put("qrCode", text)
                    jsonObject.put("latitude", (lastLocation)!!.latitude)
                    jsonObject.put("longitude", (lastLocation)!!.latitude)
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
                                Timber.i(prettyJson)

//                            tv_text.text = prettyJson
                                val foos = Response(prettyJson)
                                val hasildata = foos.getJSONObject("data").getString("userStatus")
//                            tv_text.text = hasildata.toString()

                                if (hasildata.toString() == "green" || hasildata.toString() == "yellow") {
                                    val kotakstatus: TextView = findViewById(R.id.kotakstatus)
                                    kotakstatus.setVisibility(View.GONE)

                                    val gambarstatus: ImageView = findViewById(R.id.imageView2)
                                    gambarstatus.setVisibility(View.VISIBLE)
                                    val tulisanberhasil: TextView = findViewById(R.id.textView7)
                                    tulisanberhasil.setVisibility(View.VISIBLE)

                                    val gambarstatusgagal: ImageView = findViewById(R.id.imageView3)
                                    gambarstatusgagal.setVisibility(View.GONE)
                                    val tulisangagal: TextView = findViewById(R.id.textView8)
                                    tulisangagal.setVisibility(View.GONE)
                                    val reasongagal: TextView = findViewById(R.id.textView9)
                                    reasongagal.setVisibility(View.GONE)
                                    if (hasildata.toString() == "green") {
                                        kotakstatus.setVisibility(View.VISIBLE)
                                        kotakstatus.text = "Your status is GREEN"
                                        kotakstatus.setBackgroundColor(Color.parseColor("#34AB40"))
                                        kotakstatus.setTextColor(Color.WHITE)
                                    }
                                    else if (hasildata.toString() == "yellow") {
                                        kotakstatus.setVisibility(View.VISIBLE)
                                        kotakstatus.text = "Your status is YELLOW"
                                        kotakstatus.setBackgroundColor(Color.YELLOW)
                                        kotakstatus.setTextColor(Color.BLACK)
                                    }
                                }

                                else if (hasildata.toString() == "red" || hasildata.toString() == "black") {
                                    val kotakstatus: TextView = findViewById(R.id.kotakstatus)
                                    kotakstatus.setVisibility(View.GONE)

                                    val gambarstatus: ImageView = findViewById(R.id.imageView2)
                                    gambarstatus.setVisibility(View.GONE)
                                    val tulisanberhasil: TextView = findViewById(R.id.textView7)
                                    tulisanberhasil.setVisibility(View.GONE)

                                    val gambarstatusgagal: ImageView = findViewById(R.id.imageView3)
                                    gambarstatusgagal.setVisibility(View.VISIBLE)
                                    val tulisangagal: TextView = findViewById(R.id.textView8)
                                    tulisangagal.setVisibility(View.VISIBLE)
                                    val reasongagal: TextView = findViewById(R.id.textView9)
                                    reasongagal.text = foos.getJSONObject("data").getString("reason").toString()
                                    reasongagal.setVisibility(View.VISIBLE)

                                    if (hasildata.toString() == "red") {

                                        kotakstatus.setVisibility(View.VISIBLE)
                                        kotakstatus.text = "Your status is RED"
                                        kotakstatus.setBackgroundColor(Color.RED)
                                        kotakstatus.setTextColor(Color.WHITE)
                                    }
                                    if (hasildata.toString() == "black") {
                                        kotakstatus.setVisibility(View.VISIBLE)
                                        kotakstatus.text = "Your status is BLACK"
                                        kotakstatus.setBackgroundColor(Color.BLACK)
                                        kotakstatus.setTextColor(Color.WHITE)
                                    }
                                }



                            }
                        }
//                    tv_text.text = response.toString()

                    }
                }
            }
        }


    }
    private fun codeScanner() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val scn: CodeScannerView = findViewById(R.id.scn)

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
                    getLastLocation()
                    rawJSON(it.text)
                }
            }

            errorCallback = ErrorCallback {
                runOnUiThread {
                    Timber.i("codeScanner: ${it.message}")
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
            REQUEST_PERMISSIONS_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    getLastLocation()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        private const val CAMERA_REQ = 101
        private const val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    }

    private fun loadAmbientTemperature() {
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST)
        } else {
//            Toast.makeText(this, "No temperature sensor found!", Toast.LENGTH_LONG).show()
        }
    }

    private fun unregisterAll() {
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(sensorEvent: SensorEvent) {
        val temperature_text: TextView = findViewById(R.id.temperature_text)
        if (sensorEvent.values.size > 0) {
            temperature = sensorEvent.values[0]
            temperature_text.text = "$temperature??C"
//            supportActionBar!!.setTitle(getString(R.string.app_name) + " : " + temperature)
//            Toast.makeText(this, temperature.toString(), Toast.LENGTH_LONG).show()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, i: Int) {}

    class Response(json: String) : JSONObject(json) {
        val type: String? = this.optString("type")
        val data = this.optJSONArray("data")
            ?.let { 0.until(it.length()).map { i -> it.optJSONObject(i) } } // returns an array of JSONObject
            ?.map { Foo(it.toString()) } // transforms each JSONObject of the array into Foo
    }

    class Foo(json: String) : JSONObject(json) {
        val userStatus = this.optString("userStatus")
        val reason: String? = this.optString("reason")
    }

    private fun getLastLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "Location access is required", Toast.LENGTH_SHORT).show();
            return
        } else{
            fusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                if (task.isSuccessful && task.result != null) {
                    lastLocation = task.result
                    hasilLat?.let{hasilLat = (lastLocation)!!.latitude.toFloat()}
                    hasilLong?.let{hasilLong = (lastLocation)!!.longitude.toFloat()}
                }
            }
            return
        }

    }
}