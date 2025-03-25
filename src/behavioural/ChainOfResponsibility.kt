package behavioural

enum class RequestMethod { GET, POST }

class HttpRequest(
    val method: String,
    val content: String,
    val headers: MutableMap<String, String>
)

class HttpResponse(val code: Int, val content: String)

class HttpClient(private val innerHandler: DelegatingHandler) {
    fun send(request: HttpRequest): String {
        val response = innerHandler.send(request)
        assert(response.code == 200) { "Response is not 200" }
        return response.content
    }
}

abstract class DelegatingHandler(open val innerHandler: DelegatingHandler? = null) {
    internal open fun send(request: HttpRequest): HttpResponse {
        val response = innerHandler?.send(request) ?: sendHttpRequest(request)
        return response
    }

    private fun sendHttpRequest(request: HttpRequest): HttpResponse {
        return HttpResponse(200, request.content.reversed())
    }
}

class LoggingHandler(override val innerHandler: DelegatingHandler? = null) : DelegatingHandler(innerHandler) {
    override fun send(request: HttpRequest): HttpResponse {
        println("LoggingHandler says hello")
        println("[LOG] Method: ${request.method}, Content: ${request.content}, Headers: ${request.headers}")
        return super.send(request)
    }
}

class AuthenticationHandler(override val innerHandler: DelegatingHandler? = null) : DelegatingHandler(innerHandler) {
    override fun send(request: HttpRequest): HttpResponse {
        println("AuthenticationHandler says hello")
        request.headers["Authorization"] = "Bearer access-token"
        return super.send(request)
    }
}

class RequestHeadersHandler(override val innerHandler: DelegatingHandler? = null) : DelegatingHandler(innerHandler) {
    override fun send(request: HttpRequest): HttpResponse {
        println("RequestHeadersHandler says hello")
        request.headers["Content-Type"] = "application/json"
        request.headers["User-Agent"] = "Mozilla/5.0"
        return super.send(request)
    }
}

fun main() {
    val loggingHandler = LoggingHandler()
    val authenticationHandler = AuthenticationHandler(loggingHandler)
    val headersHandler = RequestHeadersHandler(authenticationHandler)
    val client = HttpClient(headersHandler)
    println("Response:" + client.send(HttpRequest("GET", "Responsibility", mutableMapOf())))
}