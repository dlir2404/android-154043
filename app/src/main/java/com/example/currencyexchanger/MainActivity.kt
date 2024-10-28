package com.example.currencyexchanger

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.widget.doAfterTextChanged
import android.text.InputFilter
import android.widget.AdapterView
import android.util.Log

class MainActivity : ComponentActivity() {
    var value1: Double = 0.0
    var value2: Double = 0.0

    lateinit var input1: EditText
    lateinit var input2: EditText
    lateinit var textRatio: TextView

    lateinit var currency1: Currency
    lateinit var currency2: Currency

    private var from = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.currency_exchanger)

        textRatio = findViewById(R.id.text_ratio)

        val currencyList = mutableListOf(
            Currency("Dollar", "United States", "USD", 1.0),
            Currency("Pound", "United Kingdom", "GBP", 1.2979),
            Currency("Dong", "Vietnam", "VND", 0.00003944),
            Currency("Yen", "Japan", "JPY", 0.006575),
            Currency("Won", "Korea", "KRW", 0.0007192)
        )

        currency1 = currencyList[0] // Default to USD
        currency2 = currencyList[0] // Default to USD

        val adapter: ArrayAdapter<String> = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            currencyList.map { item -> "${item.country} - ${item.name}" }
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val spinner1 = findViewById<Spinner>(R.id.spinner_1)
        spinner1.adapter = adapter
        spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View, position: Int, id: Long) {
                currency1 = currencyList[position]
                calculate() // Recalculate conversion when currency1 changes
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        val spinner2 = findViewById<Spinner>(R.id.spinner_2)
        spinner2.adapter = adapter
        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View, position: Int, id: Long) {
                currency2 = currencyList[position]
                calculate() // Recalculate conversion when currency2 changes
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        input1 = findViewById(R.id.edit_text1)
        input1.setText(value1.toString())
        input1.filters = arrayOf(InputFilter.LengthFilter(12))
        input1.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                from = 1;
                calculate();
            }
        }
        input1.doAfterTextChanged { text ->
            if (from == 1) {
                calculate() // Recalculate when input1 changes
            }
        }

        input2 = findViewById(R.id.edit_text2)
        input2.setText(value2.toString())
        input2.filters = arrayOf(InputFilter.LengthFilter(12))
        input2.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                from = 2;
                calculate();
            }
        }
        input2.doAfterTextChanged { text ->
            if (from == 2) {
                calculate() // Recalculate when input2 changes
            }
        }
    }

    private fun calculate() {
        var ratio: Double;
        Log.v("tag", "$from")
        if (from == 1) {
            ratio = currency2.ratioToUSD / currency1.ratioToUSD

            // Update input2 based on input1
            val inputValue1 = input1.text.toString().toDoubleOrNull() ?: 0.0
            val convertedValue = inputValue1 * (1 / ratio)
            input2.setText(convertedValue.toString())

            textRatio.text = "1 ${currency1.acronym} = ${1 / ratio} ${currency2.acronym}" // Update the ratio text
        } else {
            ratio = currency1.ratioToUSD / currency2.ratioToUSD

            // Update input1 based on input2
            val inputValue2 = input2.text.toString().toDoubleOrNull() ?: 0.0
            val convertedValue = inputValue2 * (1 / ratio)
            input1.setText(convertedValue.toString())

            textRatio.text = "1 ${currency2.acronym} = ${1 / ratio} ${currency1.acronym}" // Update the ratio text
        }
    }
}
