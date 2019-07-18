package lx.kotlin.model

import java.time.Instant

data class Reading(val sensorId: SensorId, val label: String, val value: Number, val timestamp: Instant) {

    companion object {
        fun empty(sensorId: SensorId): Reading {
            return Reading(sensorId, "", 0, Instant.now())
        }
    }
}