package com.example.pr_kop.dataUser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class UserRepository {
    private var user = User()
    private var data = MutableLiveData(user)
    private var databaseUserReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
    private var databaseProductReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("products")
    private var storageReference: StorageReference = FirebaseStorage.getInstance().getReference("user_images")

    fun getData() = data
    private fun sync(){
        data.value = user
        databaseUserReference.child(user.uid).setValue(user)
    }
    fun getLoveProduct() = user.productList
    fun setUser(user1:User){
        this.user = user1
        sync()
    }
    fun addProduct(id: Int) {
        user.productList = user.productList.plus(id)
        sync()
    }
    fun removeProduct(id: Int) {
        user.productList = user.productList.toMutableList().apply { remove(id)  }
        sync()
    }
    fun saveUserList(userList: List<User>) {
        userList.forEach {
            databaseUserReference.child(it.uid).setValue(it)
        }
    }

    fun loadUser(uid: String,onResult: (User) -> Unit) {
        val listener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.mapNotNull { it.getValue(User::class.java) }.forEach{
                    if (it.uid == uid)
                        onResult(it)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        }
        databaseUserReference.addValueEventListener(listener)

    }




}

class UserViewModel : ViewModel() {
    private val repository: UserRepository = UserRepository()
    val data: LiveData<User> = repository.getData()
    fun setUser(user: User) = repository.setUser(user)
    fun getListLoveProduct() = repository.getLoveProduct()
    fun changePassword(){

    }
    fun loadUser(){
        FirebaseAuth.getInstance().currentUser?.let {
            repository.loadUser(it.uid){user ->
                repository.setUser(user)
            }
        }
    }

    fun addProduct(id: Int)  = repository.addProduct(id)
    fun removeProduct(id: Int)  = repository.removeProduct(id)
}


data class User(
    val uid:String = "",
    val name: String = "",
    val family: String = "",
    val email:String = "",
    val address:String = "",
    var productList: List<Int> = emptyList(),
    val password:String = "",
)