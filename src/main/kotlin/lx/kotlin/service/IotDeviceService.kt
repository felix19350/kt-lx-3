package lx.kotlin.service

import lx.kotlin.model.CreateIoTDeviceCommand
import lx.kotlin.model.IoTDevice

interface IotDeviceService {

    fun createDevice(cmd: CreateIoTDeviceCommand): IoTDevice
}