package com.example.simple_list

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.simple_list.ui.theme.Simple_listTheme

class MainActivity : ComponentActivity() {
    private lateinit var inNum: EditText
    private lateinit var radioGroup: RadioGroup
    private lateinit var checkEven: RadioButton
    private lateinit var checkOdd: RadioButton
    private lateinit var checkSquare: RadioButton
    private lateinit var buttonShow: Button
    private lateinit var listRes: ListView
    private lateinit var err: TextView
    private lateinit var adapter: ArrayAdapter<String>
    private val numbersList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)

        inNum = findViewById<EditText>(R.id.inputNum)
        checkEven = findViewById<RadioButton>(R.id.even)
        checkOdd = findViewById<RadioButton>(R.id.odd)
        checkSquare = findViewById<RadioButton>(R.id.perfectSquare)
        listRes = findViewById<ListView>(R.id.list)
        err = findViewById<TextView>(R.id.err)
        radioGroup = findViewById(R.id.radioGroup)
        buttonShow = findViewById(R.id.show)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, numbersList)
        listRes.adapter = adapter

        buttonShow.setOnClickListener { generateNumbers() }

    }

    private fun generateNumbers() {
        val input = inNum.text.toString()
        if (input.isEmpty()) {
            showError("Please enter a number")
            return
        }

        try {
            val n = input.toInt()
            if (n < 0) {
                showError("Please enter a positive number")
                return
            }

            numbersList.clear()

            when (radioGroup.checkedRadioButtonId) {
                R.id.even -> {
                    (0..n).filter { it % 2 == 0 }
                        .forEach { numbersList.add(it.toString()) }
                }
                R.id.odd -> {
                    (1..n).filter { it % 2 != 0 }
                        .forEach { numbersList.add(it.toString()) }
                }
                R.id.perfectSquare -> {
                    (0..kotlin.math.sqrt(n.toFloat()).toInt())
                        .map { it * it }
                        .takeWhile { it <= n }
                        .forEach { numbersList.add(it.toString()) }
                }
                else -> {
                    showError("Please select a number type")
                    return
                }
            }

            adapter.notifyDataSetChanged()
            err.text = ""

        } catch (e: NumberFormatException) {
            showError("Please enter a valid number")
        }
    }

    private fun showError(message: String) {
        err.text = message
        numbersList.clear()
        adapter.notifyDataSetChanged()
    }
}