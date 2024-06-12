package com.example.pr_kop.dataProduct

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ProductRepository {
    private var listProduct: List<Product> = emptyList()
    private var data = MutableLiveData(listProduct)
    private var databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("products")
    private var storageReference: StorageReference = FirebaseStorage.getInstance().getReference("product_images")

    fun getAll() = data
    private fun sync(){
        data.value = listProduct
    }
    fun setAll(list: List<Product>){
        listProduct = list
        sync()
    }
    fun saveProduct(product: Product, imageUri: Uri) {
        val key = databaseReference.push().key ?: return
        storageReference.child(key).putFile(imageUri).addOnSuccessListener {
            storageReference.child(key).downloadUrl.addOnSuccessListener { uri ->
                val productWithImage = product.copy(imageUrl = uri.toString())
                databaseReference.child(key).setValue(productWithImage)
            }
        }
    }

    fun saveProductList(productList: List<Product>) {
        productList.forEach { product ->
            val key = databaseReference.push().key ?: return
            // Предполагаем, что у вас уже есть URL изображения в объекте продукта
            val productWithImage = product.copy(imageUrl = product.imageUrl)
            databaseReference.child(key).setValue(productWithImage)
        }
    }


    fun getProductList(onResult: (List<Product>) -> Unit) {
        val listener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val products = dataSnapshot.children.mapNotNull { it.getValue(Product::class.java) }
                onResult(products)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        }
        databaseReference.addValueEventListener(listener)
    }
}

class ProductViewModel : ViewModel() {
    private val repository: ProductRepository = ProductRepository()
    // LiveData, содержащая список продуктов
    val data: LiveData<List<Product>> = repository.getAll()
    fun loadProducts() {
        repository.getProductList { productsList ->
            repository.setAll(productsList)
        }
    }
}

data class Product(
    val id:Int = 0,
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
)