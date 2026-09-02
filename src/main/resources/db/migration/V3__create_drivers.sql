CREATE TABLE drivers (
                         id BIGSERIAL PRIMARY KEY,
                         first_name VARCHAR(100) NOT NULL,
                         last_name VARCHAR(100) NOT NULL,
                         email VARCHAR(255) NOT NULL UNIQUE,
                         phone VARCHAR(30) NOT NULL,
                         license_number VARCHAR(50) NOT NULL UNIQUE,
                         license_expiry_date DATE NOT NULL,
                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);