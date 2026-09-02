CREATE TABLE maintenance_records (
                                     id BIGSERIAL PRIMARY KEY,
                                     vehicle_id BIGINT NOT NULL,
                                     service_date DATE NOT NULL,
                                     description VARCHAR(500) NOT NULL,
                                     cost NUMERIC(12, 2) NOT NULL,
                                     next_service_date DATE,
                                     status VARCHAR(30) NOT NULL,
                                     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                     CONSTRAINT fk_maintenance_records_vehicle
                                         FOREIGN KEY (vehicle_id)
                                             REFERENCES vehicles (id)
                                             ON DELETE CASCADE
);