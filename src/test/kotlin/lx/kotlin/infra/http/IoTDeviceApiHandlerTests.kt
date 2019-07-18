package lx.kotlin.infra.http

import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import lx.kotlin.infra.json.JsonSettings
import lx.kotlin.model.CreateIoTDeviceCommand
import lx.kotlin.model.IoTDevice
import lx.kotlin.model.Sensors
import lx.kotlin.service.IoTDeviceServiceImpl
import lx.kotlin.service.IotDeviceService
import lx.kotlin.service.deviceRepo
import lx.kotlin.service.locationRepo
import org.amshove.kluent.`should contain same`
import org.amshove.kluent.`should equal`
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.UUID

class IoTDeviceApiHandlerTests {

    private val service = IoTDeviceServiceImpl(
        deviceRepo(),
        locationRepo()
    )

    private val mapper = JsonSettings.mapper

    @Nested
    @DisplayName("IoT Device creation API")
    inner class CreateIoTDeviceTests {

        /*@Test
        fun `Successful creation of an IoT Device`(): Unit = withTestApplication({
            testModule(service)
        }, {
            val cmd = CreateIoTDeviceCommand(
                label = "A new device",
                installationLocationId = UUID.randomUUID().toString(),
                sensors = listOf(Sensors.carbonMonoxide())
            )
            with(handleRequest(HttpMethod.Post, "/device") {
                setBody(mapper.writeValueAsString(cmd))
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            }) {
                response.status() `should equal` HttpStatusCode.Created
                val receivedDevice: IoTDevice = mapper.readValue(response.content!!)
                receivedDevice.label `should equal` cmd.label
                receivedDevice.sensors `should contain same` cmd.sensors
                receivedDevice.installedAt.locationId `should equal` cmd.installationLocationId
            }
        })*/

        @Test
        fun `Successful creation of an IoT Device`(): Unit = testWith(service) {
            // Given a create device command
            val cmd = CreateIoTDeviceCommand(
                label = "A new device",
                installationLocationId = UUID.randomUUID().toString(),
                sensors = listOf(Sensors.carbonMonoxide())
            )

            // When the endpoint is invoked with the command
            // serialized as JSON and a content type of 'application/json'
            with(handleRequest(HttpMethod.Post, "/device") {
                setBody(mapper.writeValueAsString(cmd))
                addHeader(
                    HttpHeaders.ContentType,
                    ContentType.Application.Json.toString()
                )
            }) {
                //Then we get a 201 as well as a new IoTDevice as JSON
                response.status() `should equal` HttpStatusCode.Created
                val receivedDevice: IoTDevice = mapper.readValue(response.content!!)
                with(receivedDevice) {
                    label `should equal` cmd.label
                    sensors `should contain same` cmd.sensors
                    installedAt.locationId `should equal` cmd.installationLocationId
                }
            }
        }
    }

    fun <R> testWith(service: IotDeviceService, body: TestApplicationEngine.() -> R) = withTestApplication({
        testModule(service)
    }, body)
}