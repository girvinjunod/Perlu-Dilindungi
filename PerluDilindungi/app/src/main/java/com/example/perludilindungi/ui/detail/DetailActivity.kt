package com.example.perludilindungi.ui.detail

import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.perludilindungi.R
import com.example.perludilindungi.databinding.FragmentDetailFaskesBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import timber.log.Timber


class DetailActivity : AppCompatActivity() {

    private lateinit var binding: FragmentDetailFaskesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_detail_faskes)

        binding = FragmentDetailFaskesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.constraintLayout)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        navView.setupWithNavController(navController)

        val namaView: TextView = findViewById(R.id.namaFaskes)
        val alamatView: TextView = findViewById(R.id.alamat)
        val jenisView: TextView = findViewById(R.id.jenisFaskes)
        val telpView: TextView = findViewById(R.id.noTelp)
        val kodeView: TextView = findViewById(R.id.kodeFaskes)
        val statusView: TextView = findViewById(R.id.statusFaskes)
        val statusImageView : ImageView = findViewById(R.id.status_image)

        namaView.text = intent.getStringExtra("nama_faskes")
        alamatView.text = intent.getStringExtra("alamat_faskes")
        jenisView.text = intent.getStringExtra("jenis_faskes")
        telpView.text = "No telp: " + intent.getStringExtra("telp_faskes")
        kodeView.text = "KODE: " + intent.getStringExtra("kode_faskes")

//        set warna jenis
        if (jenisView.text.toString() == "PUSKESMAS"){
            jenisView.setBackgroundColor(Color.parseColor("#ff80ff"))
        }else{
            jenisView.setBackgroundColor(Color.parseColor("#8533ff"))
        }

//        set gambar
        if (intent.getStringExtra("status_faskes").toString() == "Siap Vaksinasi") {
            statusImageView.setImageResource(R.drawable.ic_baseline_check_circle_outline_24)
        } else {
            statusImageView.setImageResource(R.drawable.ic_baseline_cancel_24)
        }
        statusView.text = "Fasilitas ini\n" + intent.getStringExtra("status_faskes")?.uppercase()
    }
}