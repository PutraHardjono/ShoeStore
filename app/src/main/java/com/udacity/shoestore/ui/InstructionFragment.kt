package com.udacity.shoestore.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.udacity.shoestore.databinding.FragmentInstructionBinding

/**
 * An Instuction Fragment class
 */
class InstructionFragment : Fragment() {

    private var _binding: FragmentInstructionBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInstructionBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding!!.buttonStart.setOnClickListener {
            findNavController().navigate(InstructionFragmentDirections.actionInstructionFragmentToShoesFragment())
        }
    }
}