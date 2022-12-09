package ru.abyzbaev.weather_app.view.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.abyzbaev.weather_app.databinding.ContactsRecycleItemBinding
import ru.abyzbaev.weather_app.domain.Weather

class ContactsAdapter : RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>() {
    private var data: List<Weather> = arrayListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContactsAdapter.ContactsViewHolder {
        val binding = ContactsRecycleItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ContactsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactsAdapter.ContactsViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ContactsViewHolder(private val binding: ContactsRecycleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Weather) {
            binding.apply {
                contactName.text = data.city.name
            }
        }
    }
}