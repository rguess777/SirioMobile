package com.example.sirioecommerceapp.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class EditButton(context: Context, attrs: AttributeSet, var isChecked: Boolean):AppCompatButton(context, attrs) {



    init {
        applyFont()
    }

    private fun applyFont() {


        val typeface: Typeface =
            Typeface.createFromAsset(context.assets, "Montserrat-Bold.ttf")
        setTypeface(typeface)
    }
}