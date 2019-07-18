package lx.kotlin.infra.persistence

import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.cql.Row
import com.datastax.oss.driver.api.querybuilder.QueryBuilder
import com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import lx.kotlin.infra.json.JsonSettings
import lx.kotlin.model.DeviceStatus
import lx.kotlin.model.IoTDevice
import lx.kotlin.model.IoTDeviceId
import lx.kotlin.model.Sensor
import lx.kotlin.service.InstallationLocationRepository
import lx.kotlin.service.IoTDeviceRepository
import java.util.UUID

class CassandraIoTDeviceRepository(
    private val session: CqlSession,
    private val locationRepo: InstallationLocationRepository
) : IoTDeviceRepository {
    override fun save(device: IoTDevice) {
        val query = QueryBuilder.insertInto(IoTDeviceTable.tableName)
            .value(IoTDeviceTable.deviceId, QueryBuilder.bindMarker())
            .value(IoTDeviceTable.locationId, QueryBuilder.bindMarker())
            .value(IoTDeviceTable.label, QueryBuilder.bindMarker())
            .value(IoTDeviceTable.status, QueryBuilder.bindMarker())
            .value(IoTDeviceTable.sensors, QueryBuilder.bindMarker())

        val statement = query.builder().addPositionalValues(
            UUID.fromString(device.deviceId),
            UUID.fromString(device.installedAt.locationId),
            device.label,
            device.status.name,
            device.sensors.map { JsonSettings.mapper.writeValueAsString(it) }
        ).build()

        session.execute(statement)
    }

    override fun get(deviceId: IoTDeviceId): IoTDevice? {
        val query = QueryBuilder.selectFrom(IoTDeviceTable.tableName).all()
            .whereColumn(IoTDeviceTable.deviceId).isEqualTo(literal(UUID.fromString(deviceId)))
            .build()

        val results = session.execute(query)
        val row = results.firstOrNull()
        println(row)
        return row?.let{ toIoTDevice(it) }
    }

    private fun toIoTDevice(row: Row): IoTDevice {
        val locationId = row.getUuid(IoTDeviceTable.locationId)
        requireNotNull(locationId)
        val location = runBlocking(Dispatchers.IO) {
            locationRepo.get(locationId.toString())
        }

        val statusName = row.getString(IoTDeviceTable.status)
        requireNotNull(statusName)
        val status = DeviceStatus.valueOf(statusName)

        val jsonSensors: List<String>? = row.getList(IoTDeviceTable.sensors, String::class.java)
        requireNotNull(jsonSensors)
        val sensors = jsonSensors.map { JsonSettings.mapper.readValue<Sensor>(it) }

        return IoTDevice(
            deviceId = row.getUuid(IoTDeviceTable.deviceId).toString(),
            installedAt = location,
            label = row.getString(IoTDeviceTable.label)!!,
            status = status,
            sensors = sensors
        )
    }
}