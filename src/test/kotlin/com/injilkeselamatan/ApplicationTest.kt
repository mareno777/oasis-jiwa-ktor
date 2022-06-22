package com.injilkeselamatan

import com.injilkeselamatan.plugins.configureRouting
import io.ktor.http.*
import io.ktor.network.tls.certificates.*
import io.ktor.server.testing.*
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun testRoot() {
        withTestApplication({ configureRouting() }) {
            generateCertificate(
                file = File("keystore.jks"),
                keyAlias = "oasis-jiwa",
                keyPassword = "jgm4ever",
                jksPassword = "jgm4ever"
            )
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("Hello World", response.content)
            }
        }
    }
}