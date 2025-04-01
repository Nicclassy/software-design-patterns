package behavioural

enum class SimpleOperator { PLUS, MINUS, MULTIPLY, DIVIDE }

abstract class MathematicalTerm

data class NumericTerm(val value: Int) : MathematicalTerm()
data class OperatorTerm(val value: SimpleOperator) : MathematicalTerm()
data class MathematicalExpression(val terms: List<MathematicalTerm>) : MathematicalTerm() {
    constructor(vararg terms: MathematicalTerm) : this(terms.toList())
}

abstract class MathematicalTermVisitor {
    abstract fun accept(value: NumericTerm)
    abstract fun accept(value: OperatorTerm)
    abstract fun accept(value: MathematicalExpression)
}

class Calculator : MathematicalTermVisitor() {
    private val stack = mutableListOf<Int>()
    private var lastOperator: SimpleOperator? = null

    var result: Int = 0
        private set

    override fun accept(value: NumericTerm) {
        if (lastOperator == null) {
            stack.add(value.value)
            return
        }

        val left = stack.removeLastOrNull() ?: throw IllegalStateException("Cannot calculate expression")
        val computed = when (lastOperator) {
            SimpleOperator.PLUS -> left + value.value
            SimpleOperator.MINUS -> left - value.value
            SimpleOperator.MULTIPLY -> left * value.value
            SimpleOperator.DIVIDE -> {
                if (value.value == 0) throw ArithmeticException("Cannot divide by zero")
                left / value.value
            }
            else -> throw IllegalStateException("Cannot calculate expression")
        }

        stack.add(computed)
        lastOperator = null
    }

    override fun accept(value: OperatorTerm) {
        lastOperator = value.value
    }

    override fun accept(value: MathematicalExpression) {
        value.terms.forEach {
            when (it) {
                is NumericTerm -> accept(it)
                is OperatorTerm -> accept(it)
                is MathematicalExpression -> accept(it)
            }
        }
        result = stack.firstOrNull() ?: throw IllegalStateException("Invalid expression")
    }
}

class OddNumberCounter : MathematicalTermVisitor() {
    var result = 0
        private set

    override fun accept(value: NumericTerm) {
        if (value.value % 2 != 0)
            result += 1
    }

    override fun accept(value: OperatorTerm) {}

    override fun accept(value: MathematicalExpression) {
        value.terms.forEach {
            when (it) {
                is NumericTerm -> accept(it)
                is OperatorTerm -> accept(it)
                is MathematicalExpression -> accept(it)
            }
        }
    }
}

fun main() {
    val expression = MathematicalExpression(
        NumericTerm(3),
        OperatorTerm(SimpleOperator.MULTIPLY),
        NumericTerm(5),
        OperatorTerm(SimpleOperator.PLUS),
        NumericTerm(2),
        OperatorTerm(SimpleOperator.PLUS),
        MathematicalExpression(
            NumericTerm(7),
            OperatorTerm(SimpleOperator.MINUS),
            NumericTerm(6),
        )
    )

    val calculator = Calculator()
    calculator.accept(NumericTerm(2))
    calculator.accept(OperatorTerm(SimpleOperator.PLUS))
    calculator.accept(expression)
    println("The calculator result is ${calculator.result}")

    val counter = OddNumberCounter()
    counter.accept(NumericTerm(1))
    counter.accept(expression)
    println("The odd number counter was given ${counter.result} odd numbers.")
}



