package com.example.customlistview

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.customlistview.databinding.ActivityMainBinding
import com.example.customlistview.databinding.ActivitySecondBinding
import java.io.IOException

class SecondActivity : AppCompatActivity() {
    private val GALLERY_REQUEST = 777
    var bitmap: Bitmap? = null
    var products: MutableList<Product> = mutableListOf()
    private lateinit var binding: ActivitySecondBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySecondBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.toolbarSecond.title = "Магазин продуктов"
        setSupportActionBar(binding.toolbarSecond)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.imageViewIV.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, GALLERY_REQUEST)
        }

        binding.saveBTN.setOnClickListener {
            createProduct()

            val listAdapter = ListAdapter(this, products)
            binding.listViewLV.adapter = listAdapter
            listAdapter.notifyDataSetChanged()
            clearEditFields()
        }

    }

    private fun createProduct() {
        val productName = binding.nameET.text.toString()
        val productPrice = binding.priceET.text.toString()
        val productImage = bitmap
        val product = Product(productName, productPrice.toString().toInt(), productImage)
        products.add(product)
    }

    private fun clearEditFields() {
        binding.nameET.text.clear()
        binding.priceET.text.clear()
        binding.imageViewIV.setImageResource(R.drawable.ic_product)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_second, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.exitMenuSecond -> {
                finishAndRemoveTask()
                finishAffinity()
                finish()

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GALLERY_REQUEST -> if (resultCode === RESULT_OK) {
                val selectedImage: Uri? = data?.data
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImage)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                binding.imageViewIV.setImageBitmap(bitmap)
            }
        }
    }
}