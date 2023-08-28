package com.example.arithmeticoperationsgame

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.KeyEvent
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

lateinit var globalTimer: MyTimer


class GameActivity : AppCompatActivity() {
    lateinit var leftMathExpression: TextView
    lateinit var rightMathExpression: TextView
    lateinit var lessThanButton: Button
    lateinit var equalsButton: Button
    lateinit var greaterThanButton: Button
    lateinit var totalText: TextView
    lateinit var rightText: TextView
    lateinit var timerText: TextView
    lateinit var displayExpressions: DisplayExpressions
    lateinit var wrongTextNumber: TextView
    lateinit var wrongTextString: TextView
    lateinit var rightTextNumber: TextView
    lateinit var rightTextString: TextView
    lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        leftMathExpression = findViewById(R.id.left_math_expression_text)
        rightMathExpression = findViewById(R.id.right_math_expression_text)
        lessThanButton = findViewById(R.id.less_than_button)
        equalsButton = findViewById(R.id.equals_button)
        greaterThanButton = findViewById(R.id.greater_than_button)

        totalText = findViewById(R.id.total_text)
        rightText = findViewById(R.id.right_text)

        wrongTextNumber = findViewById(R.id.wrong_text_large_number)
        wrongTextString = findViewById(R.id.wrong_text_large_string)
        rightTextNumber = findViewById(R.id.right_text_large_number)
        rightTextString = findViewById(R.id.right_text_large_string)
        backButton = findViewById(R.id.back_button)

        var resultText = findViewById<TextView>(R.id.result_text)
        resultText.visibility = View.INVISIBLE
        var resultTextUpdater = UpdateText(resultText)

        timerText = findViewById(R.id.timer_text)
        var startTime: Long = 50000
        if (savedInstanceState != null) {
            startTime = savedInstanceState!!.getLong("timer")!!
        }

        var timer = MyTimer(startTime, 1000,this)
        timerText.text = (startTime/1000).toString()
        timer.start()
        globalTimer = timer

        if (savedInstanceState != null) {
            ResultsCounter.total = savedInstanceState.getInt("total", 0)!!
            ResultsCounter.right = savedInstanceState!!.getInt("right", 0)!!
            ResultsCounter.wrong = savedInstanceState!!.getInt("wrong", 0)!!
        }

        displayExpressions = DisplayExpressions(
            resultTextUpdater,
            leftMathExpression,
            rightMathExpression,
            lessThanButton,
            equalsButton,
            greaterThanButton,
            totalText,
            rightText,
            savedInstanceState,
            timer
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (displayExpressions != null) {
            outState.putString("leftExpressionString", displayExpressions.leftExpressionString)
            outState.putString("rightExpressionString", displayExpressions.rightExpressionString)
            outState.putDouble("leftExpressionAnswer", displayExpressions.leftExpressionAnswer)
            outState.putDouble("rightExpressionAnswer", displayExpressions.rightExpressionAnswer)
        }
        outState.putInt("total", ResultsCounter.total)
        outState.putInt("right", ResultsCounter.right)
        outState.putInt("wrong", ResultsCounter.wrong)
        outState.putLong("timer", globalTimer.timeLeft)
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        ResultsCounter.total = 0
        ResultsCounter.right = 0
        ResultsCounter.wrong = 0
        globalTimer.cancel()
        super.onBackPressed()
    }

    fun hideAllElements() {
        backButton.setOnClickListener{
            onBackPressed()
        }

        wrongTextNumber.text = ResultsCounter.wrong.toString()
        rightTextNumber.text = ResultsCounter.right.toString()

        wrongTextNumber.visibility = View.VISIBLE
        wrongTextString.visibility = View.VISIBLE
        rightTextNumber.visibility = View.VISIBLE
        rightTextString.visibility = View.VISIBLE
        backButton.visibility = View.VISIBLE

        lessThanButton.visibility = View.INVISIBLE
        equalsButton.visibility = View.INVISIBLE
        greaterThanButton.visibility = View.INVISIBLE
        leftMathExpression.visibility = View.INVISIBLE
        rightMathExpression.visibility = View.INVISIBLE
        totalText.visibility = View.INVISIBLE
        rightText.visibility = View.INVISIBLE
        timerText.visibility = View.INVISIBLE
    }

    fun showAllElements() {

    }
}

object ResultsCounter {
    var right: Int = 0
    var wrong: Int = 0
    var total: Int = 0
}

