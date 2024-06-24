package com.example.adminbakeboutique

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminbakeboutique.databinding.ItemViewBinding
import com.google.firebase.database.FirebaseDatabase
import com.models.Item

class ItemsAdapter(private val items: MutableList<Item>) : RecyclerView.Adapter<ItemsAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class ItemViewHolder(private val binding: ItemViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            binding.itemName.text = item.name
            binding.itemPrice.text = item.price.toString()
            Glide.with(binding.itemImage.context).load(item.imageUrl).into(binding.itemImage)
            binding.tvQuantity.text = "1"

            binding.btnIncrease.setOnClickListener {
                val currentQuantity = binding.tvQuantity.text.toString().toInt()
                binding.tvQuantity.text = (currentQuantity + 1).toString()
            }

            binding.btnDecrease.setOnClickListener {
                val currentQuantity = binding.tvQuantity.text.toString().toInt()
                if (currentQuantity > 1) {
                    binding.tvQuantity.text = (currentQuantity - 1).toString()
                }
            }

            binding.btnDelete.setOnClickListener {
                val database = FirebaseDatabase.getInstance().reference.child("BakeBoutique/items")
                database.child(item.id).removeValue()
                    .addOnSuccessListener {
                        items.removeAt(adapterPosition)
                        notifyItemRemoved(adapterPosition)
                    }
                    .addOnFailureListener {

                    }
            }
        }
    }
}
