package ru.mpei.pavlovis

import io.ktor.network.tls.certificates.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.slf4j.LoggerFactory
import ru.mpei.pavlovis.plugins.routing.configureRouting
import java.io.File

fun main() {
    val keyStoreFile = File("build/keystore.jks")
    val keyStore = buildKeyStore {
        certificate("pavlovIsCertificate") {
            password = "password123456"
            domains = listOf( "192.168.0.110", "127.0.0.1", "0.0.0.0", "localhost")
        }
    }
    keyStore.saveToFile(keyStoreFile, "passwordKeyStore123456")

    val enviroment = applicationEngineEnvironment {
        log = LoggerFactory.getLogger("ktor.application")
        connector {
            port = 8082
        }
        sslConnector(
            keyStore = keyStore,
            keyAlias = "pavlovIsCertificate",
            keyStorePassword = { "passwordKeyStore123456".toCharArray() },
            privateKeyPassword = { "password123456".toCharArray() }) {
            port = 8443
            keyStorePath = keyStoreFile
        }
        module(Application::module)
    }
    embeddedServer(
        Netty, enviroment
    ).start(wait=true)
    /*embeddedServer(Netty, port = 8082, host = "0.0.0.0", module = Application::module)
        .start(wait = true)*/
}

fun Application.module() {
    configureRouting()
}
