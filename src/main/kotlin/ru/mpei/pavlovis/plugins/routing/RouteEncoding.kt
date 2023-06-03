package ru.mpei.pavlovis.plugins.routing

import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.mpei.pavlovis.domain.decodeByDefaultMode
import ru.mpei.pavlovis.domain.decodeBySteganographyMode
import ru.mpei.pavlovis.domain.encodeByDefaultMode
import ru.mpei.pavlovis.domain.encodeBySteganographyMode

fun Route.magmaRouteEncoding() = post("/magma/encode") {
    val mode = call.parameters["mode"]
    val threads = call.parameters["threads"]
    val multiPart = try {
        call.receiveNullable<MultiPartData>()
    } catch (e: Exception) {
        println(e.message)
        null
    }
    if (multiPart != null) {
        val (fileBytes, password) = receiveFileAndPasswordPartData(multiPart)
        if (fileBytes.isNotEmpty()) {
            if (!password.isNullOrEmpty()) {
                if (mode != null || threads != null) {
                    val result: ByteArray? = when (mode) {
                        "default" -> encodeByDefaultMode(fileBytes, threads, password)
                        "steganography" -> encodeBySteganographyMode(fileBytes, threads, password)
                        else -> null
                    }
                    if (result != null) {
                        call.respondBytes(result)
                    } else {
                        call.respondText("One of parameters is incorrect")
                    }
                } else {
                    call.respondText("Mode parameter is empty")
                }
            } else {
                call.respondText("Password is empty")
            }
        } else {
            call.respondText("File is empty")
        }
    } else {
        call.respondText("File or password is empty")
    }
}

fun Route.magmaRouteDecoding() = post("/magma/decode") {
    val mode = call.parameters["mode"]
    val threads = call.parameters["threads"]
    val multiPart = try {
        call.receiveNullable<MultiPartData>()
    } catch (e: Exception) {
        println(e.message)
        null
    }
    if (multiPart != null) {
        val (fileBytes, password) = receiveFileAndPasswordPartData(multiPart)
        if (fileBytes.isNotEmpty()) {
            if (!password.isNullOrEmpty()) {
                if (mode != null || threads != null) {
                    val result: ByteArray? = when (mode) {
                        "default" -> decodeByDefaultMode(fileBytes, threads, password)
                        "steganography" -> decodeBySteganographyMode(fileBytes, threads, password)
                        else -> null
                    }
                    if (result != null) {
                        call.respondBytes(result)
                    } else {
                        call.respondText("One of parameters is incorrect")
                    }
                } else {
                    call.respondText("Mode parameter is empty")
                }
            } else {
                call.respondText("Password is empty")
            }
        } else {
            call.respondText("File is empty")
        }
    } else {
        call.respondText("File or password is empty")
    }
}