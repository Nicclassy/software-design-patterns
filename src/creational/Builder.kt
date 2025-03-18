package creational

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

enum class LogLevel(val value: Int) {
    INFO(20),
    WARN(30),
    ERROR(40)
}

class MinimalLogger(
    private val name: String,
    private val delimiter: String,
    private val minLogLevel: LogLevel,
    private val dateTimeFormatter: DateTimeFormatter,
) {
    fun info(message: String) {
        if (minLogLevel.value > LogLevel.INFO.value) return
        logMessage(LogLevel.INFO, message)
    }

    fun warn(message: String) {
        if (minLogLevel.value > LogLevel.WARN.value) return
        logMessage(LogLevel.WARN, message)
    }

    fun error(message: String) {
        if (minLogLevel.value > LogLevel.ERROR.value) return
        logMessage(LogLevel.INFO, message)
    }

    private fun logMessage(logLevel: LogLevel, message: String) {
        val datetime = LocalDateTime.now().format(dateTimeFormatter)
        println("${logLevel.name}$delimiter$name$delimiter$datetime $message")
    }
}

class MinimalLoggerBuilder {
    private var name: String? = null
    private var delimiter: String? = null
    private var minLogLevel: LogLevel? = null
    private var dateTimeFormatter: DateTimeFormatter? = null

    fun setName(name: String): MinimalLoggerBuilder = apply {
        this.name = name
    }

    fun setDelimiter(delimiter: String): MinimalLoggerBuilder = apply {
        this.delimiter = delimiter
    }

    fun setMinLogLevel(minLogLevel: LogLevel): MinimalLoggerBuilder = apply {
        this.minLogLevel = minLogLevel
    }

    fun setDateTimeFormatter(dateTimeFormatter: DateTimeFormatter): MinimalLoggerBuilder = apply {
        this.dateTimeFormatter = dateTimeFormatter
    }

    fun build(): MinimalLogger = MinimalLogger(
        name ?: "",
        delimiter ?: "@",
        minLogLevel ?: LogLevel.INFO,
        dateTimeFormatter ?: DateTimeFormatter.ISO_LOCAL_DATE_TIME,
    )
}

fun main() {
    val logger = MinimalLoggerBuilder()
        .setName("MainLogger")
        .setDelimiter("@")
        .setMinLogLevel(LogLevel.INFO)
        .build()
    logger.info("Program is running.")
}