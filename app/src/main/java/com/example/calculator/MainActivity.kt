package com.example.calculator

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity(), View.OnClickListener {
    private lateinit var textResult: TextView;
    private lateinit var textExpression: TextView;

    private var state:Int = 1

    private var op:String = "";
    private var op1:Int = 0;
    private var op2:Int = 0;
    private var result:Int = 0;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.constraint_layout);

        textResult = findViewById(R.id.textView);
        textExpression = findViewById(R.id.textView2)

        findViewById<Button>(R.id.button1).setOnClickListener(this);
        findViewById<Button>(R.id.button2).setOnClickListener(this);
        findViewById<Button>(R.id.button3).setOnClickListener(this);
        findViewById<Button>(R.id.button4).setOnClickListener(this);
        findViewById<Button>(R.id.button5).setOnClickListener(this);
        findViewById<Button>(R.id.button6).setOnClickListener(this);
        findViewById<Button>(R.id.button7).setOnClickListener(this);
        findViewById<Button>(R.id.button8).setOnClickListener(this);
        findViewById<Button>(R.id.button9).setOnClickListener(this);
        findViewById<Button>(R.id.button10).setOnClickListener(this);
        findViewById<Button>(R.id.button11).setOnClickListener(this);
        findViewById<Button>(R.id.button12).setOnClickListener(this);
        findViewById<Button>(R.id.button13).setOnClickListener(this);
        findViewById<Button>(R.id.button14).setOnClickListener(this);
        findViewById<Button>(R.id.button15).setOnClickListener(this);
        findViewById<Button>(R.id.button16).setOnClickListener(this);
        findViewById<Button>(R.id.button17).setOnClickListener(this);
        findViewById<Button>(R.id.button18).setOnClickListener(this);
        findViewById<Button>(R.id.button19).setOnClickListener(this);
        findViewById<Button>(R.id.button20).setOnClickListener(this);
    }

    override fun onClick(p0: View?) {
        val id = p0?.id;

        when (id) {
            R.id.button1 -> {
                clearElement();
            }
            R.id.button2 -> {
                clear();
            }
            R.id.button3 -> {
                backspace();
            }
            R.id.button4 -> {
                if (state == 1){
                    state = 2;
                } else if (state == 2) {
                    calculatePrevious();
                } else if (state == 3) {
                    op1 = result;
                    op2 = 0;
                    state = 2;
                }
                op = "/";

                textExpression.text = "$op1 $op";
                textResult.text = "$op1";
            }
            R.id.button5 -> {
                addDigit(7);
            }
            R.id.button6 -> {
                addDigit(8);
            }
            R.id.button7 -> {
                addDigit(9);
            }
            R.id.button8 -> {
                if (state == 1){
                    state = 2;
                } else if (state == 2){
                    calculatePrevious();
                } else if (state == 3) {
                    op1 = result;
                    op2 = 0;
                    state = 2;
                }
                op = "x";

                textExpression.text = "$op1 $op";
                textResult.text = "$op1";
            }
            R.id.button9 -> {
                addDigit(4);
            }
            R.id.button10 -> {
                addDigit(5);
            }
            R.id.button11 -> {
                addDigit(6);
            }
            R.id.button12 -> {
                if (state == 1){
                    state = 2;
                } else if (state == 2){
                    calculatePrevious();
                } else if (state == 3) {
                    op1 = result;
                    op2 = 0;
                    state = 2;
                }
                op = "-";

                textExpression.text = "$op1 $op";
                textResult.text = "$op1";
            }
            R.id.button13 -> {
                addDigit(1);
            }
            R.id.button14 -> {
                addDigit(2);
            }
            R.id.button15 -> {
                addDigit(3);
            }
            R.id.button16 -> {
                if (state == 1){
                    state = 2;
                } else if (state == 2){
                    calculatePrevious();
                } else if (state == 3){
                    op1 = result;
                    op2 = 0;
                    state = 2;
                }
                op = "+";

                textExpression.text = "$op1 $op";
                textResult.text = "$op1";
            }
            R.id.button17 -> {
                if (state == 1) {
                    op1 = -op1;
                    textResult.text = "$op1";
                } else if (state == 2) {
                    op2 = -op2;
                    textResult.text = "$op2";
                } else if (state == 3){
                    op1 = -op1;
                    op2 = 0;
                    textResult.text = "$op1";
                    state = 1;
                }
            }
            R.id.button18 -> {
                addDigit(0);
            }
            R.id.button19 -> {
                showDialog("Đề bài yêu cầu thực hiện với số nguyên.");
            }
            R.id.button20 -> {
                if (state == 1){
                    state = 3;
                    result = op1;
                    textExpression.text = "$op1 ="
                    textResult.text = "$op1"
                    return;
                }

                if (op == "+") {
                    result = op1 + op2;
                } else if (op == "-"){
                    result = op1 - op2;
                } else if (op == "x"){
                    result = op1 * op2;
                } else if (op == "/"){
                    if (op2 == 0){
                        showDialog("Không thể chia cho số 0.");
                        return;
                    } else {
                        result = op1 / op2;
                    }
                }
                textExpression.text = "$op1 $op $op2 =";
                textResult.text = "$result";
                state = 3;
                op1 = result;
            }
        }

        val textLength = textResult.text.length;

        if (textLength == 11) {
            textResult.textSize = 57.0F;
        } else if (textLength == 12) {
            textResult.textSize = 53.0F;
        } else if (textLength == 13) {
            textResult.textSize = 50.0F;
        } else if (textLength == 14) {
            textResult.textSize = 46.0F;
        } else if (textLength == 15) {
            textResult.textSize = 43.0F;
        } else if (textLength == 16) {
            textResult.textSize = 40.0F;
        } else {
            textResult.textSize = 60.0F;
        }
    }

    private fun addDigit(c: Int) {
        if (state == 1){
            op1 = op1 * 10 + c;
            textResult.text = "$op1";
        } else if (state == 2) {
            op2 = op2 * 10 + c;
            textResult.text = "$op2";
        } else if (state == 3) {
            op1 = c;
            op2 = 0;
            textExpression.text = "";
            textResult.text = "$op1";
            state = 1;
        }
    }

    private fun calculatePrevious(){
        if (op == "+") {
            op1 += op2;
        } else if (op == "-"){
            op1 -= op2;
        } else if (op == "x"){
            op1 *= op2;
        } else if (op == "/"){
            op1 /= op2;
        }
        op2 = 0;
    }

    private fun backspace(){
        if (state == 1){
            op1 /= 10;
            textResult.text = "$op1";
        } else if (state == 2){
            op2 /= 10;
            textResult.text = "$op2";
        } else if (state == 3){
            textExpression.text = "";
        }
    }

    private fun clear(){
        state = 1;
        op = "";
        op1 = 0;
        op2 = 0;
        textExpression.text = "";
        textResult.text = "0";
    }

    private fun clearElement(){
        if (state == 1){
            op1 = 0;
            textResult.text = "0";
        } else if (state == 2){
            op2 = 0;
            textResult.text = "0";
        } else if (state == 3){
            clear();
        }
    }

    private fun showDialog(message: String) {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Calculator says:")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }

        val dialog = builder.create()
        dialog.show()
    }
}