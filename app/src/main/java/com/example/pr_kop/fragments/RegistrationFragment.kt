package com.example.pr_kop.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.pr_kop.R
import com.example.pr_kop.dataProduct.ProductRepository
import com.example.pr_kop.dataProduct.ProductViewModel
import com.example.pr_kop.dataUser.User
import com.example.pr_kop.dataUser.UserRepository
import com.example.pr_kop.dataUser.UserViewModel
import com.example.pr_kop.databinding.FragmentRegistrationBinding
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegistrationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegistrationFragment : Fragment() {
    private lateinit var binding: FragmentRegistrationBinding
    private val userViewModel: UserViewModel by activityViewModels()
    private val productViewModel: ProductViewModel by activityViewModels()
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
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding)
        {
            buttonRegistration.setOnClickListener{
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(textViewEmail.text.toString(), textViewPassword.text.toString())
                    .addOnCompleteListener{ task ->
                        if (task.isSuccessful) {
                            UserRepository().saveUserList(listOf(
                                User(
                                    FirebaseAuth.getInstance().currentUser!!.uid,
                                    textViewName.text.toString(),
                                    textViewFamaly.text.toString(),
                                    textViewEmail.text.toString(),
                                    textViewAddress.text.toString(),
                                    emptyList(),
                                    textViewPassword.text.toString(),

                                )
                            ))
                            it.findNavController().popBackStack()
                        } else {
                            // Обработка ошибки
                        }
                    }
            }
            buttonLogin.setOnClickListener{
                it.findNavController().popBackStack()
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
         * @return A new instance of fragment RegistrationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegistrationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}