class MyTimer(
    var startTime: Long = 10000,
    var countdownTime: Long = 1000,
    var mainActivity: GameActivity
): CountDownTimer(startTime, countdownTime) {
    var timeLeft: Long = 0

    override fun onTick(p0: Long) {
        mainActivity.timerText.text = (p0/1000).toString()
        timeLeft = p0
    }

    override fun onFinish() {
        mainActivity.hideAllElements()
    }
}

class DisplayExpressions(
    var resultTextUpdater: UpdateText,
    var leftMathExpression: TextView,
    var rightMathExpression: TextView,
    var lessThanButton: Button,
    var equalsButton: Button,
    var greaterThanButton: Button,
    var totalText: TextView,
    var rightText: TextView,
    var savedInstanceState: Bundle?,
    var timer: MyTimer
) {

    lateinit var leftExpressionString: String
    lateinit var rightExpressionString: String
    var leftExpressionAnswer = 0.0
    var rightExpressionAnswer = 0.0

    init {
        main()
    }

    fun main() {
//        resultTextUpdater.resultText.visibility = View.INVISIBLE

        totalText.text = ResultsCounter.total.toString()
        rightText.text = ResultsCounter.right.toString()

        var expressionGeneration = ExpressionGenerator()

        if (savedInstanceState != null) {
            expressionGeneration.leftExpressionString = savedInstanceState!!.getString("leftExpressionString", expressionGeneration.leftExpressionString)!!
            expressionGeneration.rightExpressionString = savedInstanceState!!.getString("rightExpressionString", expressionGeneration.rightExpressionString)!!
            expressionGeneration.leftExpressionAnswer = savedInstanceState!!.getDouble("leftExpressionAnswer", expressionGeneration.leftExpressionAnswer)!!
            expressionGeneration.rightExpressionAnswer = savedInstanceState!!.getDouble("rightExpressionAnswer", expressionGeneration.rightExpressionAnswer)!!
            savedInstanceState = null
        }

        leftExpressionString = expressionGeneration.leftExpressionString
        rightExpressionString = expressionGeneration.rightExpressionString
        leftExpressionAnswer = expressionGeneration.leftExpressionAnswer
        rightExpressionAnswer = expressionGeneration.rightExpressionAnswer

        leftMathExpression.text = leftExpressionString
        rightMathExpression.text = rightExpressionString

//        lessThanButton.text = expressionGeneration.leftExpressionAnswer.toString()
//        greaterThanButton.text = expressionGeneration.rightExpressionAnswer.toString()

        var listener = MyButtonListener(resultTextUpdater, this, expressionGeneration)
        lessThanButton.setOnClickListener(listener)
        equalsButton.setOnClickListener(listener)
        greaterThanButton.setOnClickListener(listener)
    }
}

class MyAnimationListener(var resultText: TextView) : Animation.AnimationListener {
    override fun onAnimationStart(p0: Animation?) {
        resultText.visibility = View.VISIBLE
    }

    override fun onAnimationEnd(animation: Animation) {
        resultText.visibility = View.INVISIBLE
    }

    override fun onAnimationRepeat(p0: Animation?) {
        TODO("Not yet implemented")
    }
}

class UpdateText(var resultText: TextView) {
    fun updateText(result: Boolean) {
        if (result) {
            resultText.text = "Correct!"
            resultText.setTextColor(Color.parseColor("#0F8B8D"))
        } else {
            resultText.text = "Wrong!"
            resultText.setTextColor(Color.parseColor("#A8201A"))
        }
        resultText.visibility = View.VISIBLE

        val alphaAnim = AlphaAnimation(1.0f, 0.0f)
        alphaAnim.startOffset = 3000
        alphaAnim.duration = 400
        alphaAnim.setAnimationListener(MyAnimationListener(resultText))
        resultText.animation = alphaAnim
    }
}

