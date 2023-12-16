package com.example.debttracker

object DebtTracker {

    private val debtMap = mutableMapOf<String, Double>()

    fun addDebt(debtName: String, debtAmount: Double) {
        debtMap[debtName] = debtAmount
    }

    fun getDebtAmount(debtName: String): Double {
        return debtMap[debtName] ?: 0.0
    }

    fun addToDebt(debtName: String, amountToAdd: Double) {
        val currentAmount = getDebtAmount(debtName)
        debtMap[debtName] = currentAmount + amountToAdd
    }

    fun subtractFromDebt(debtName: String, amountToSubtract: Double) {
        val currentAmount = getDebtAmount(debtName)
        debtMap[debtName] = currentAmount - amountToSubtract
    }

    fun removeDebt(debtName: String) {
        debtMap.remove(debtName)
    }

    fun getDebts(): Map<String, Double> {
        return debtMap.toMap()
    }

    fun updateDebt(debtName: String, newAmount: Double) {
        if (debtMap.containsKey(debtName)) {
            debtMap[debtName] = newAmount
        } else {
            // Log an error message if the debt with the specified name doesn't exist
            println("Error: Debt with name '$debtName' not found.")
        }
    }
}