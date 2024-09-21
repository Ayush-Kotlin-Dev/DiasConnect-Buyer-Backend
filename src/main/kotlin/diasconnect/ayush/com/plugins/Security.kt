package diasconnect.ayush.com.plugins

import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

private val jwtAudience = System.getenv("jwtAudience")
private val jwtIssuer = System.getenv("jwtIssuer")
private val jwtSecret = System.getenv("jwtSecret")

private const val CLAIM = "email"


fun Application.configureSecurity() {

    //dummy user dao


    authentication {
        jwt {

            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtSecret))
                    .withAudience(jwtAudience)
                    .withIssuer(jwtIssuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.getClaim(CLAIM).asString() != null) {
//                    val userExists = userDao.findByEmail(email = credential.payload.getClaim(CLAIM).asString()) != null
                    val isValidAudience = credential.payload.audience.contains(jwtAudience)
                    val userExists = true
                    if (userExists && isValidAudience) {
                        JWTPrincipal(payload = credential.payload)
                    } else {
                        null
                    }
                } else {
                    null
                }
            }

            challenge { _, _ ->
                call.respond(
                    status = HttpStatusCode.Unauthorized,
                    message = "You are not authorized to access this resource"
                )
            }
        }
    }
}

fun generateToken(email: String): String {
    return JWT.create()
        .withAudience(jwtAudience)
        .withIssuer(jwtIssuer)
        .withClaim(CLAIM, email)
        //.withExpiresAt()
        .sign(Algorithm.HMAC256(jwtSecret))
}









