package com.lotus.ktorserver.ui

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.lotus.ktorserver.R
import com.lotus.ktorserver.adapter.UrunAdapter
import com.lotus.ktorserver.databinding.ActivityMainBinding
import com.lotus.ktorserver.db.AppDatabase
import com.lotus.ktorserver.helpers.IpHelper
import com.lotus.ktorserver.helpers.UiState
import com.lotus.ktorserver.models.Urun
import com.lotus.ktorserver.network.KtorServer
import com.lotus.ktorserver.viewmodels.UrunViewModel
import com.lotus.ktorserver.viewmodels.UrunViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val db by lazy { AppDatabase.getDatabase(this) }

    private val viewModel: UrunViewModel by viewModels {
        UrunViewModelFactory(AppDatabase.getDatabase(this))
    }
    private val adapter = UrunAdapter()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initViews()
    }

    private fun initViews(){

        val currentIp = IpHelper.getDeviceIpAddress(this)
        binding.tvCihazIp.text = "Cihaz Adresi: $currentIp"

        // Test için birkaç ürün ekleyelim
//        CoroutineScope(Dispatchers.IO).launch {
//            db.urunDao().upsertUrun(Urun(1, "Laptop", 15000.0))
//            db.urunDao().upsertUrun(Urun(5, "Mouse", 250.0))
//        }

        binding.textIp.text = "Sunucu Adresi:\n http://$currentIp:6868/update/{urunId}"
        binding.textStatus.text = "Sunucu başlatılıyor..."

        binding.btnStartServer.setOnClickListener {
            // Server'ı başlat
            KtorServer.startServer(db)
            binding.textStatus.text = "Sunucu AKTİF ✓\nPort: 6868"
        }



        setAdapter()

    }

    private fun setAdapter(){
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        binding.progressBar.visibility = VISIBLE
                        binding.tvError.visibility = GONE
                        binding.recyclerView.visibility = GONE
                    }
                    is UiState.Success -> {
                        binding.progressBar.visibility = GONE
                        binding.recyclerView.visibility = VISIBLE
                        adapter.submitList(state.urunler)
                        binding.recyclerView.scrollToPosition(0)
                    }
                    is UiState.Error -> {
                        binding.progressBar.visibility = GONE
                        binding.tvError.visibility = VISIBLE
                        binding.tvError.text = "Hata: ${state.message}"
                        binding.recyclerView.visibility = GONE
                    }
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        // Uygulama ön plana geldiğinde Flow’u yeniden başlat
        // (Zaten çalışıyor ama emin olalım diye)
        viewModel.observeUrunler() // bu fonksiyonu ekleyelim
    }


    override fun onDestroy() {
        KtorServer.stopServer()
        super.onDestroy()
    }

}