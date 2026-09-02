CREATE TABLE vehicle_locations (
                                   id BIGSERIAL PRIMARY KEY,
                                   vehicle_id BIGINT NOT NULL,
                                   latitude NUMERIC(10, 7) NOT NULL,
                                   longitude NUMERIC(10, 7) NOT NULL,
                                   recorded_at TIMESTAMP NOT NULL,

                                   CONSTRAINT fk_vehicle_locations_vehicle
                                       FOREIGN KEY (vehicle_id)
                                           REFERENCES vehicles(id)
                                           ON DELETE CASCADE
);

CREATE INDEX idx_vehicle_locations_vehicle_id
    ON vehicle_locations(vehicle_id);

CREATE INDEX idx_vehicle_locations_recorded_at
    ON vehicle_locations(recorded_at);