package com.udacity.shoestore.ui

import android.content.res.Resources.getSystem
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.udacity.shoestore.MainViewModel
import com.udacity.shoestore.R
import com.udacity.shoestore.databinding.FragmentShoesBinding
import com.udacity.shoestore.models.Shoe
import timber.log.Timber

/**
 *
 */
class ShoesFragment : Fragment() {

    private val _vm by activityViewModels<MainViewModel>()
    private lateinit var _binding: FragmentShoesBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shoes, container, false)

        _vm.shoeList.observe(viewLifecycleOwner, { shoesList ->
            Timber.i("Shoe List Size: ${shoesList.size}")
            for (item in shoesList)
                addTextView(item)
        })

        _binding.fab.setOnClickListener {
            findNavController().navigate(ShoesFragmentDirections.actionShoesFragmentToShoeDetailFragment(""))
        }

        setHasOptionsMenu(true)
        return _binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.overflow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController()) ||
                super.onOptionsItemSelected(item)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun addTextView(shoe: Shoe) {
        val textView = TextView(requireContext())
        val param = LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT)
        param.setMargins(16.px,16.px, 16.px, 0.px)
        textView.layoutParams = param
        textView.text = shoe.name
        textView.setTextAppearance(R.style.TextAppearance_body2)
        textView.setOnClickListener {
            findNavController().navigate(ShoesFragmentDirections.actionShoesFragmentToShoeDetailFragment(shoe.name))
        }
        _binding.shoeList.addView(textView)
    }
}

val Int.px: Int get() = (this * getSystem().displayMetrics.density).toInt()