package lx.kotlin.infra.persistence

import com.datastax.oss.driver.api.core.CqlSession
import kotlinx.coroutines.runBlocking
import lx.kotlin.model.DeviceStatus
import lx.kotlin.model.IoTDevice
import lx.kotlin.model.Sensors
import lx.kotlin.service.locationRepo
import org.amshove.kluent.`should equal`
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Testcontainers
import java.net.InetSocketAddress
import java.util.UUID

@Tag("Slow")
@Testcontainers
@DisplayName("Cassandra IoT Device repository integration test")
class CassandraIoTDeviceRepositoryTests {

    private val cassandra = CassandraTestContainer.instance

    private val containerHost = InetSocketAddress(
        cassandra.containerIpAddress,
        cassandra.getMappedPort(9042)
    )

    private val session by lazy {
        CqlSession.builder()
            .withLocalDatacenter("datacenter1")
            .addContactPoint(containerHost)
            .withKeyspace("kt_lx")
            .build()
    }

    private val locationRepo = locationRepo()

    @Test
    fun `An IoTDevice can be successfully persisted`() {
        // Given a correctly configured repo
        val repo = CassandraIoTDeviceRepository(session, locationRepo)
        val location = runBlocking { locationRepo.get(UUID.randomUUID().toString()) }

        // And an IoTDevice to persist
        val originalDevice = IoTDevice(
            UUID.randomUUID().toString(),
            "A test device",
            DeviceStatus.Available,
            location,
            listOf(Sensors.humidity())
        )

        // When the save method is called on the repo
        repo.save(originalDevice)

        // The said IoTDevice is persisted, that is,
        // we can fetch it from the database
        val storedDevice = repo.get(originalDevice.deviceId)
        storedDevice `should equal` originalDevice
    }

    /*@Test
    fun `Alpine test`() {
        val server = AlpineHttpServer.instance
        val address = InetSocketAddress(
            server.containerIpAddress,
            server.getMappedPort(80)
        )
        val socket = Socket()
        socket.connect(address)
    }*/
}