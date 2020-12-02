package com.udacity.shoestore.util

import androidx.databinding.InverseMethod

object DoubleConverter {
    @InverseMethod("stringToDouble")
    @JvmStatic fun doubleToString(value: Double): String {
        return value.toString()
    }

    @JvmStatic fun stringToDouble(value: String): Double {
        return if (value.isEmpty()) 0.0
        else value.toDouble()
    }
}