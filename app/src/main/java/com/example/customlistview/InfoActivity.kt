package com.example.customlistview

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.customlistview.databinding.ActivityInfoBinding


@Suppress("DEPRECATED_IDENTITY_EQUALS", "DEPRECATION", "UNCHECKED_CAST", "NAME_SHADOWING")
class InfoActivity : AppCompatActivity() {
    private var photoUri: Uri? = null
    private val GALLERY_REQUEST = 888
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

        val product: Product = intent.extras?.getSerializable("product") as Product
        val products = intent.getSerializableExtra("products")
        val item = intent.extras?.getInt("position")
        var check = intent.extras?.getBoolean("check")



        binding.infoNameTV.text = product.name
        binding.infoPriceTV.text = product.price
        binding.infoInfoTV.text = product.info
        binding.infoImageIV.setImageURI(Uri.parse(product.image))

        binding.infoImageIV.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, GALLERY_REQUEST)

        }
        binding.updateBTN.setOnClickListener{
            val product = Product(
                binding.infoNameTV.text.toString(),
                binding.infoPriceTV.text.toString(),
                binding.infoInfoTV.text.toString(),
                photoUri.toString()
            )
            val list: MutableList<Product> = products as MutableList<Product>
            if ( item != null) {
                swap(item, product, products)
            }
            check = false
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("list", list as ArrayList<Product>)
            intent.putExtra("newCheck", check)
            startActivity(intent)
            finish()
        }


    }
    fun swap(item: Int, product: Product, products: MutableList<Product>){
        products.add(item + 1, product)
        products.removeAt(item)
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
                Toast.makeText(this, "Программа завершена", Toast.LENGTH_LONG).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GALLERY_REQUEST -> if (resultCode === RESULT_OK) {
                photoUri = data?.data

                binding.infoImageIV.setImageURI(photoUri)
            }
        }
    }
}