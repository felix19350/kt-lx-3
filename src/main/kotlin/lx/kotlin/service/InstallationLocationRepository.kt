package lx.kotlin.service

import lx.kotlin.model.InstallationLocation
import lx.kotlin.model.LocationId

interface InstallationLocationRepository {
    suspend fun get(id: LocationId): InstallationLocation
}