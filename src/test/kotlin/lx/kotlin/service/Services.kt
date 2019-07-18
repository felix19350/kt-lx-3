package lx.kotlin.service

import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import lx.kotlin.model.InstallationLocation
import lx.kotlin.model.LocationId
import lx.kotlin.model.PhysicalLocation
import org.mockito.ArgumentMatchers.any

fun locationRepo(): InstallationLocationRepository {
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

fun deviceRepo(): IoTDeviceRepository{
    val repo = mockk<IoTDeviceRepository>()
    every { repo.save(any()) } just Runs
    return repo
}