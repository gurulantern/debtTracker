package com.example.debttracker

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Debts {
    private const val PREFS_NAME = "DebtTrackerPrefs"
    private const val DEBT_MAP_KEY = "debtMap"
    private val debtMap = mutableMapOf<String, Double>()

    fun addDebt(debtName: String, debtAmount: Double) {
        debtMap[debtName] = debtAmount
        saveToSharedPreferences()
    }

    fun getDebtAmount(debtName: String): Double {
        return debtMap[debtName] ?: 0.0
    }

    fun addToDebt(debtName: String, amountToAdd: Double) {
        val currentAmount = getDebtAmount(debtName)
        debtMap[debtName] = currentAmount + amountToAdd
        saveToSharedPreferences()
    }

    fun subtractFromDebt(debtName: String, amountToSubtract: Double) {
        val currentAmount = getDebtAmount(debtName)
        debtMap[debtName] = currentAmount - amountToSubtract
        saveToSharedPreferences()
    }

    fun removeDebt(debtName: String) {
        debtMap.remove(debtName)
        saveToSharedPreferences()
    }

    fun getDebts(): Map<String, Double> {
        return debtMap.toMap()
    }

    private fun saveToSharedPreferences() {
        val prefs = DebtTracker.instance.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(DEBT_MAP_KEY, Gson().toJson(debtMap)).apply()
    }

    fun loadFromSharedPreferences() {
        val prefs = DebtTracker.instance.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val serializedDebtMap = prefs.getString(DEBT_MAP_KEY, null)

        serializedDebtMap?.let {
            debtMap.clear()
            debtMap.putAll(Gson().fromJson(it, object : TypeToken<Map<String, Double>>() {}.type))
        }
    }
}