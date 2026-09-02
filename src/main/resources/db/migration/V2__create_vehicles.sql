CREATE TABLE vehicles (
                          id BIGSERIAL PRIMARY KEY,
                          make VARCHAR(100) NOT NULL,
                          model VARCHAR(100) NOT NULL,
                          year INTEGER NOT NULL,
                          license_plate VARCHAR(20) NOT NULL UNIQUE,
                          status VARCHAR(30) NOT NULL,
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);