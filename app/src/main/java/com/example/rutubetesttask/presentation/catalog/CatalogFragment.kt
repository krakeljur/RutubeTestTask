package com.example.rutubetesttask.presentation.catalog

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rutubetesttask.R
import com.example.rutubetesttask.data.catalog.entities.CityDataEntity
import com.example.rutubetesttask.databinding.FragmentCatalogBinding
import com.example.rutubetesttask.presentation.catalog.adapters.ActionListener
import com.example.rutubetesttask.presentation.catalog.adapters.CityAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.abs

@AndroidEntryPoint
class CatalogFragment : Fragment(R.layout.fragment_catalog) {

    private val viewModel by viewModels<CatalogViewModel>()
    private lateinit var binding: FragmentCatalogBinding

    private val cityAdapter = CityAdapter(object : ActionListener {

        override fun selectCity(city: CityDataEntity) {
            val direction = CatalogFragmentDirections.actionCatalogFragmentToForecastFragment(
                city.city,
                city.latitude.toFloat(),
                city.longitude.toFloat()
            )
            findNavController().navigate(direction)
        }

    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCatalogBinding.bind(view)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = cityAdapter

        setupListeners()
        setupObserve()
    }

    private fun setupListeners() {
        binding.searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    viewModel.refreshCities(query ?: "")
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText.isNullOrBlank())
                        viewModel.refreshCities()

                    return true
                }

            })


        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (cityAdapter.cities.isEmpty())
                    binding.stickyNameGroup.text = ""
                else {

                    val firstVisibleView = recyclerView.getChildAt(0)
                    val secondVisibleView = recyclerView.getChildAt(1)

                    val firstRowIndex = firstVisibleView.findViewById<TextView>(R.id.nameGroup)
                    val secondRowIndex =
                        secondVisibleView.findViewById<TextView>(R.id.nameGroup)

                    val isHeader =
                        firstRowIndex.text.toString() != secondRowIndex.text.toString()

                    val visibleRange = recyclerView.childCount
                    val actual = recyclerView.getChildAdapterPosition(firstVisibleView)
                    val next = actual + 1
                    val last = actual + visibleRange

                    binding.stickyNameGroup.text = firstRowIndex.text
                    binding.stickyNameGroup.visibility = View.VISIBLE



                    if (dy > 0) {
                        if (next <= last) {
                            if (isHeader) {
                                binding.stickyNameGroup.visibility = View.INVISIBLE
                                firstRowIndex.visibility = TextView.VISIBLE
                                firstRowIndex.alpha =
                                    1 - (abs(firstVisibleView.y) / firstRowIndex.height)

                                secondRowIndex.visibility = TextView.VISIBLE
                            } else {
                                firstRowIndex.visibility = TextView.INVISIBLE
                                binding.stickyNameGroup.visibility = View.VISIBLE
                            }
                        }
                    } else {
                        if (next <= last) {
                            firstRowIndex.visibility = TextView.INVISIBLE

                            if (isHeader) {
                                binding.stickyNameGroup.visibility = View.INVISIBLE
                                firstRowIndex.visibility = TextView.VISIBLE
                                firstRowIndex.alpha =
                                    1 - (abs(firstVisibleView.y) / firstRowIndex.height)
                                secondRowIndex.visibility = TextView.VISIBLE
                            } else {
                                secondRowIndex.visibility = TextView.INVISIBLE
                            }
                        }
                    }

                    if (binding.stickyNameGroup.visibility == View.VISIBLE)
                        firstRowIndex.visibility = TextView.INVISIBLE
                    else if (firstRowIndex.alpha <= 0 )
                        binding.stickyNameGroup.visibility = View.VISIBLE

                }
            }
        })

    }


    private fun setupObserve() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.catalogStateFLow.collect {
                    if (it.isError) {
                        showError()
                    } else if (it.isLoading) {
                        showLoading()
                    } else {
                        showSuccess()
                        cityAdapter.cities = it.cities
                        binding.recyclerView.scrollToPosition(0)

                        if (it.cities.isNotEmpty()) {
                            binding.stickyNameGroup.text = it.cities[0].city[0].toString()
                            binding.stickyNameGroup.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    private fun showSuccess() {
        binding.containerView.showSuccess()
        binding.successGroup.visibility = View.VISIBLE
    }

    private fun showLoading() {
        binding.containerView.showPending()
        binding.successGroup.visibility = View.GONE
    }

    private fun showError() {
        binding.containerView.showError {
            viewModel.refreshCities()
        }
        binding.successGroup.visibility = View.GONE
    }

}