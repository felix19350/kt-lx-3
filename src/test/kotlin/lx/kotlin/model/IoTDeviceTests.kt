package lx.kotlin.model

import org.amshove.kluent.`should be`
import org.amshove.kluent.`should equal`
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.EnumSource

@Tag("Fast")
@DisplayName("IoTDevice tests")
class IoTDeviceTests {

    @Nested
    @DisplayName("Tests the device activation process and conditions")
    inner class DeviceActivationProcessTests {

        @Test
        fun `Activation succeeds with state Configured`() {
            // Given a device
            val device =
                IoTDevice("my-device", "A test device", DeviceStatus.Configured, location(), listOf(Sensors.humidity()))

            // When the activate method is called
            val updatedState = device.activate()

            // Then
            updatedState.status `should be` DeviceStatus.Active
        }

        @ParameterizedTest
        @EnumSource(value = DeviceStatus::class, mode = EnumSource.Mode.EXCLUDE, names = ["Configured"])
        fun `Activation fails on the other states`(status: DeviceStatus) {
            // Given a device
            val device = IoTDevice("my-device", "A test device", status, location(), listOf(Sensors.carbonMonoxide()))

            // Expect: IllegalStateException
            val exception = assertThrows<IllegalArgumentException> { device.activate() }
            exception.message `should equal` "A nice error message"
        }

        @ParameterizedTest
        @CsvSource("Inactive", "Active", "Available")
        fun `Activation fails on the other states`(statusName: String) {
            // Given a device
            val status = DeviceStatus.valueOf(statusName)
            val device = IoTDevice(
                "my-device",
                "A test device",
                status,
                location(),
                listOf(Sensors.carbonMonoxide())
            )

            // Expect: IllegalStateException
            assertThrows<IllegalArgumentException> { device.activate() }
        }
    }

    @Nested
    @DisplayName("Tests the device deactivation process and conditions")
    inner class DeviceDeactivationProcessTests {
        @Test
        fun `Deactivation succeeds with state Active`() {
            // Given a device
            val device =
                IoTDevice("my-device", "A test device", DeviceStatus.Active, location(), listOf(Sensors.temperature()))

            // When the activate method is called
            val updatedState = device.deactivate()

            // Then
            updatedState.status `should be` DeviceStatus.Inactive
        }
    }
}