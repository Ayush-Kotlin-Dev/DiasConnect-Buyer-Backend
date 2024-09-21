package diasconnect.ayush.com.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import java.time.Duration
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap

class RateLimiter(private val requestsPerMinute: Int) {
    private val requestCounts = ConcurrentHashMap<String, MutableList<Instant>>()

    fun isAllowed(clientId: String): Boolean {
        val now = Instant.now()
        val windowStart = now.minus(Duration.ofMinutes(1))

        val requestTimestamps = requestCounts.computeIfAbsent(clientId) { mutableListOf() }
        requestTimestamps.removeIf { it.isBefore(windowStart) }

        return if (requestTimestamps.size < requestsPerMinute) {
            requestTimestamps.add(now)
            true
        } else {
            false
        }
    }
}


fun Application.configureRateLimitedApiKeyAuthentication() {
    val validApiKeys = setOf("ayush")
    val rateLimiter = RateLimiter(requestsPerMinute = 60)

    intercept(ApplicationCallPipeline.Call) {
        // API Key Authentication
        val excludedPaths = setOf("/")
        if (call.request.path() !in excludedPaths) {
            // API Key Authentication
            val apiKey = call.request.headers["Api-Key"]
            if (apiKey == null || apiKey !in validApiKeys) {
                call.respond(HttpStatusCode.Unauthorized, "Invalid API Key")
                finish()
                return@intercept
            }

            // Rate Limiting
            //TODO        // can use API key as a client identifier if needed
            //        // to rate limit based on API key instead of client IP
            val clientId = call.request.origin.remoteHost // Using client's IP as an identifier
            if (!rateLimiter.isAllowed(clientId)) {
                call.respond(HttpStatusCode.TooManyRequests, "Rate limit exceeded")
                finish()
            }
        }



    }
}
