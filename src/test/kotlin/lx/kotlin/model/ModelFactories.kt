package lx.kotlin.model

import java.util.UUID

internal fun location(locationId: LocationId? = null): InstallationLocation {
    return InstallationLocation(locationId ?: UUID.randomUUID().toString(), "clientId", PhysicalLocation(0.0, 0.0, 0.0))
}

internal object Sensors {
    fun humidity(id: String? = null) = HumiditySensor(id ?: UUID.randomUUID().toString())
    fun temperature(id: String? = null) = TemperatureSensor(id ?: UUID.randomUUID().toString())
    fun carbonMonoxide(id: String? = null) = CarbonMonoxideSensor(id ?: UUID.randomUUID().toString())
}