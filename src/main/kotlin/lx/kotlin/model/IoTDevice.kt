package lx.kotlin.model

typealias IoTDeviceId = String

enum class DeviceStatus {
    Available,
    Configured,
    Active,
    Inactive
}

data class IoTDevice(
    val deviceId: IoTDeviceId,
    val label: String,
    val status: DeviceStatus,
    val installedAt: InstallationLocation,
    val sensors: List<Sensor>
) {

    fun activate(): IoTDevice {
        require(status == DeviceStatus.Configured) { "A nice error message" }
        return this.copy(status = DeviceStatus.Active)
    }

    fun deactivate(): IoTDevice {
        require(status == DeviceStatus.Active)
        return this.copy(status = DeviceStatus.Inactive)
    }
}
