package com.lotus.ktorserver.network

import android.util.Log
import com.lotus.ktorserver.db.AppDatabase
import com.lotus.ktorserver.network.routes.configureUrunEkleRoutes
import com.lotus.ktorserver.network.routes.configureUrunListRoutes
import com.lotus.ktorserver.network.routes.installUrunActiveRoute
import com.lotus.ktorserver.network.routes.installUrunDeleteRoutes
import com.lotus.ktorserver.network.routes.statusRoutes
import com.lotus.ktorserver.network.routes.urunUpdateRoute
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS


/***
 * Kullanimi:
curl -X POST http://192.168.1.140:6868/update `
-H "Content-Type: application/json" `
-d "{\"id\": 55, \"urunIsmi\": \"iPhone 16 Pro\", \"fiyati\": 45990.0}"


curl -X POST http://192.168.1.140:6868/urunekle `
-H "Content-Type: application/json" `
-d "{\"id\": 15, \"urunIsmi\": \"AirPods Pro 2\", \"fiyati\": 5499.90}"

 *
 *
 *
 */

object KtorServer {


    // BU SATIR KRİTİK – TEngine, TConfiguration ile generic olmalı!
    private var server: EmbeddedServer<*, *>? = null

    fun startServer(database: AppDatabase) {
        if (server != null) return

        // BU ŞEKİLDE YAZILMALI → .start() dışarıda, apply içinde değil!
        server = embeddedServer(Netty, port = 6868, host = "0.0.0.0") {
            install(ContentNegotiation) { json() }
            install(CORS) { anyHost() }


            statusRoutes()
            configureUrunListRoutes(database)
            configureUrunEkleRoutes(database)
            urunUpdateRoute(database)
            installUrunActiveRoute(database)
            installUrunDeleteRoutes(database)

        }.start(wait = false)



        Log.d("KtorServer", "Server BAŞARIYLA ÇALIŞIYOR → http://192.168.1.140:6868")
    }


    fun startServer2(database: AppDatabase) {
        if (server != null) return

        server = embeddedServer(Netty, port = 6868, host = "0.0.0.0") {
            install(ContentNegotiation) {
                json()
            }
            install(CORS) {
                anyHost() // Geliştirme için, production'da kısıtla
            }


//            routing {

            // TÜM ROUTE’LARI BURAYA EKLE


//                get("/getUrunler") {
//                        try {
//                            val tumUrunler = database.urunDao().getAllUrunler() // aşağıda dao'ya ekleyeceğiz
//
//                            // Room'dan gelen listeyi JSON'a çeviriyoruz (Ktor otomatik serialization yapabiliyor ama en basit haliyle)
//                            call.respond(tumUrunler)
//                        } catch (e: Exception) {
//                            call.respondText("Veritabanı hatası: ${e.message}", status = io.ktor.http.HttpStatusCode.InternalServerError)
//                        }
//                }
//                get("/status") {
//                    call.respondText("ONLINE")
//                    // JSON döndürmek icin
////                    call.respond(mapOf(
////                        "status" to "ONLINE",
////                        "server" to "Ktor-Android",
////                        "port" to 6868,
////                        "time" to System.currentTimeMillis()
////                    ))
//                }
//
//                get("/update/{urunId}") {
//                    val urunId = call.parameters["urunId"]?.toIntOrNull()
//                    if (urunId == null) {
//                        call.respondText("Hatalı ID")
//                        return@get
//                    }
//
//                    // Örnek olarak: ID + 100 olsun yeni fiyat, isim de "Güncellendi - $id"
//                    val yeniIsim = "Güncellendi - $urunId"
//                    val yeniFiyat = (urunId + 100).toDouble()
//
//                    CoroutineScope(Dispatchers.IO).launch {
//                        try {
//                            val etkilenenSatir = database.urunDao()
//                                .updateUrun(urunId, yeniIsim, yeniFiyat)
//
//                            if (etkilenenSatir > 0) {
//                                Log.d("KtorServer", "Ürün $urunId başarıyla güncellendi")
//                            } else {
//                                // Ürün yoksa ekleyelim (opsiyonel)
//                                database.urunDao().upsertUrun(
//                                    Urun(
//                                        id = urunId,
//                                        urunIsmi = yeniIsim,
//                                        fiyati = yeniFiyat
//                                    )
//                                )
//                                Log.d("KtorServer", "Yeni ürün eklendi: $urunId")
//                            }
//                        } catch (e: Exception) {
//                            Log.e("KtorServer", "Hata: ${e.message}")
//                        }
//                    }
//
//                    val urun = Urun(urunId, yeniIsim, yeniFiyat)
//                    call.respond(urun)
//                }
//
//                post("/update") {
//                    try {
//                        // Gelen JSON'dan sadece dolu alanları alacağız (patch tarzı)
//                        val gelen = call.receive<Urun>()
//
//                        // ID zorunlu olmalı, yoksa hata ver
//                        if (gelen.id <= 0) {
//                            call.respond(
//                                ApiResponse(
//                                    success = false,
//                                    error = "Güncelleme için geçerli bir 'id' gereklidir."
//                                )
//                            )
//                            return@post
//                        }
//
//                        // Veritablosunda bu ID var mı kontrol et (isteğe bağlı ama güzel olur)
//                        val mevcutUrun = database.urunDao().getUrunById(gelen.id)
//                            ?: run {
//                                call.respond(
//                                    ApiResponse(
//                                        success = false,
//                                        error = "ID'si ${gelen.id} olan ürün bulunamadı."
//                                    )
//                                )
//                                return@post
//                            }
//
//                        // Sadece gelen alanları güncelle (patch mantığı)
//                        val guncellenmisUrun = mevcutUrun.copy(
//                            urunIsmi = gelen.urunIsmi,
//                            fiyati = gelen.fiyati
//                        )
//
//                        database.urunDao().upsertUrun(guncellenmisUrun)
//
//                        call.respond(
//                            ApiResponse(
//                                success = true,
//                                message = "Ürün başarıyla güncellendi",
//                                urun = guncellenmisUrun
//                            )
//                        )
//
//                    } catch (ex: Exception) {
//                        call.respond(
//                            ApiResponse(
//                                success = false,
//                                error = ex.localizedMessage ?: "Bilinmeyen hata"
//                            )
//                        )
//                    }
//                }
//
//
//                post("/urunekle") {
//                    try {
//                        val gelenUrun = call.receive<Urun>()
//                        database.urunDao().upsertUrun(gelenUrun)
//
//                        call.respond(
//                            ApiResponse(
//                                success = true,
//                                message = "Ürün başarıyla kaydedildi",
//                                urun = gelenUrun,
//                                error = "0"
//                            )
//                        )
//                    } catch (e: Exception) {
//                        call.respond(
//                            ApiResponse(
//                                success = false,
//                                error = e.localizedMessage ?: "Bilinmeyen hata"
//                            )
//                        )
//                    }
//                }
//
//                get("/") {
//                    call.respondText("Ktor server çalışıyor! Kullanım: /update/5")
//                }


//            }


        }.start(wait = false)

        Log.d("KtorServer", "Server başlatıldı → http://192.168.1.180:6868")
    }

    fun stopServer() {
        server?.stop(1000, 5000)
        server = null
        Log.d("KtorServer", "Server durduruldu")
    }
}
