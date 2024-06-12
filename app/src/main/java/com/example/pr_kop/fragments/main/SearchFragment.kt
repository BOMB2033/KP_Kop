package com.example.pr_kop.fragments.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.pr_kop.R
import com.example.pr_kop.dataProduct.Product
import com.example.pr_kop.dataProduct.ProductAdapter
import com.example.pr_kop.dataProduct.ProductViewModel
import com.example.pr_kop.dataUser.UserViewModel
import com.example.pr_kop.databinding.FragmentSearchBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SearchFragment : Fragment() {
    private lateinit var binding:FragmentSearchBinding
    private val userViewModel: UserViewModel by activityViewModels()
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
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding)
        {
            adapter = ProductAdapter(userViewModel.data.value!!.productList,true,object :ProductAdapter.Listener{
                override fun addBasket(product: Product) {
                    userViewModel.addProduct(product.id)
                }
            })
            recyclerView.adapter = adapter
            productViewModel.data.observe(viewLifecycleOwner) { list ->
                adapter.submitList(list.filter { product ->
                    product.name.startsWith(textViewSearch.text)
                })
            }
            textViewSearch.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    // Код здесь будет выполняться каждый раз после изменения текста
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // Код здесь будет выполняться перед изменением текста
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    adapter.submitList(productViewModel.data.value?.filter { product ->
                        product.name.lowercase().startsWith(textViewSearch.text.toString().lowercase())
                    })
                }
            })

            buttonSearch.setOnClickListener {
                adapter.submitList(productViewModel.data.value)
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
         * @return A new instance of fragment SearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}