package com.example.debttracker

import android.text.InputFilter
import android.text.Spanned

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
