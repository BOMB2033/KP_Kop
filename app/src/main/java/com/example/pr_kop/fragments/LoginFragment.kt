package com.example.pr_kop.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.pr_kop.R
import com.example.pr_kop.dataUser.UserViewModel
import com.example.pr_kop.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val userViewModel:UserViewModel by activityViewModels()
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
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            buttonLogin.setOnClickListener{
                FirebaseAuth.getInstance().signInWithEmailAndPassword(textViewEmail.text.toString(), textViewPassword.text.toString())
                    .addOnCompleteListener{ task ->
                        if (task.isSuccessful) {
                            /*UserRepository().saveUserList(
                                listOf(
                                    User(
                                        "feSFu8Kz69OIZOwUT99W0HyrL5R2",
                                        "Ирина",
                                        "Копылова",
                                        "q@mail.com",
                                        "ул.Пирожкова д.10",
                                        listOf(0,1)
                                    ),User(
                                        "feSFu8Kz69OIZOwUT99W0HgrL5R2",
                                        "sddas",
                                        "Коasdsadпылова",
                                        "q@asd.com",
                                        "ул.Пирожкова д.10",
                                        listOf(1,2)
                                    )
                                )
                            )*/

                            userViewModel.loadUser()
                            it.findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                        } else {
                            // Обработка ошибки
                        }
                    }
            }
            buttonRegistration.setOnClickListener{
                it.findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
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
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}