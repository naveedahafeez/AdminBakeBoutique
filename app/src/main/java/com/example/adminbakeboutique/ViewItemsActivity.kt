package com.example.adminbakeboutique

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminbakeboutique.databinding.ActivityViewItemsBinding
import com.google.firebase.database.*
import com.models.Item

class ViewItemsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewItemsBinding
    private lateinit var database: DatabaseReference
    private lateinit var itemList: MutableList<Item>
    private lateinit var adapter: ItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        database = FirebaseDatabase.getInstance().getReference("BakeBoutique/items")
        itemList = mutableListOf()
        adapter = ItemsAdapter(itemList)

        binding.itemsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.itemsRecyclerView.adapter = adapter

        fetchItemsFromDatabase()
    }

    private fun fetchItemsFromDatabase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                itemList.clear()
                for (itemSnapshot in snapshot.children) {
                    val item = itemSnapshot.getValue(Item::class.java)
                    item?.let { itemList.add(it) }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}
