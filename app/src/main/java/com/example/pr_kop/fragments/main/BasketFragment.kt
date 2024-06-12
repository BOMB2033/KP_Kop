package com.example.pr_kop.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.pr_kop.dataUser.UserViewModel
import com.example.pr_kop.databinding.FragmentBasketBinding
import com.example.pr_kop.dataProduct.Product
import com.example.pr_kop.dataProduct.ProductAdapter
import com.example.pr_kop.dataProduct.ProductViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BasketFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BasketFragment : Fragment() {
    private lateinit var binding: FragmentBasketBinding
    private val userViewModel:UserViewModel by activityViewModels()
    private val productViewModel: ProductViewModel by activityViewModels()
    private lateinit var adapter: ProductAdapter
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBasketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            buttonSearch.setOnClickListener {
                adapter.updateData()

            }

            adapter = ProductAdapter(userViewModel.data.value!!.productList,false,object : ProductAdapter.Listener{
                override fun addBasket(product: Product) {
                    userViewModel.removeProduct(product.id)
                    adapter.submitList(productViewModel.data.value?.filter {
                        for ( forItem in userViewModel.data.value?.productList!!)
                            if ((forItem == it.id))
                                return@filter true
                        return@filter false
                    })
                    adapter.updateData()

                }
            })
            recyclerView.adapter = adapter
            productViewModel.data.observe(viewLifecycleOwner){list ->
                adapter.submitList(list.filter { product ->
                    for ( forItem in userViewModel.data.value?.productList!!)
                        if ((forItem == product.id))
                            return@filter true
                    return@filter false
                })
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BasketFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BasketFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}