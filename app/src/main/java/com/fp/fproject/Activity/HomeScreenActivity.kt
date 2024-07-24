package com.fp.fproject.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fp.fproject.R
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fp.fproject.Activity.adapter.typAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import typ

class HomeScreenActivity : AppCompatActivity(), typAdapter.RecyclerViewClickListener {
    private lateinit var horizontalScrollView: HorizontalScrollView
    private lateinit var recyclerOne: RecyclerView
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var view_all: Button
    private val crateItems: MutableList<typ> = mutableListOf()
    private lateinit var typList: List<typ>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_F_project)
        setContentView(R.layout.activity_home_screen)



        horizontalScrollView = findViewById(R.id.hor1)
        recyclerOne = findViewById(R.id.hor2)
        bottomNavigationView = findViewById(R.id.bot_nav)
        view_all = findViewById(R.id.view_all)

        view_all.setOnClickListener {
            startActivity(Intent(this@HomeScreenActivity, AllRest::class.java))
        }


        val burgerButton = findViewById<androidx.cardview.widget.CardView>(R.id.Burger)
        val donatButton = findViewById<androidx.cardview.widget.CardView>(R.id.Donat)
        val pizzaButton = findViewById<androidx.cardview.widget.CardView>(R.id.Pizza)
        val mexicanButton = findViewById<androidx.cardview.widget.CardView>(R.id.Mexican)
        val asianButton = findViewById<androidx.cardview.widget.CardView>(R.id.Asian)
        val sandwichesButton = findViewById<androidx.cardview.widget.CardView>(R.id.Sandwiches)

        burgerButton.setOnClickListener {
            startActivity(Intent(this@HomeScreenActivity, BurgerActivity::class.java))
        }
        donatButton.setOnClickListener {
            startActivity(Intent(this@HomeScreenActivity, DonatActivity::class.java))
        }
        pizzaButton.setOnClickListener {
            startActivity(Intent(this@HomeScreenActivity, PizzaActivity::class.java))
        }
        mexicanButton.setOnClickListener {
            startActivity(Intent(this@HomeScreenActivity, MexicanActivity::class.java))
        }
        asianButton.setOnClickListener {
            startActivity(Intent(this@HomeScreenActivity, AsianActivity::class.java))
        }
        sandwichesButton.setOnClickListener {
            startActivity(Intent(this@HomeScreenActivity, SandwichesActivity::class.java))
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            val intent: Intent = when (item.itemId) {
                R.id.btn_two -> Intent(this@HomeScreenActivity, MapActivity::class.java)
                R.id.btn_three -> Intent(this@HomeScreenActivity, CrateActivity::class.java)
                R.id.btn_four -> Intent(this@HomeScreenActivity, FavActivity::class.java)
                R.id.btn_five -> Intent(this@HomeScreenActivity, NotifyActivity::class.java)
                else -> Intent(this@HomeScreenActivity, HomeScreenActivity::class.java)
            }
            startActivity(intent)
            true
        }





        // Initialize the RecyclerView
        recyclerOne.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        val typList = getTypList()
        val typAdapter = typAdapter(this, typList, this, mutableListOf())
        recyclerOne.adapter = typAdapter






        // When user navigates to the CrateActivity
        fun navigateToCrateActivity(view: View) {
            val intent = Intent(this, CrateActivity::class.java)
            intent.putParcelableArrayListExtra("crateItems", ArrayList(crateItems))
            startActivity(intent)
        }
        // Initialize the Firebase database reference
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("items")

        // Assuming typList contains your item data
        for (item in typList) {
            // Push each item to the database
            val newItemReference = reference.push()
            newItemReference.setValue(item)
                .addOnSuccessListener {
                    // Data successfully written to the database
                    Log.d("TAG", "Item added to the database: ${newItemReference.key}")
                }
                .addOnFailureListener { e ->
                    // Failed to write data to the database
                    Log.w("TAG", "Error adding item to database", e)
                }
        }

    }
    // Function to save selected items to Firebase
    private fun saveItemsToFirebase(item: typ) {
        // Initialize the Firebase database reference
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("crateItems")

        // Push the item to the database
        val newItemReference = reference.push()
        newItemReference.setValue(item)
            .addOnSuccessListener {
                Log.d("HomeScreenActivity", "Item added to the database: ${newItemReference.key}")
            }
            .addOnFailureListener { e ->
                Log.w("HomeScreenActivity", "Error adding item to database", e)
            }
    }




    private fun getTypList(): List<typ> {
        val typList: MutableList<typ> = ArrayList()
        typList.add(
            typ(
                "Big Mac Meal",
                "4.5",
                "(25+)",
                "Free delivery",
                "10-15 mins",
                "Big Mac",
                "Fries",
                "Soft Drink",
                R.drawable.mc_feu,
                R.drawable.favourite_icon,
                R.drawable.verf_mc,
                "$9.50"
            )
        )
        typList.add(
            typ(
                "Zinger Combo",
                "4.6",
                "(99+)",
                "$1 delivery",
                "5-15 mins",
                "Zinger Chicken",
                "Fries",
                "Coke",
                R.drawable.kfc_ic1,
                R.drawable.favourite_icon,
                R.drawable.verf_mc,
                "$5.50"
            )
        )
        typList.add(
            typ(
                "Mighty Bucket for One",
                "4.6",
                "(99+)",
                "$1 delivery",
                "5-15 mins",
                "Chicken",
                "Coleslaw",
                "Biscuit",
                R.drawable.kfc_ic1,
                R.drawable.favourite_icon,
                R.drawable.verf_mc,
                "$15"
            )
        )
        typList.add(
            typ(
                "Quarter Pounder with Cheese Meal",
                "4.6",
                "(99+)",
                "$1 delivery",
                "5-15 mins",
                "Quarter Pounder with Cheese",
                "Fries",
                "Soft Drink",
                R.drawable.kfc_ic1,
                R.drawable.favourite_icon,
                R.drawable.verf_mc,
                "$20"
            )
        )
        typList.add(
            typ(
                "Spicy McChicken Meal",
                "4.6",
                "(99+)",
                "$1 delivery",
                "5-15 mins",
                "Spicy McChicken",
                "Fries",
                "Coke",
                R.drawable.stb_img,
                R.drawable.favourite_icon,
                R.drawable.verf_mc,
                "$10.99"
            )
        )
        typList.add(
            typ(
                "Double Down Combo",
                "4.7",
                "(99+)",
                "$2 delivery",
                "10-15 mins",
                "Double Down",
                "Fries",
                "Soft Drink",
                R.drawable.mc_feu,
                R.drawable.favourite_icon,
                R.drawable.verf_mc,
                "$5.50"
            )
        )
        typList.add(
            typ(
                "McChicken Meal",
                "4.6",
                "(99+)",
                "$1 delivery",
                "5-15 mins",
                "McChicken",
                "Fries",
                "Coke",
                R.drawable.kfc_ic1,
                R.drawable.favourite_icon,
                R.drawable.verf_mc,
                "$25.50"
            )
        )
        typList.add(
            typ(
                "Chicken McNuggets Meal",
                "4.6",
                "(99+)",
                "$1 delivery",
                "5-15 mins",
                "Chicken McNuggets",
                "Fries",
                "Soft Drink",
                R.drawable.stb_img,
                R.drawable.favourite_icon,
                R.drawable.verf_mc,
                "$17.50"
            )
        )
        typList.add(
            typ(
                "Filet-O-Fish Meal",
                "4.6",
                "(99+)",
                "$1 delivery",
                "5-15 mins",
                "Filet-O-Fish",
                "Fries",
                "Coke",
                R.drawable.stb_img,
                R.drawable.favourite_icon,
                R.drawable.verf_mc,
                "$9.50"
            )
        )
        typList.add(
            typ(
                "Zinger Stacker Meal",
                "4.6",
                "(99+)",
                "$1 delivery",
                "5-15 mins",
                "Zinger Stacker",
                "Fries",
                "Soft Drink",
                R.drawable.kfc_ic1,
                R.drawable.favourite_icon,
                R.drawable.verf_mc,
                "$2.50"
            )
        )
        return typList
    }


    override fun onClick(v: View?, position: Int) {
        // Get the selected item
        val selectedItem = typList[position]

        // Create an intent to start the DetailsActivity
        val intent = Intent(this@HomeScreenActivity, DetailsActivity::class.java)

        // Pass the selected item to the DetailsActivity
        intent.putExtra("selectedItem", selectedItem)

        // Start the DetailsActivity
        startActivity(intent)
    }

    override fun onAddToCrateClicked(position: Int) {
        // Ensure typList is initialized before accessing it
        if (!::typList.isInitialized) {
            // Handle typList not being initialized
            Log.e("HomeScreenActivity", "typList is not initialized")
            return
        }

        // Add the selected item to the crate list
        val selectedItem = typList[position]
        crateItems.add(selectedItem)
        // Serialize selectedItems to JSON string
        val gson = Gson()
        val selectedItemsJson = gson.toJson(crateItems)

        // Create the intent for CrateActivity
        val intent = Intent(this, CrateActivity::class.java)

        // Add JSON string as extra to Intent
        intent.putExtra("selectedItemsJson", selectedItemsJson)

        // Now, start the CrateActivity and pass the list of items
        intent.putParcelableArrayListExtra("crateItems", ArrayList(crateItems))
        startActivity(intent)

        // Log the size of the crateItems list
        Log.d("CrateActivity", "Size of crateItems list: ${crateItems.size}")
    }



}



