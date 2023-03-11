package com.example.numberconverterapp

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {


    private lateinit var editTextDecimal: EditText
    private lateinit var editTextBinary: EditText
    private lateinit var editTextOctal: EditText
    private lateinit var editTextHex: EditText
    private lateinit var buttonClear: Button
    private var focusedViewId:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        addCallBacks()

    }

    private fun initViews() {
        editTextDecimal = findViewById(R.id.edit_text_decimal)
        editTextBinary = findViewById(R.id.edit_text_binary)
        editTextOctal = findViewById(R.id.edit_text_octal)
        editTextHex = findViewById(R.id.edit_text_hex)
        buttonClear = findViewById(R.id.button_clear)
    }

    private fun addCallBacks() {

        val textWatcher = object: TextWatcher{

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val number = p0.toString().trim()
                if (number.isNotEmpty()){
                    convertNumberBases(focusedViewId,number)
                }
                else {
                    clearEditTextsWhenNoInput(focusedViewId)
                    }
                }


            override fun afterTextChanged(p0: Editable?) {

            }

        }

        val views = listOf(editTextDecimal, editTextBinary, editTextOctal, editTextHex)
        views.forEach {
            it.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    focusedViewId = view.id
                    it.addTextChangedListener(textWatcher)
                    onFocusedViewDrawable(view)
                } else {
                    it.removeTextChangedListener(textWatcher)
                    onRemoveFocusedViewDrawable(view)
                }
            }
        }

        buttonClear.setOnClickListener {
            clearEditTexts(editTextDecimal,editTextBinary,editTextOctal,editTextHex)
        }

    }

    private fun clearEditTexts(vararg editTexts: EditText) {
        for (editText in editTexts) {
            editText.setText("")
        }
    }

    private fun clearEditTextsWhenNoInput(viewId: Int) {
        when (viewId) {
            R.id.edit_text_decimal -> clearEditTexts(editTextBinary, editTextOctal, editTextHex)
            R.id.edit_text_binary -> clearEditTexts(editTextDecimal, editTextOctal, editTextHex)
            R.id.edit_text_octal -> clearEditTexts(editTextDecimal, editTextBinary, editTextHex)
            R.id.edit_text_hex -> clearEditTexts(editTextDecimal, editTextBinary, editTextOctal)
        }
    }

    private fun convertNumberBases(viewId: Int, number: String) {
        try {
            val value = when (viewId) {
                R.id.edit_text_decimal -> number.toInt()
                R.id.edit_text_binary -> number.toInt(2)
                R.id.edit_text_octal -> number.toInt(8)
                else -> number.toInt(16)
            }

            editTextDecimal.takeIf { it.id != viewId }?.setText(value.toString())
            editTextBinary.takeIf { it.id != viewId }?.setText(Integer.toBinaryString(value))
            editTextOctal.takeIf { it.id != viewId }?.setText(Integer.toOctalString(value))
            editTextHex.takeIf { it.id != viewId }?.setText(Integer.toHexString(value))

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Dynamic Drawable Design
    private fun onFocusedViewDrawable(view: View){
        val gradientDrawable = GradientDrawable(GradientDrawable.Orientation.TL_BR,intArrayOf(Color.WHITE,Color.WHITE))
        gradientDrawable.shape = GradientDrawable.RECTANGLE
        gradientDrawable.cornerRadius = 16F
        gradientDrawable.setStroke(8,ContextCompat.getColor(this, android.R.color.holo_orange_light))
        view.background = gradientDrawable
    }

    private fun onRemoveFocusedViewDrawable(view: View){
        view.background = ContextCompat.getDrawable(this,R.drawable.edit_text_background)
    }

}