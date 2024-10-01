package diasconnect.buyer.com.plugins

import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.head
import kotlinx.html.*

fun Application.configureRouting() {

    routing {
        route(path = "/") {
            get {
                call.respondHtml {
                    head {
                        title { +"Welcome to DiasConnect" }
                        style {
                            +"""
                            body {
                                font-family: 'Helvetica Neue', sans-serif;
                                background-color: #f4f4f9;
                                color: #333;
                                padding: 40px;
                                text-align: center;
                            }
                            h1 {
                                color: #4CAF50;
                                font-size: 3em;
                            }
                            h2 {
                                color: #555;
                                margin-top: 20px;
                                font-size: 1.5em;
                            }
                            p {
                                color: #777;
                                font-size: 1.2em;
                                line-height: 1.6;
                                margin-top: 20px;
                            }
                        """.trimIndent()
                        }
                    }
                    body {
                        h1 { +"Welcome to DiasConnect Backend Server " }
                        h2 { +"E-Commerce for Indian Diasporas" }
                        p {
                            +"India Post - A Bridge for Indian Diaspora to access things Indian."
                        }
                        p {
                            +"Building a community of Indian Diaspora for meeting their needs of traditional, ethnic, and handicraft products "
                            +"through India Post by connecting PIOs with local sellers, MSMEs, and artisans."
                        }
                    }
                }
            }
        }
    }
}