package com.example.pr_kop.dataProduct

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pr_kop.dataUser.UserViewModel
import com.example.pr_kop.databinding.ProductItemBinding

class ProductDiffCallback : DiffUtil.ItemCallback<Product>(){
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id==newItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return  oldItem == newItem
    }

}
class ProductViewHolder(private val binding: ProductItemBinding)
    : RecyclerView.ViewHolder(binding.root) {
    fun bind(product: Product,isHome:Boolean,listCount:List<Int>, listener: ProductAdapter.Listener) {
        binding.apply {
            if (isHome){
                price.text = "${product.price}р"
                sum.text = ""
                basket.text = "В корзину"
            }else{
                var count = 0
                listCount.forEach {
                    if (it == product.id)
                        count++
                }

                price.text = "${product.price*count}р"
                sum.text = "x$count  ${product.price}р"
                basket.text = "Удалить"
            }

            name.text = product.name
            description.text = product.description
            basket.setOnClickListener{
                listener.addBasket(product)
            }
        }
    }
}

class ProductAdapter(
    private val count:List<Int>,
    private val isHome:Boolean,
    private val listener: Listener,
): ListAdapter<Product, ProductViewHolder>(ProductDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ProductViewHolder, position:Int){
        val product = getItem(position)
        holder.bind(product, isHome, count, listener)
    }
    fun updateData() {
        notifyDataSetChanged()
    }

    interface Listener{
        fun addBasket(product: Product)
    }
}