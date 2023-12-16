package com.example.debttracker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : ComponentActivity() {

    private val startDebtActivityForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val updatedAmount = data?.getDoubleExtra("updatedAmount", 0.0) ?: 0.0
                val debtName = data?.getStringExtra("debtName") ?: ""

                DebtTracker.updateDebt(debtName, updatedAmount)
                updateUI(layoutDebts)
            }
        }

    private val layoutDebts: LinearLayout by lazy {
        findViewById<LinearLayout>(R.id.layoutDebts)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnAddDebt: Button = findViewById(R.id.btnAddDebt)

        btnAddDebt.setOnClickListener {
            // Start the DebtAddActivity when the "Add New Debt" button is clicked
            val intent = Intent(this, DebtAddActivity::class.java)
            startActivity(intent)

            // Update the UI based on the debts in DebtTracker
            updateUI(layoutDebts)
        }

        // Update the UI based on the debts in DebtTracker
        updateUI(layoutDebts)
    }

    private fun updateUI(layoutDebts: LinearLayout) {
        layoutDebts.removeAllViews()

        // Iterate through debts in DebtTracker and add UI elements
        for ((debtName, debtAmount) in DebtTracker.getDebts()) {
            // Add a layout to contain each debt's views
            val debtLayout = LinearLayout(this)
            debtLayout.orientation = LinearLayout.HORIZONTAL
            debtLayout.gravity = android.view.Gravity.CENTER // Center the views horizontally

            // Display debt details in a Button with two decimal places
            val formattedAmount = String.format("%.2f", debtAmount)
            val debtButton = Button(this)
            debtButton.text = "$debtName: $formattedAmount"
            debtButton.setOnClickListener {
                // Start DebtActivity when the button is clicked
                val intent = Intent(this, DebtActivity::class.java)
                intent.putExtra("debtName", debtName)
                intent.putExtra("debtAmount", debtAmount)
                startDebtActivityForResult.launch(intent)
            }

            // Add a "Remove" button dynamically
            val removeButton = Button(this)
            removeButton.text = "Remove"
            removeButton.setOnClickListener {
                // Remove the debt from DebtTracker and update the UI
                val builder = androidx.appcompat.app.AlertDialog.Builder(this)


                with(builder) {
                    setTitle(getString(R.string.confirm_removal))
                    setMessage(getString(R.string.confirm_removal_msg))
                    setPositiveButton(getString(R.string.yeah_bro)){ dialog, which ->
                        DebtTracker.removeDebt(debtName)
                        updateUI(layoutDebts)
                    }
                    setNegativeButton(getString(R.string.nah)){ dialog, which ->
                        Log.d("Amount", "Cancel button clicked")
                    }
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show()
                }
                updateUI(layoutDebts)
            }

            // Add views to the debt layout
            debtLayout.addView(debtButton)
            debtLayout.addView(removeButton)

            // Add the layout to the main layout
            layoutDebts.addView(debtLayout)
        }
    }
}
