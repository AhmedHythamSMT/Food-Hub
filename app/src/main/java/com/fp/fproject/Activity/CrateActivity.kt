package com.fp.fproject.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fp.fproject.Activity.adapter.typAdapter
import com.fp.fproject.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import typ

class CrateActivity : AppCompatActivity(), typAdapter.RecyclerViewClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var checkoutButton: ImageButton
    private lateinit var adapter: typAdapter
    private var crateItems: MutableList<typ> = mutableListOf()
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_F_project)
        setContentView(R.layout.activity_crate)

        recyclerView = findViewById(R.id.order_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        checkoutButton = findViewById(R.id.checkoutButton)

        database = FirebaseDatabase.getInstance().reference.child("crateItems")

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                crateItems.clear()
                for (itemSnapshot in dataSnapshot.children) {
                    val item = itemSnapshot.getValue(typ::class.java)
                    item?.let {
                        crateItems.add(item)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("CrateActivity", "Database Error: ${databaseError.message}")
            }
        }

        database.addValueEventListener(valueEventListener)

        val typList = getTypList()
        adapter = typAdapter(this, typList, this, crateItems)
        recyclerView.adapter = adapter

        checkoutButton.setOnClickListener {
            if (isLoggedIn() && isValidCrate(crateItems)) {
                saveItemsToFirebase(crateItems)
                Toast.makeText(this, "Order Has Confirmed", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    this,
                    "Please login and add valid items to the crate",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun saveItemsToFirebase(items: List<typ>) {
        for (item in items) {
            val newItemReference = database.push()
            newItemReference.setValue(item)
                .addOnSuccessListener {
                    Log.d("CrateActivity", "Item added to the database: ${newItemReference.key}")
                }
                .addOnFailureListener { e ->
                    Log.w("CrateActivity", "Error adding item to database", e)
                }
        }
    }

    private fun getTypList(): List<typ> {
        // Implement this method to provide typList
        return emptyList() // Placeholder implementation
    }

    private fun isLoggedIn(): Boolean {
        // Check if the user is logged in
        return false // Placeholder implementation
    }

    private fun isValidCrate(crateItems: List<typ>): Boolean {
        // Check if the crate has valid items for checkout
        return crateItems.isNotEmpty() // Placeholder implementation
    }

    override fun onClick(v: View?, position: Int) {
        // Handle click events if needed
    }

    override fun onAddToCrateClicked(position: Int) {
        // Add the selected item to the crate list
        val selectedItem = adapter.getItem(position)
        crateItems.add(selectedItem)
        adapter.notifyDataSetChanged()
    }
}