package com.lotus.ktorserver.network.routes

import com.lotus.ktorserver.db.AppDatabase
import com.lotus.ktorserver.models.ApiResponse
import com.lotus.ktorserver.models.Urun
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.urunEkleRoute(database: AppDatabase) {
    routing {
        post("/urunekle") {
            try {
                val urun = call.receive<Urun>()
                database.urunDao().upsertUrun(urun)
                call.respond(
                    ApiResponse(
                        success = true,
                        message = "Ürün kaydedildi",
                        urun = urun,
                        error = "0"
                    )
                )
            } catch (e: Exception) {
                call.respond(
                    ApiResponse(
                        success = false,
                        error = e.localizedMessage
                    )
                )
            }
        }
    }
}