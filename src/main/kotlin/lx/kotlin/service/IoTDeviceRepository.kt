package lx.kotlin.service

import lx.kotlin.model.IoTDevice
import lx.kotlin.model.IoTDeviceId

interface IoTDeviceRepository {

    fun save(device: IoTDevice)

    fun get(deviceId: IoTDeviceId): IoTDevice?
}