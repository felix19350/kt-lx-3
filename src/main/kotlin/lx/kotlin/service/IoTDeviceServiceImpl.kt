package lx.kotlin.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import lx.kotlin.model.CreateIoTDeviceCommand
import lx.kotlin.model.DeviceStatus
import lx.kotlin.model.IoTDevice
import java.util.UUID

internal class IoTDeviceServiceImpl(
    private val deviceRepo: IoTDeviceRepository,
    private val locationRepo: InstallationLocationRepository
) : IotDeviceService {
    override fun createDevice(cmd: CreateIoTDeviceCommand): IoTDevice {

        val location = runBlocking(Dispatchers.IO) {
            locationRepo.get(cmd.installationLocationId)
        }

        val device = IoTDevice(
            deviceId = UUID.randomUUID().toString(),
            status = DeviceStatus.Available,
            installedAt = location,
            label = cmd.label,
            sensors = cmd.sensors
        )

        deviceRepo.save(device)
        return device
    }
}