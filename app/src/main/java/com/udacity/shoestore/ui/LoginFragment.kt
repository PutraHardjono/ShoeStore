package com.udacity.shoestore.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.udacity.shoestore.MainViewModel
import com.udacity.shoestore.R
import com.udacity.shoestore.databinding.FragmentLoginBinding
import com.udacity.shoestore.util.State

/**
 * A Login Fragment class
 */
class LoginFragment : Fragment() {

    private lateinit var _binding: FragmentLoginBinding
    private val _vm by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        // State to observe for every validation occurred
        _vm.state.observe(viewLifecycleOwner, { state ->
            _binding.textStatus.text = ""
            state?.let {
                when (state) {
                    State.EMAIL_FIELD_EMPTY ->  // Email is empty
                        _binding.textUserEmail.error = getString(R.string.error_email_field_empty)
                    State.EMAIL_INVALID ->  // Invalid email address
                        _binding.textUserEmail.error = getString(R.string.error_email_invalid)
                    State.PASSWORD_FIELD_EMPTY ->  // Password is empty
                        _binding.userPassword.error = getString(R.string.error_password_field_empty)
                    State.EMAIL_PASS ->
                        _binding.textUserEmail.error = null
                    State.PASSWORD_PASS ->
                        _binding.textUserPassword.error = null
                }
            }
        })

        _vm.eventCreate.observe(viewLifecycleOwner, { isCreated ->
            isCreated?.let {
                if (isCreated) {
//                    clearUI()
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToWelcomeFragment())
                    _vm.isCreateDone()
                }
                else
                    _binding.textStatus.text = getString(R.string.email_exist)
            }
        })

        _vm.eventLogin.observe(viewLifecycleOwner, { isLogin ->
            isLogin?.let {
                if (isLogin) {
//                    clearUI()
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToWelcomeFragment())
                    _vm.isLoginDone()
                }
                else
                    _binding.textStatus.text = getString(R.string.invalid_login)
            }
        })

        _binding.viewModel = _vm
        _binding.lifecycleOwner = viewLifecycleOwner
        _binding.executePendingBindings()

        // Inflate the layout for this fragment
        return _binding.root
    }

//    private fun clearUI() {
//        _binding.userPassword.error = null
//        _binding.textUserEmail.error = null
//        _binding.textUserPassword.error = null
//        _binding.userEmail.setText("")
//        _binding.userPassword.setText("")
//        _binding.textStatus.text = ""
//    }
}