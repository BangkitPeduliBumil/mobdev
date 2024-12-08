package com.bangkit.pedulibumil.util

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.bangkit.pedulibumil.R

class CustomEditText(context: Context, attrs: AttributeSet) : AppCompatEditText(context, attrs) {

    init {
        // Tambahkan TextWatcher untuk validasi input real-time
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validatePassword(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun validatePassword(password: String) {
        if (password.length < 8) {
            this.error = context.getString(R.string.password_too_short)
        } else {
            this.error = null
        }
    }
}
