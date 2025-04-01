package behavioural

abstract class SortingAlgorithmStrategy<T: Comparable<T>> {
    abstract fun compare(a: T, b: T): Int
}

class DescendingSort<T: Comparable<T>>: SortingAlgorithmStrategy<T>() {
    override fun compare(a: T, b: T): Int {
        if (a == b) return 0
        return if (a < b) 1 else -1
    }
}

class AscendingSort<T: Comparable<T>>: SortingAlgorithmStrategy<T>() {
    override fun compare(a: T, b: T): Int {
        if (a == b) return 0
        return if (a > b) 1 else -1
    }
}

fun<T: Comparable<T>> sort(elements: MutableList<T>, strategy: SortingAlgorithmStrategy<T>): MutableList<T> {
    val result = elements.toMutableList()
    var n = elements.size

    do {
        var swapped = false
        var i = 1
        while (i < n) {
            if (strategy.compare(result[i - 1], result[i]) > 0) {
                result[i - 1] = result[i].also { result[i] = result[i - 1] }
                swapped = true
            }
            i += 1
        }
        n -= 1
    } while (swapped)

    return result
}

fun main() {
    val elements = mutableListOf(5, 4, 8, 1, 6, 4)
    println("Descending sort: ${sort(elements, DescendingSort())}")
    println("Ascending sort: ${sort(elements, AscendingSort())}")
}