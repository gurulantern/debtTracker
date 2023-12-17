package com.example.debttracker

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    private val layoutDebts: LinearLayout by lazy {
        findViewById<LinearLayout>(R.id.layoutDebts)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Debts.loadFromSharedPreferences()

        val btnAddDebt: Button = findViewById(R.id.btnAddDebt)

        btnAddDebt.setOnClickListener {
            showAddDebtDialog()
        }

        // Update the UI based on the debts in DebtTracker
        updateUI(layoutDebts)
    }

    override fun onResume() {
        super.onResume()

        // Update the UI again when the activity resumes
        updateUI(layoutDebts)
    }

    private fun updateUI(layoutDebts: LinearLayout) {
        layoutDebts.removeAllViews()

        // Iterate through debts in DebtTracker and add UI elements
        for ((debtName, debtAmount) in Debts.getDebts()) {
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
                startActivity(intent)
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
                        Debts.removeDebt(debtName)
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

    private fun showAddDebtDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_add_debt, null)
        val etDebtName = dialogLayout.findViewById<EditText>(R.id.etDebtName)
        val etDebtAmount = dialogLayout.findViewById<EditText>(R.id.etDebtAmount)

        // Set an input filter to allow only numbers with up to two decimal places
        val decimalFilter = DecimalDigitsInputFilter(2)
        etDebtAmount.filters = arrayOf(decimalFilter)

        builder.apply {
            setTitle(getString(R.string.add_new_debt))
            setPositiveButton(getString(R.string.save)) { dialog, which ->
                // Handle positive button click
                val debtName = etDebtName.text.toString()
                val debtAmountStr = etDebtAmount.text.toString()

                if (debtName.isNotEmpty() && debtAmountStr.isNotEmpty()) {
                    val debtAmount = debtAmountStr.toDouble()
                    Debts.addDebt(debtName, debtAmount)
                    updateUI(layoutDebts)
                } else {
                    // Show an error message or handle invalid input
                    // For example, you can set an error message on the EditText
                    etDebtAmount.error = getString(R.string.enter_a_valid_amount)
                }
            }
            setNegativeButton(getString(R.string.cancel)) { dialog, which ->
                Log.d("Amount", "Cancel button clicked")
            }
            setView(dialogLayout)
            show()
        }
    }
}