class MyButtonListener(
    var resultTextUpdater: UpdateText,
    var displayExpressions: DisplayExpressions,
    var expressionGeneration: ExpressionGenerator
) : View.OnClickListener {
    override fun onClick(p0: View?) {
        var button = p0 as Button
        var result = false

        result = when (button.id) {
            R.id.less_than_button -> {
                expressionGeneration.leftExpressionAnswer < expressionGeneration.rightExpressionAnswer
            }
            R.id.equals_button -> {
                expressionGeneration.leftExpressionAnswer == expressionGeneration.rightExpressionAnswer
            }
            R.id.greater_than_button -> {
                expressionGeneration.leftExpressionAnswer > expressionGeneration.rightExpressionAnswer
            }
            else -> {
                false
            }
        }

        if (result) {
            ResultsCounter.right++
            Toast.makeText(p0.context, "Correct!", Toast.LENGTH_SHORT).show()
        } else {
            ResultsCounter.wrong++
            Toast.makeText(p0.context, "Wrong!", Toast.LENGTH_SHORT).show()
        }

        ResultsCounter.total++
        resultTextUpdater.updateText(result)

        if (ResultsCounter.right % 5 == 0 && ResultsCounter.right != 0 && result) {
            var startTime = displayExpressions.timer.timeLeft + 10000
            displayExpressions.timer.cancel()
            displayExpressions.timer.mainActivity.timerText.text = (startTime/1000).toString()
            displayExpressions.timer = MyTimer(
                startTime,
                1000,
                displayExpressions.timer.mainActivity
            )
            displayExpressions.timer.start()
            globalTimer = displayExpressions.timer
        }
        displayExpressions.main()

    }
}


class ExpressionGenerator {
    val randomGenerator = Random()
    val operators = listOf('*', '/', '+', '-')
    var leftExpressionList = mutableListOf<String>()
    var rightExpressionList = mutableListOf<String>()
    var leftExpressionAnswer = 0.0
    var rightExpressionAnswer = 0.0
    var leftExpressionString: String = ""
    var rightExpressionString: String = ""

    private fun multiply(x: Double, y: Double): Double {
        return x * y
    }

    private fun add(x: Double, y: Double): Double {
        return x + y
    }

    private fun divide(x: Double, y: Double): Double {
        return x / y
    }

    private fun minus(x: Double, y: Double): Double {
        return x - y
    }

    private fun detectOperation(x: Double, y: Double, operator: Char): Double {
        return when (operator) {
            '*' -> {
                multiply(x, y)
            }
            '/' -> {
                divide(x, y)
            }
            '+' -> {
                add(x, y)
            }
            else -> {
                minus(x, y)
            }
        }
    }

    fun createExpressionString(expressionList: List<String>): String {
        var expressionString: String = ""
        if (expressionList.size != 1) {
            expressionString += "(".repeat(expressionList.size - 1)
        }
        for ((index, value) in expressionList.withIndex()) {
            if (expressionList.size == 1) {
                expressionString = value
                break
            }

            expressionString += if (index == expressionList.size - 1) {
                value
            } else {
                "$value)"
            }
        }
        return expressionString
    }

    private fun generateExpressions() {
        leftExpressionList = mutableListOf<String>()
        rightExpressionList = mutableListOf<String>()
        leftExpressionAnswer = 0.0
        rightExpressionAnswer = 0.0
        leftExpressionString = ""
        rightExpressionString = ""

        for (j in 1..2) {
            var amountOfExpressions = randomGenerator.nextInt(4)

            for (i in 1..amountOfExpressions) {
                var firstNumber = randomGenerator.nextInt(20)
                var secondNumber = randomGenerator.nextInt(20)
                var firstNumberDouble = firstNumber.toDouble()
                var secondNumberDouble = secondNumber.toDouble()
                var randomOperator = operators[randomGenerator.nextInt(operators.size)]

                var expressionString: String = if (i == 1) {
                    firstNumber.toString() + randomOperator + secondNumber.toString()
                } else {
                    randomOperator + secondNumber.toString()
                }

                if (j == 1) {
                    leftExpressionList.add(expressionString)
                    if (i != 1)
                        firstNumberDouble = leftExpressionAnswer
                    leftExpressionAnswer =
                        detectOperation(firstNumberDouble, secondNumberDouble, randomOperator)
                } else {
                    rightExpressionList.add(expressionString)
                    if (i != 1)
                        firstNumberDouble = rightExpressionAnswer
                    rightExpressionAnswer =
                        detectOperation(firstNumberDouble, secondNumberDouble, randomOperator)
                }
            }
        }
    }

    private fun isValid(value: Double): Boolean {
        return (value - value.toInt() == 0.0
                && value > 0
                && value <= 100)
    }

    fun main() {
        while (true) {
            generateExpressions()

            if (isValid(leftExpressionAnswer)
                && isValid(rightExpressionAnswer)
                && leftExpressionList.isNotEmpty()
                && rightExpressionList.isNotEmpty()) {
                break
            }
        }
        leftExpressionString = createExpressionString(leftExpressionList)
        rightExpressionString = createExpressionString(rightExpressionList)
    }

    init {
        main()
    }
}
