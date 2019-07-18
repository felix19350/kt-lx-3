create keyspace if not exists kt_lx with replication = {
    'class': 'SimpleStrategy', 'replication_factor': 1
    };

CREATE TABLE if not exists kt_lx.iot_device
(
    device_id   uuid,
    location_id uuid,
    label       text,
    status      text,
    sensors     list<text>,
    PRIMARY KEY (device_id)
)
