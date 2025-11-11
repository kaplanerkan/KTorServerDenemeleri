package com.lotus.ktorserver.network.routes

import com.lotus.ktorserver.db.AppDatabase
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.urunListRoute(database: AppDatabase) {
    get("/getUrunler") {
        try {
            val urunler = database.urunDao().getAllUrunler()
            call.respond(urunler)
        } catch (e: Exception) {
            call.respondText("DB Hatası: ${e.message}")
        }
    }
}