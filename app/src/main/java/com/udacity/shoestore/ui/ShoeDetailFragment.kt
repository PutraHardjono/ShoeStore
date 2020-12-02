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
import com.udacity.shoestore.databinding.FragmentShoeDetailBinding
import com.udacity.shoestore.util.State
import timber.log.Timber

/**
 * Shoe Detail
 */
class ShoeDetailFragment : Fragment() {

    private lateinit var _binding: FragmentShoeDetailBinding
    private val _vm by activityViewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _vm.cleanState()
        _vm.getShoe(ShoeDetailFragmentArgs.fromBundle(requireArguments()).name)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shoe_detail, container, false)

        // Observe state from validator
        _vm.state.observe(viewLifecycleOwner, { state ->
            Timber.i("State: $state")
            when (state) {
                State.SHOE_NAME_EMPTY -> {
                    _binding.textInputName.error = getString(R.string.error_shoe_name_empty)
                }
                State.SHOE_NAME_EXIST -> {
                    _binding.textInputName.error = getString(R.string.error_shoe_name_exist)
                }
                else -> {}
            }
        })

        _vm.eventSave.observe(viewLifecycleOwner, { isSaved ->
            isSaved?.let {
                Timber.i("isSaved: $isSaved")
                if (isSaved) {
                    findNavController().navigate(ShoeDetailFragmentDirections.actionShoeDetailFragmentToShoesFragment())
                    _vm.onSaveShoeDone()
                }
            }
        })

        _binding.buttonCancel.setOnClickListener {
            findNavController().popBackStack()
        }

        _binding.viewModel = _vm
        _binding.lifecycleOwner = viewLifecycleOwner
        _binding.executePendingBindings()
        return _binding.root
    }
}