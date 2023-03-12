package com.example.numberconverterapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {


    private lateinit var editTextDecimal: EditText
    private lateinit var editTextBinary: EditText
    private lateinit var editTextOctal: EditText
    private lateinit var editTextHex: EditText
    private lateinit var buttonClear: Button
    private var focusedView: EditText? = null
    private var textWatcher: TextWatcher? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        addCallBacks()
        initListeners()

    }

    private fun initViews() {
        editTextDecimal = findViewById(R.id.edit_text_decimal)
        editTextBinary = findViewById(R.id.edit_text_binary)
        editTextOctal = findViewById(R.id.edit_text_octal)
        editTextHex = findViewById(R.id.edit_text_hex)
        buttonClear = findViewById(R.id.button_clear)
    }

    private fun initListeners() {
        editTextDecimal.onFocusChangeListener = CustomFocusChangeListener()
        editTextBinary.onFocusChangeListener = CustomFocusChangeListener()
        editTextOctal.onFocusChangeListener = CustomFocusChangeListener()
        editTextHex.onFocusChangeListener = CustomFocusChangeListener()
    }


    private fun addCallBacks() {

        textWatcher = object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val number = s.toString().trim()
                focusedView?.let { convertNumberBases(it, number) }

            }


            override fun afterTextChanged(p0: Editable?) {

            }

        }

        buttonClear.setOnClickListener {
            clearFields()
        }

    }

    private fun convertToDecimal(number: String, base: Int): String {
        if (number.isEmpty()) {
            return ""
        }
        return number.toIntOrNull(base)?.toString() ?: ""
    }

    private fun convertFromDecimal(number: String, base: Int): String {
        return number.toInt().toString(base)
    }

    private fun convertNumberBases(view: EditText, number: String) {
        try {
            val value = when (view.id) {
                R.id.edit_text_decimal -> convertToDecimal(number, DECIMAL)
                R.id.edit_text_binary -> convertToDecimal(number, BINARY)
                R.id.edit_text_octal -> convertToDecimal(number, OCTAL)
                else -> convertToDecimal(number, HEXADECIMAL)
            }

            editTextDecimal.takeIf { it.id != view.id }?.setText(convertFromDecimal(value, DECIMAL))
            editTextBinary.takeIf { it.id != view.id }?.setText(convertFromDecimal(value, BINARY))
            editTextOctal.takeIf { it.id != view.id }?.setText(convertFromDecimal(value, OCTAL))
            editTextHex.takeIf { it.id != view.id }?.setText(convertFromDecimal(value, HEXADECIMAL))

        } catch (e: NumberFormatException) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun clearFields() {

        editTextDecimal.setText("")
        editTextBinary.setText("")
        editTextOctal.setText("")
        editTextHex.setText("")

    }

    inner class CustomFocusChangeListener : View.OnFocusChangeListener {
        override fun onFocusChange(view: View?, hasFocus: Boolean) {

            if (hasFocus) {
                focusedView = view as EditText
                focusedView!!.addTextChangedListener(textWatcher)
            } else {
                focusedView!!.removeTextChangedListener(textWatcher)
            }
        }

    }

    companion object {
        private const val DECIMAL = 10
        private const val BINARY = 2
        private const val OCTAL = 8
        private const val HEXADECIMAL = 16
    }
}