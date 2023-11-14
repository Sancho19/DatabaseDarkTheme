package com.st10083248.database1

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }

    class MainActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            // Get references to the radio buttons

            val distanceEditText = findViewById<EditText>(R.id.etxtMaxDistance)
            val btnSaveToFirestore = findViewById<Button>(R.id.btnSave)
            val light = findViewById<RadioButton>(R.id.rbLight)

            light.isChecked = true

            val switchDarkTheme = findViewById<RadioButton>(R.id.rbDark)
            switchDarkTheme.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }

            // Set a listener for the radio buttons
            val radioButtonListener = RadioGroup.OnCheckedChangeListener { _, checkedId ->
                // Handle the case when "Miles" is selected
                // Handle the case when "Kilometers" is selected
            }

            // Attach the listener to the radio group
            val radioGroup = findViewById<RadioGroup>(R.id.rgMetricSystem)
            radioGroup.setOnCheckedChangeListener(radioButtonListener)

            // Set a click listener for the "Save to Firestore" button
            btnSaveToFirestore.setOnClickListener {
                // Get the selected unit and entered distance
                val selectedUnit = when (radioGroup.checkedRadioButtonId) {
                    R.id.rbMiles -> "Miles"
                    R.id.rbKilometers -> "Kilometers"
                    else -> null
                }

                val enteredDistance = distanceEditText.text.toString()

                // Check if both unit and distance are selected
                if (selectedUnit != null && enteredDistance.isNotEmpty()) {
                    // Call the function to save to Firestore
                    addToFirestore(selectedUnit, enteredDistance)
                } else {
                    // Handle the case when either unit or distance is not selected
                    Toast.makeText(this, "Please select unit and enter distance", Toast.LENGTH_SHORT).show()
                }
            }
        }



        private fun addToFirestore(unit: String, distance: String) {
            // Firebase code
            val db = FirebaseFirestore.getInstance()

            // Create a data object
            val data = hashMapOf(
                "unit" to unit,
                "distance" to distance
            )

            // Add a new document with a generated ID
            db.collection("selected_units_and_distances")
                .add(data)
                .addOnSuccessListener { documentReference ->
                    Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
                    // Optionally, you can add code to handle the success case
                }
                .addOnFailureListener { e ->
                    Log.w("TAG", "Error adding document", e)
                    // Optionally, you can add code to handle the failure case
                }
        }
    }}