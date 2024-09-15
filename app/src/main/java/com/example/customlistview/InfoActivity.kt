package com.example.customlistview

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.customlistview.databinding.ActivityInfoBinding
import com.example.customlistview.databinding.ActivityMainBinding

class InfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityInfoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.toolbarInfo.title = "Магазин продуктов"
        binding.toolbarInfo.subtitle = "версия 1.0"
        setSupportActionBar(binding.toolbarInfo)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val productName = intent.extras?.getString("name")
        val productPrice = intent.extras?.getInt("price")
        val productInfo = intent.extras?.getString("info")
        val productImage = intent.extras?.getString("image")


        binding.infoNameTV.text = productName
        binding.infoPriceTV.text = productPrice.toString()
        binding.infoInfoTV.text = productInfo
        binding.infoImageIV.setImageURI(Uri.parse(productImage))



    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.exitMenuMain -> {
                finishAndRemoveTask()
                finishAffinity()
                finish()

            }
        }
        return super.onOptionsItemSelected(item)
    }
}