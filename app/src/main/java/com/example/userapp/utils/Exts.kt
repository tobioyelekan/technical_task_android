package com.example.userapp.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.userapp.model.ErrorBody
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.Exception
import java.lang.reflect.Type

inline fun <reified T> typeToken(): Type = object : TypeToken<T>() {}.type

inline fun <reified T> String?.toObject(): T {
    val type = typeToken<T>()
    return Gson().fromJson(this, type)
}

fun String?.parseError(): String {
    return try {
        val body = this.toObject<ErrorBody>().data
        return "${body[0].field} ${body[0].message}"
    } catch (e: Exception) {
        e.printStackTrace()
        "Something went wrong"
    }
}

fun Context.show(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun hideKeyboard(fragment: androidx.fragment.app.Fragment) {
    try {
        val imm =
            fragment.view?.context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(fragment.view?.rootView?.windowToken, 0)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}