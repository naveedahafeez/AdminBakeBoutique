package com.example.adminbakeboutique

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

import com.google.firebase.database.*

class CompletedOrdersActivity : AppCompatActivity() {

    private lateinit var completedOrdersListView: ListView
    private lateinit var database: DatabaseReference
    private lateinit var adapter: ArrayAdapter<String>
    private val completedOrders = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed_orders)


        completedOrdersListView = findViewById(R.id.completed_orders_listview)

        database = FirebaseDatabase.getInstance().reference.child("completed_orders")

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, completedOrders)
        completedOrdersListView.adapter = adapter

        fetchCompletedOrders()
    }

    private fun fetchCompletedOrders() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                completedOrders.clear()
                for (orderSnapshot in snapshot.children) {
                    val order = orderSnapshot.getValue(String::class.java)
                    if (order != null) {
                        completedOrders.add(order)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors
            }
        })
    }
}
