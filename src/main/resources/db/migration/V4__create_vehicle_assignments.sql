CREATE TABLE vehicle_assignments (
                                     id BIGSERIAL PRIMARY KEY,

                                     vehicle_id BIGINT NOT NULL,
                                     driver_id BIGINT NOT NULL,

                                     assigned_at TIMESTAMP NOT NULL,
                                     unassigned_at TIMESTAMP,

                                     CONSTRAINT fk_vehicle_assignments_vehicle
                                         FOREIGN KEY (vehicle_id)
                                             REFERENCES vehicles (id),

                                     CONSTRAINT fk_vehicle_assignments_driver
                                         FOREIGN KEY (driver_id)
                                             REFERENCES drivers (id)
);

CREATE INDEX idx_vehicle_assignments_vehicle_id
    ON vehicle_assignments (vehicle_id);

CREATE INDEX idx_vehicle_assignments_driver_id
    ON vehicle_assignments (driver_id);

CREATE INDEX idx_vehicle_assignments_assigned_at
    ON vehicle_assignments (assigned_at);