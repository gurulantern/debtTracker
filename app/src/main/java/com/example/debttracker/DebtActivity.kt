package com.example.debttracker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class DebtActivity : AppCompatActivity() {
    // Define variables to keep track of the total amount
    private var totalAmount = 0.0
    private var debtName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.debt_activity)

        // Retrieve data from intent
        debtName = intent.getStringExtra("debtName").toString()
        totalAmount = intent.getDoubleExtra("debtAmount", 0.0)

        // Set the TextView with debt amount and name
        val nameTextView = findViewById<TextView>(R.id.debt_name)
        val amountTextView = findViewById<TextView>(R.id.total_amount)
        nameTextView.text = debtName
        amountTextView.text = String.format("%.2f", totalAmount)

        showEditAmountDialog()

        val backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener {
            setActivityResult(totalAmount, debtName) // Pass the updated amount here
        }
    }

    private fun showEditAmountDialog() {
        val addBtn = findViewById<ImageButton>(R.id.add_amount)
        val subBtn = findViewById<ImageButton>(R.id.subtract_amount)

        // Create a dialog with an amount input layout
        fun createAmountDialog(positiveAction: (Double) -> Unit) {
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.amount_input, null)
            val editText = dialogLayout.findViewById<EditText>(R.id.edit_text_id)

            // Set an input filter to allow only numbers with up to two decimal places
            val decimalFilter = DecimalDigitsInputFilter(2)
            editText.filters = arrayOf(decimalFilter)

            builder.apply {
                setTitle(getString(R.string.enter_an_amount))
                setPositiveButton(getString(R.string.okay)) { dialog, which ->
                    // Handle positive button click
                    val enteredAmount = editText.text.toString().toDoubleOrNull() ?: 0.0
                    positiveAction(enteredAmount)
                }
                setNegativeButton(getString(R.string.cancel)) { dialog, which ->
                    Log.d("Amount", "Cancel button clicked")
                }
                setView(dialogLayout)
                show()
            }
        }

        // Set up click listeners for the add and subtract buttons
        addBtn.setOnClickListener {
            createAmountDialog { enteredAmount ->
                // Handle positive action (adding amount)
                totalAmount += enteredAmount
                updateAmountTextView()
            }
        }

        subBtn.setOnClickListener {
            createAmountDialog { enteredAmount ->
                // Handle positive action (subtracting amount)
                totalAmount -= enteredAmount
                updateAmountTextView()
            }
        }
    }

    private fun updateAmountTextView() {
        // Update the TextView with the formatted total amount
        val amountTextView = findViewById<TextView>(R.id.total_amount)
        amountTextView.text = String.format("%.2f", totalAmount)
    }

    private fun setActivityResult(updatedAmount: Double, debtName: String) {
        val resultIntent = Intent()
        resultIntent.putExtra("updatedAmount", updatedAmount)
        resultIntent.putExtra("debtName", debtName)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}

// Input filter to restrict input to numbers with specified decimal digits
class DecimalDigitsInputFilter(digitsAfterZero: Int) : InputFilter {
    private val pattern: String = if (digitsAfterZero > 0) {
        String.format("[0-9]+(\\.[0-9]{0,%d})?", digitsAfterZero)
    } else {
        "[0-9]+"
    }

    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        val newText = dest?.substring(0, dstart) + source?.substring(start, end) + dest?.substring(dend)
        return if (newText.matches(pattern.toRegex())) {
            null
        } else {
            ""
        }
    }
}
