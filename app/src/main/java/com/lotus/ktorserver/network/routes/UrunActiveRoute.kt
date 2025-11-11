package com.lotus.ktorserver.network.routes

import com.lotus.ktorserver.db.AppDatabase
import com.lotus.ktorserver.models.ApiResponse
import io.ktor.server.application.Application

import io.ktor.server.request.receive
import io.ktor.server.response.respond


import io.ktor.server.routing.post
import io.ktor.server.routing.routing

// Gelen istek için basit bir request modeli
@kotlinx.serialization.Serializable
data class ActiveRequest(
    val id: Int,
    val active: Boolean
)

fun Application.installUrunActiveRoutes(database: AppDatabase) {
    routing {
        post("/urun/active") {
            try {
                val request = call.receive<ActiveRequest>()

                if (request.id <= 0) {
                    call.respond(
                        ApiResponse(success = false, error = "Geçerli bir 'id' gerekli")
                    )
                    return@post
                }

                // Önce ürün var mı kontrol et
                val urun = database.urunDao().getUrunById(request.id)
                    ?: run {
                        call.respond(
                            ApiResponse(success = false, error = "Ürün bulunamadı: ${request.id}")
                        )
                        return@post
                    }

                // Sadece active alanını güncelle
                val guncellenmis = urun.copy(active = request.active)
                database.urunDao().upsertUrun(guncellenmis)

                call.respond(
                    ApiResponse(
                        success = true,
                        message = if (request.active) "Ürün aktif edildi" else "Ürün pasif hale getirildi",
                        urun = guncellenmis
                    )
                )

            } catch (ex: Exception) {
                call.respond(
                    ApiResponse(success = false, error = ex.localizedMessage ?: "Bilinmeyen hata")
                )
            }
        }
    }
}