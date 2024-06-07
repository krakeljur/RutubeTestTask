package com.example.rutubetesttask.presentation.catalog.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rutubetesttask.data.catalog.entity.CityDataEntity
import com.example.rutubetesttask.databinding.ItemCityBinding


class CityDiffCallBack(
    private val oldList: List<CityDataEntity>,
    private val newList: List<CityDataEntity>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]

}

interface ActionListener {
    fun selectCity(city: CityDataEntity)
}

class CityAdapter(
    private val actionListener: ActionListener
) : RecyclerView.Adapter<CityAdapter.CityViewHolder>(), View.OnClickListener {
    var cities: List<CityDataEntity> = emptyList()
        set(newValue) {
            val diffCallBack = CityDiffCallBack(field, newValue)
            val diffResult = DiffUtil.calculateDiff(diffCallBack)
            field = newValue
            diffResult.dispatchUpdatesTo(this)
        }

    class CityViewHolder(val binding: ItemCityBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCityBinding.inflate(inflater, parent, false)

        binding.nameCity.setOnClickListener(this)

        return CityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val currentCity = cities[position]
        val currentGroupName = currentCity.city[0].toString()
        val isGroupChanged =
            (position != 0 && cities[position - 1].city.first() != currentCity.city.first()) || position == 0


        with(holder.binding) {
            nameCity.tag = currentCity
            nameCity.text = currentCity.city
            nameGroup.text = currentGroupName
            nameGroup.visibility =
                if (isGroupChanged) View.VISIBLE else View.INVISIBLE
        }


    }

    override fun getItemCount(): Int = cities.size


    override fun onClick(v: View) {
        val city = v.tag as CityDataEntity

        actionListener.selectCity(city)
    }

}