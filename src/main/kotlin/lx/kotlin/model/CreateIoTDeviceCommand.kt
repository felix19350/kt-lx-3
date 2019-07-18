package lx.kotlin.model

data class CreateIoTDeviceCommand(val label: String, val installationLocationId: LocationId, val sensors: List<Sensor>)