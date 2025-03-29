package behavioural

import java.io.File

class DataStatistics(val mean: Double, val median: Double)

fun<T> List<T>.median(): Double where T: Number, T: Comparable<T> = this.sorted().let {
    val medianIndex = size / 2
    this[medianIndex].let { mid ->
        if (size % 2 == 0)
            ((mid.toDouble() + this[medianIndex - 1].toDouble()) / 2.0)
        else
            mid.toDouble()
    }
}

abstract class StatisticalAnalyser {
    protected abstract fun readData(source: String)
    protected abstract fun createStatistics(): DataStatistics
    protected abstract fun reportStatistics(statistics: DataStatistics)

    fun analyse(source: String) {
        readData(source)
        val dataStatistics = createStatistics()
        reportStatistics(dataStatistics)
    }
}

class CSVAnalyser(private val data: MutableList<Int> = mutableListOf()) : StatisticalAnalyser() {
    override fun readData(source: String) {
        val csvFile = File(source)
        csvFile.readLines().forEachIndexed { index, line ->
            if (index != 0)
                data.addAll(line.split(",").map { it.toInt() })
        }
    }

    override fun createStatistics(): DataStatistics = DataStatistics(data.average(), data.median())

    override fun reportStatistics(statistics: DataStatistics) {
        println(
            "The mean in all of the CSV values is ${statistics.mean} " +
            "and the median is ${statistics.median}"
        )
    }
}

class TextAnalyser(
    private val separator: String,
    private val data: MutableList<Double> = mutableListOf()
) : StatisticalAnalyser() {
    override fun readData(source: String) {
        data.addAll(source.split(separator).mapNotNull { it.toDoubleOrNull() })
    }

    override fun createStatistics(): DataStatistics = DataStatistics(data.average(), data.median())

    override fun reportStatistics(statistics: DataStatistics) {
        println(
            "The mean in the provided text values is ${statistics.mean} " +
            "and the median is ${statistics.median}"
        )
    }
}

fun setupCsvFile(path: String) {
    val csvFile = File(path)
    if (csvFile.exists())
        csvFile.delete()
    csvFile.createNewFile()
    csvFile.appendText("value1,value2,value3\n1,2,3,4")
}

fun deleteCsvFile(path: String) {
    val csvFile = File(path)
    csvFile.delete()
}

fun main() {
    val path = "data.csv"
    setupCsvFile(path)


    var analyser: StatisticalAnalyser = CSVAnalyser()
    analyser.analyse(path)
    deleteCsvFile(path)

    analyser = TextAnalyser(";")
    analyser.analyse("0;5;6;7;8")
}