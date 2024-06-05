package com.example.rutubetesttask.presentation.catalog.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rutubetesttask.databinding.ItemCityBinding
import com.example.rutubetesttask.databinding.ItemLetterGroupBinding
import com.example.rutubetesttask.data.catalog.entity.CityDataEntity


class CityDiffCallBack(
    private val oldList: List<CityDataEntity>,
    private val newList: List<CityDataEntity>,
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]

}

class CityAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var cities = emptyList<CityDataEntity>()
        set(newValue) {
            val diffCallBack = CityDiffCallBack(field, newValue)
            val diffResult = DiffUtil.calculateDiff(diffCallBack)
            field = newValue
            diffResult.dispatchUpdatesTo(this)
        }

    class CityViewHolder(val binding: ItemCityBinding) : RecyclerView.ViewHolder(binding.root)
    class LetterGroupViewHolder(val binding: ItemLetterGroupBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            VIEW_TYPE_CITY -> {
                val binding = ItemCityBinding.inflate(inflater, parent, false)
                CityViewHolder(binding)
            }

            else -> {
                val binding = ItemLetterGroupBinding.inflate(inflater, parent, false)
                LetterGroupViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    companion object {
        const val VIEW_TYPE_LETTER = 0
        const val VIEW_TYPE_CITY = 1
    }
}