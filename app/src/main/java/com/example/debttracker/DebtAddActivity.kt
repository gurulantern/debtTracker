package com.example.debttracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.ParseException


class DebtAddActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debt_add)

        val btnSave: Button = findViewById(R.id.btnSave)
        val etDebtName: EditText = findViewById(R.id.etDebtName)
        val etDebtAmount: EditText = findViewById(R.id.etDebtAmount)

        btnSave.setOnClickListener {
            // Get debt name and amount from EditText
            val debtName = etDebtName.text.toString()
            val debtAmountStr = etDebtAmount.text.toString()

            if (debtName.isNotEmpty() && isValidAmount(debtAmountStr)) {
                // Format the debt amount with exactly two decimal places
                val formattedAmount = formatAmount(debtAmountStr)

                // Convert formatted debt amount to double
                val debtAmount = formattedAmount.toDouble()

                // Add the new debt to DebtTracker
                DebtTracker.addDebt(debtName, debtAmount)

                // Update the UI in MainActivity
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            } else {
                // Show an error message or handle invalid input
                // For example, you can set an error message on the EditText
                etDebtAmount.error = getString(R.string.enter_a_valid_amount)
            }
        }
    }

    private fun isValidAmount(amount: String): Boolean {
        try {
            // Use DecimalFormat to validate the amount with exactly two decimal places
            val decimalFormat = DecimalFormat("#,##0.00")
            decimalFormat.isParseBigDecimal = true
            val parsedAmount = decimalFormat.parse(amount) as? BigDecimal

            // Ensure the input has exactly two decimal places and only one decimal point
            return parsedAmount != null && amount.matches("\\d*\\.\\d{2}".toRegex()) && !amount.endsWith(".")
        } catch (e: ParseException) {
            return false
        }
    }

    private fun formatAmount(amount: String): String {
        // Use DecimalFormat to format the amount with exactly two decimal places
        val decimalFormat = DecimalFormat("#,##0.00")
        return decimalFormat.format(amount.toDouble())
    }
}