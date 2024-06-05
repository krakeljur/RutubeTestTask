package com.example.rutubetesttask.presentation.catalog

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.rutubetesttask.R
import com.example.rutubetesttask.databinding.FragmentCatalogBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CatalogFragment : Fragment(R.layout.fragment_catalog) {

    private val viewModel by viewModels<CatalogViewModel>()
    private lateinit var binding : FragmentCatalogBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCatalogBinding.bind(view)

        viewModel.refreshCities()
        setupObserve()
    }

    private fun setupObserve() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect {
                    if (it.isError) {

                    } else if (it.isLoading) {

                    } else {
                        Log.d("MYTAG", it.cities.joinToString("\n"))
                    }
                }
            }
        }
    }
}