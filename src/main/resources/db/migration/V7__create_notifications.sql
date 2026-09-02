CREATE TABLE notifications (
                               id BIGSERIAL PRIMARY KEY,

                               user_id BIGINT NOT NULL,
                               vehicle_id BIGINT,

                               type VARCHAR(50) NOT NULL,
                               message VARCHAR(500) NOT NULL,

                               read BOOLEAN NOT NULL DEFAULT FALSE,

                               created_at TIMESTAMP NOT NULL,

                               CONSTRAINT fk_notifications_user
                                   FOREIGN KEY (user_id)
                                       REFERENCES users (id),

                               CONSTRAINT fk_notifications_vehicle
                                   FOREIGN KEY (vehicle_id)
                                       REFERENCES vehicles (id)
);

CREATE INDEX idx_notifications_user_id
    ON notifications (user_id);

CREATE INDEX idx_notifications_vehicle_id
    ON notifications (vehicle_id);

CREATE INDEX idx_notifications_created_at
    ON notifications (created_at);