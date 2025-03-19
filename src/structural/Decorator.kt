package structural

abstract class Cache<K, V> {
    abstract fun get(key: K): V?
    abstract fun put(key: K, value: V)
}

class IntCache(private val mapping: MutableMap<Int, Int> = HashMap()) : Cache<Int, Int>() {
    override fun get(key: Int): Int? = mapping[key]

    override fun put(key: Int, value: Int) {
        mapping[key] = value
    }
}

class LogDecoratedIntCache(private val cache: IntCache) : Cache<Int, Int>() {
    override fun put(key: Int, value: Int) {
        println("Setting $key = $value")
        cache.put(key, value)
    }

    override fun get(key: Int): Int? {
        println("Getting the value for $key")
        val result = cache.get(key)
        if (result == null) {
            println("Cache does not contain the key $key")
        } else {
            println("The value for $key is $result")
        }
        return result
    }
}

fun fibonacci(n: Int): Int = fibonacci(n, 0, 1)
tailrec fun fibonacci(n: Int, a: Int, b: Int): Int =
    if (n == 0) a else fibonacci(n - 1, b, a + b)

fun main() {
    val intCache = IntCache()
    val decoratedIntCache = LogDecoratedIntCache(intCache)

    for (i in 1..10) {
        val fib = fibonacci(i)
        intCache.put(i, fib)
        decoratedIntCache.put(i, fib)
    }

    for (i in 1..10) {
        assert(intCache.get(i) == decoratedIntCache.get(i))
    }
}