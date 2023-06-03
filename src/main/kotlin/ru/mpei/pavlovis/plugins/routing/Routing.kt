package ru.mpei.pavlovis.plugins.routing

import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        streebogRoute()
        magmaRouteEncoding()
        magmaRouteDecoding()
        route("/hello"){
            get{
               call.respondText("ALOHA:D")
            }
        }
    }
}

suspend fun receiveFilePartData(multipartData: MultiPartData): ByteArray {
    var fileBytes: ByteArray? = null
    multipartData.forEachPart { part ->
        if (part is PartData.FileItem) {
            fileBytes = part.streamProvider().readBytes()
            part.dispose()
        }
    }
    return fileBytes ?: ByteArray(0)
}

suspend fun receiveFileAndPasswordPartData(multipartData: MultiPartData): Pair<ByteArray, String?> {
    var fileBytes: ByteArray? = null
    var password: String? = null
    multipartData.forEachPart { part ->
        when (part) {
            is PartData.FileItem -> {
                fileBytes = part.streamProvider().readBytes()
            }
            is PartData.FormItem -> {
                if (part.name == "password") {
                    password = part.value
                }
            }
            else -> {
                println("ERROR IN PARCING MultiPartData")
            }
        }
        part.dispose()
    }
    return Pair(fileBytes?: ByteArray(0), password)
}