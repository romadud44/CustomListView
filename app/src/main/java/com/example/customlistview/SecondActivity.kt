package com.example.customlistview

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.customlistview.databinding.ActivitySecondBinding


@Suppress("DEPRECATED_IDENTITY_EQUALS", "DEPRECATION")
class SecondActivity : AppCompatActivity(), Updatable {
    private var check = true
    private var product: Product? = null
    private val GALLERY_REQUEST = 777
    private var photoUri: Uri? = null
    private var products: MutableList<Product> = mutableListOf()
    private var listAdapter : ListAdapter? = null
    private var item: Int? = null
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

            listAdapter = ListAdapter(this, products)
            binding.listViewLV.adapter = listAdapter
            listAdapter?.notifyDataSetChanged()
            clearEditFields()
        }
        binding.listViewLV.onItemClickListener =
            AdapterView.OnItemClickListener{ _, _, position, _ ->
                product = listAdapter?.getItem(position)
                item = position
                update(product as Product)

            }

    }

    override fun onResume() {
        super.onResume()
        check = intent.extras?.getBoolean("newCheck") ?: true
        if (!check) {
            products = intent.getSerializableExtra("list") as MutableList<Product>
            listAdapter = ListAdapter(this, products)
            check = true
        }
        binding.listViewLV.adapter = listAdapter
    }

    private fun createProduct() {
        val productName = binding.nameET.text.toString()
        val productPrice = binding.priceET.text.toString()
        val productInfo = binding.infoET.text.toString()
        val productImage = photoUri.toString()
        val product = Product(productName, productPrice, productInfo, productImage)
        products.add(product)
        clearEditFields()
        photoUri = null
    }

    private fun clearEditFields() {
        binding.nameET.text.clear()
        binding.priceET.text.clear()
        binding.infoET.text.clear()
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

                binding.imageViewIV.setImageURI(photoUri)
            }
        }
    }

    override fun update(product: Product) {
        val intent = Intent(this, InfoActivity::class.java)
        intent.putExtra("product", product)
        intent.putExtra("products", this.products as ArrayList<Product>)
        intent.putExtra("position", item)
        intent.putExtra("check", check)
        startActivity(intent)
    }
}