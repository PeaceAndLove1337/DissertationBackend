package ru.mpei.pavlovis.plugins.routing

import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.mpei.pavlovis.domain.getHashByDefaultMode
import ru.mpei.pavlovis.domain.getHashBySteganographyMode

fun Route.streebogRoute() =
    post("/streebog") {
        val length = call.parameters["length"]
        val mode = call.parameters["mode"]
        val multiPart = try {
            call.receiveNullable<MultiPartData>()
        } catch (e: Exception) {
            println(e.message)
            null
        }
        if (multiPart != null) {
            val fileBytes = receiveFilePartData(multiPart)
            if (fileBytes.isNotEmpty()) {
                if (mode != null || length != null) {
                    val result: ByteArray? = when (mode) {
                        "default" -> getHashByDefaultMode(fileBytes, length)
                        "steganography" -> getHashBySteganographyMode(fileBytes, length)
                        else -> null
                    }
                    if (result != null) {
                        call.respondBytes(result)
                    } else {
                        call.respondText("One of parameters is incorrect")
                    }
                } else {
                    call.respondText("One of parameters is empty!")
                }
            } else {
                call.respondText("File is empty")
            }
        } else {
            call.respondText("File is empty")
        }
    }
