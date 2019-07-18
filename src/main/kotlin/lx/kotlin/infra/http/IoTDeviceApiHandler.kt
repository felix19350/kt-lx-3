package lx.kotlin.infra.http

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import lx.kotlin.model.CreateIoTDeviceCommand
import lx.kotlin.service.IotDeviceService

fun Route.iotDeviceApi(service: IotDeviceService) {

    post("/device"){
        val cmd: CreateIoTDeviceCommand = call.receive()
        val result = service.createDevice(cmd)
        call.respond(HttpStatusCode.Created, result)
    }

}