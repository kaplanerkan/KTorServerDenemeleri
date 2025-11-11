package com.lotus.ktorserver.network.routes

import com.lotus.ktorserver.db.AppDatabase
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

// BURAYA EKLE — BU SATIRLAR OLMAZSA OLMAZ!
fun Application.statusRoutes() = routing { configureStatusRoutes() }
fun Application.configureUrunEkleRoutes(database: AppDatabase) = routing { urunEkleRoute(database) }
fun Application.configureUrunListRoutes(database: AppDatabase) = routing { urunListRoute(database) }

fun Application.urunUpdateRoute(database: AppDatabase) = routing { urunUpdateRoute(database) }

fun Application.installUrunActiveRoute(database: AppDatabase) = routing { installUrunActiveRoutes(database) }

fun Application.installUrunDeleteRoutes(database: AppDatabase) = routing { configureUrunDeleteRoute(database) }

