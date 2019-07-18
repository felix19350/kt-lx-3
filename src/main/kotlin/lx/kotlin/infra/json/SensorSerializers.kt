package lx.kotlin.infra.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import lx.kotlin.model.CarbonMonoxideSensor
import lx.kotlin.model.HumiditySensor
import lx.kotlin.model.Sensor
import lx.kotlin.model.TemperatureSensor

internal class SensorDeserializer :
    StdDeserializer<Sensor>(Sensor::class.java) {
    override fun deserialize(jp: JsonParser?, ctxt: DeserializationContext?): Sensor {
        val node: JsonNode = jp!!.codec.readTree(jp)
        val type = node.get("type").textValue()

        return when (type) {
            "Humidity" -> HumiditySensor(node.get("id").textValue())
            "Temperature" -> TemperatureSensor(node.get("id").textValue())
            "CarbonMonoxide" -> CarbonMonoxideSensor(node.get("id").textValue())
            else -> throw IllegalArgumentException("Unknown sensor type $type.")
        }
    }
}

internal class SensorSerializer :
    StdSerializer<Sensor>(Sensor::class.java) {
    override fun serialize(value: Sensor?, gen: JsonGenerator?, provider: SerializerProvider?) {
        gen!!.writeStartObject()

        when (value) {
            is HumiditySensor -> {
                gen.writeStringField("id", value.id)
                gen.writeStringField("type", "Humidity")
            }
            is TemperatureSensor -> {
                gen.writeStringField("id", value.id)
                gen.writeStringField("type", "Temperature")
            }
            is CarbonMonoxideSensor -> {
                gen.writeStringField("id", value.id)
                gen.writeStringField("type", "CarbonMonoxide")
            }
        }
        gen.writeEndObject()
    }
}