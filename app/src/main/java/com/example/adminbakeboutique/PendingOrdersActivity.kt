package com.example.adminbakeboutique

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.bakeboutique.CartItem
import com.google.firebase.database.*

class PendingOrdersActivity : AppCompatActivity() {

    private lateinit var pendingOrdersListView: ListView
    private lateinit var completedOrdersButton: Button
    private lateinit var database: DatabaseReference
    private lateinit var pendingOrdersAdapter: ArrayAdapter<String>
    private val pendingOrders = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pending_orders)


        pendingOrdersListView = findViewById(R.id.pending_orders_listview)
        completedOrdersButton = findViewById(R.id.completed_orders_button)

        database = FirebaseDatabase.getInstance().reference.child("pending_orders")


        pendingOrdersAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, pendingOrders)
        pendingOrdersListView.adapter = pendingOrdersAdapter


        fetchPendingOrders()

        pendingOrdersListView.setOnItemClickListener { _, _, position, _ ->
            val orderToComplete = pendingOrders[position]
            pendingOrders.removeAt(position)
            pendingOrdersAdapter.notifyDataSetChanged()

            saveCompletedOrder(orderToComplete)
        }

        completedOrdersButton.setOnClickListener {
            startActivity(Intent(this, CompletedOrdersActivity::class.java))
        }
    }

    private fun fetchPendingOrders() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                pendingOrders.clear()
                for (orderSnapshot in snapshot.children) {
                    val orderDetails = orderSnapshot.getValue(OrderDetails::class.java)
                    orderDetails?.let {
                        pendingOrders.add("Order for ${it.firstName} ${it.lastName} - ${it.totalBill} Rs")
                    }
                }
                pendingOrdersAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun saveCompletedOrder(order: String) {

        val database = FirebaseDatabase.getInstance().reference
        val completedOrderId = database.child("completed_orders").push().key
        if (completedOrderId != null) {
            database.child("completed_orders").child(completedOrderId).setValue(order)
        }
    }

    data class OrderDetails(
        val firstName: String = "",
        val lastName: String = "",
        val email: String = "",
        val phoneNumber: String = "",
        val totalBill: Int = 0,
        val dateTime: String = "",
        val items: List<CartItem> = emptyList()
    )
}
