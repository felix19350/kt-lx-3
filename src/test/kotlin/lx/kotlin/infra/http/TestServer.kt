package lx.kotlin.infra.http

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.Compression
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.features.deflate
import io.ktor.features.gzip
import io.ktor.features.identity
import io.ktor.http.ContentType
import io.ktor.jackson.JacksonConverter
import io.ktor.routing.Routing
import lx.kotlin.infra.json.JsonSettings
import lx.kotlin.service.IotDeviceService
import org.slf4j.event.Level

fun Application.testModule(service: IotDeviceService) {

    install(DefaultHeaders)
    install(Compression) {
        gzip {
            priority = 100.0
        }
        identity {
            priority = 10.0
        }
        deflate {
            priority = 1.0
        }
    }
    install(CallLogging) { level = Level.INFO }
    install(ContentNegotiation) {
        register(ContentType.Application.Json, JacksonConverter(JsonSettings.mapper))
    }
    install(Routing) {
        iotDeviceApi(service)
    }
}