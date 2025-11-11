package com.lotus.ktorserver.network.routes

import com.lotus.ktorserver.db.AppDatabase
import com.lotus.ktorserver.models.ApiResponse
import io.ktor.server.application.Application
import io.ktor.server.request.receiveText
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.serialization.Serializable

@Serializable
data class DeleteRequest(val id: Int)

fun Application.configureUrunDeleteRoute(database: AppDatabase) {
    routing {
        post("/urun/sil") {
            try {
                // EN SADE VE EN GARANTİ YOL – TEXT OLARAK AL, ELLE PARSE ET
                val body = call.receiveText()
                // {"id":15} formatını yakala
                val idMatch = """"id"\s*:\s*(\d+)""".toRegex().find(body)
                val id = idMatch?.groupValues?.get(1)?.toIntOrNull() ?: 0

                if (id <= 0) {
                    call.respond(
                        ApiResponse(
                            success = false,
                            error = "Geçerli ID gerekli",
                            urun = null
                        )
                    )
                    return@post
                }

                val silinen = database.urunDao().deleteById(id)

                call.respond(
                    ApiResponse(
                        success = silinen > 0,
                        message = if (silinen > 0) "Ürün silindi (ID: $id)" else "Ürün bulunamadı",
                        error = if (silinen == 0) "Ürün bulunamadı" else null
                    )
                )

            } catch (ex: Exception) {
                call.respond(ApiResponse(success = false, error = "Hata: ${ex.localizedMessage}"))
            }
        }
    }
}