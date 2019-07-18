package lx.kotlin.model

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import lx.kotlin.infra.json.SensorDeserializer
import lx.kotlin.infra.json.SensorSerializer

typealias SensorId = String

@JsonSerialize(using = SensorSerializer::class)
@JsonDeserialize(using = SensorDeserializer::class)
sealed class Sensor

data class TemperatureSensor(val id: SensorId) : Sensor() {

    fun readTemperature(): Reading {
        return Reading.empty(id)
    }
}

data class HumiditySensor(val id: SensorId) : Sensor() {
    fun readHumidity(): Reading {
        return Reading.empty(id)
    }
}

data class CarbonMonoxideSensor(val id: SensorId) : Sensor() {
    fun readCO(): Reading {
        return Reading.empty(id)
    }
}

