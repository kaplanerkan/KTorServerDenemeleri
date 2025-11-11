package com.lotus.ktorserver.network.routes

import com.lotus.ktorserver.db.AppDatabase
import com.lotus.ktorserver.models.ApiResponse
import com.lotus.ktorserver.models.Urun
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.urunUpdateRoute(database: AppDatabase) {
    post("/update") {
        try {
            val gelen = call.receive<Urun>()

            if (gelen.id <= 0) {
                call.respond(ApiResponse(success = false, error = "Geçerli bir 'id' gerekli"))
                return@post
            }

            val mevcut = database.urunDao().getUrunById(gelen.id)
                ?: run {
                    call.respond(
                        ApiResponse(
                            success = false,
                            error = "Ürün bulunamadı: ${gelen.id}"
                        )
                    )
                    return@post
                }

            val guncellenmis = mevcut.copy(
                urunIsmi = gelen.urunIsmi,
                fiyati = gelen.fiyati
            )

            database.urunDao().upsertUrun(guncellenmis)

            call.respond(
                ApiResponse(
                    success = true,
                    message = "Güncellendi",
                    urun = guncellenmis
                )
            )

        } catch (ex: Exception) {
            call.respond(ApiResponse(success = false, error = ex.localizedMessage))
        }
    }
}