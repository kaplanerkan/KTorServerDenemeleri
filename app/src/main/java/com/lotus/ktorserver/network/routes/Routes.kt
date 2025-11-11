package com.lotus.ktorserver.network.routes

import com.lotus.ktorserver.db.AppDatabase
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

// BURAYA EKLE — BU SATIRLAR OLMAZSA OLMAZ!
fun Application.routingStatus() = routing { statusRoutes() }
fun Application.routingUrunEkle(database: AppDatabase) = routing { urunEkleRoute(database) }
fun Application.routingUrunList(database: AppDatabase) = routing { urunListRoute(database) }
fun Application.routingUrunUpdate(database: AppDatabase) = routing { urunUpdateRoute(database) }
fun Application.routingUrunActive(database: AppDatabase) = routing { urunActiveRoutes(database) }
fun Application.routingUrunDelete(database: AppDatabase) = routing { urunDeleteRoute(database) }

