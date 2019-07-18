package lx.kotlin.service

import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import lx.kotlin.model.CreateIoTDeviceCommand
import lx.kotlin.model.DeviceStatus
import lx.kotlin.model.InstallationLocation
import lx.kotlin.model.IoTDevice
import lx.kotlin.model.IoTDeviceId
import lx.kotlin.model.LocationId
import lx.kotlin.model.PhysicalLocation
import lx.kotlin.model.Sensors
import org.amshove.kluent.`should contain all`
import org.amshove.kluent.`should equal`
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@Tag("Slow-ish")
class IoTDeviceServiceImplTests {

    @Nested
    @DisplayName("Test the creation of an IoTDevice")
    inner class CreateIotDeviceTests {

        @Test
        fun `An IoTDevice is created when a valid command is provided`() {
            // Given a properly configured service
            val service = IoTDeviceServiceImpl(deviceRepo(), locationRepo())

            // When the create method is called with a correct command
            val sensors = listOf(Sensors.temperature(), Sensors.carbonMonoxide())
            val cmd = CreateIoTDeviceCommand("a test device", "equator", sensors)
            val result = service.createDevice(cmd)

            // Then: the method executes correctly
            result.status `should equal` DeviceStatus.Available
            result.sensors `should contain all` sensors
            result.label `should equal` "a test device"
        }

        @Test
        fun `When an invalid device id is requested an exception is thrown`() {
            // Given a properly configured service
            val repo = deviceRepo()

            // Expect: an IllegalArgumentException when an invalid id is called
            assertThrows<IllegalArgumentException> {
                repo.get("FORCE_ERROR")
            }
        }
    }

    private fun locationRepo(): InstallationLocationRepository {
        val repo = mockk<InstallationLocationRepository>()

        val stubLocation = InstallationLocation(
            locationId = "change",
            clientId = "Test Client",
            physicalLocation = PhysicalLocation(0.0, 0.0, 0.0)
        )

        val idSlot = slot<LocationId>()
        coEvery { repo.get(capture(idSlot)) } answers { stubLocation.copy(locationId = idSlot.captured) }
        return repo
    }

    private fun deviceRepo(): IoTDeviceRepository {
        val repo = mockk<IoTDeviceRepository>()

        every { repo.save(any()) } just Runs

        // every {repo.get(any())} returns IoTDevice(deviceId = "someId", label = "my label", installedAt = location(), status = DeviceStatus.Active, sensors = listOf())

        val id = slot<IoTDeviceId>()
        val device1 = IoTDevice(
            deviceId = "device1",
            label = "Device 1",
            installedAt = location(),
            status = DeviceStatus.Active,
            sensors = listOf(
                Sensors.humidity(), Sensors.temperature()
            )
        )
        val device2 = IoTDevice(
            deviceId = "device2",
            label = "Device 2",
            installedAt = location(),
            status = DeviceStatus.Active,
            sensors = listOf(
                Sensors.carbonMonoxide()
            )
        )
        val devices = mapOf(
            "device1" to device1,
            "device2" to device2
        )

        every { repo.get(capture(id)) } answers { if (id.isCaptured) devices[id.captured] else null }
        every { repo.get(cmpEq("FORCE_ERROR")) } throws IllegalArgumentException("Invalid id")

        return repo
    }

    private fun location(): InstallationLocation {
        return InstallationLocation("Somewhere", "clientId", PhysicalLocation(0.0, 0.0, 0.0))
    }
}