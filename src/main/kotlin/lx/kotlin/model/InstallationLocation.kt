package lx.kotlin.model

typealias LocationId = String
typealias ClientId = String

data class PhysicalLocation(
    val lat: Double,
    val lon: Double,
    val altitudeMeters: Double,
    val floor: String? = null,
    val room: String? = null,
    val notes: String? = null
)

data class InstallationLocation(
    val locationId: LocationId,
    val clientId: ClientId,
    val physicalLocation: PhysicalLocation
)