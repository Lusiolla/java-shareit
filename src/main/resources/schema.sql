DROP TABLE IF EXISTS bookings CASCADE;

DROP TABLE IF EXISTS requests CASCADE;

DROP TABLE IF EXISTS items_comments CASCADE;

DROP TABLE IF EXISTS items CASCADE;

DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR(255)                            NOT NULL,
    email VARCHAR(512)                            NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS requests
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name        VARCHAR(255)                            NOT NULL,
    description VARCHAR(512)                            NOT NULL,
    user_id     BIGINT                                  NOT NULL,
    CONSTRAINT pk_request PRIMARY KEY (id),
    CONSTRAINT REQUESTS_TO_USERS_FK FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS items
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name         VARCHAR(255)                            NOT NULL,
    description  VARCHAR(512)                            NOT NULL,
    is_available BOOLEAN                                 NOT NULL,
    user_id      BIGINT                                  NOT NULL,
    request_id   BIGINT                                  NULL,
    CONSTRAINT pk_item PRIMARY KEY (id),
    CONSTRAINT ITEMS_TO_USERS_FK FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT ITEMS_TO_REQUEST_FK FOREIGN KEY (request_id) REFERENCES requests (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS bookings
(
    id            BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    start_booking TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    end_booking   TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    item_id       BIGINT                                  NOT NULL,
    user_id       BIGINT                                  NOT NULL,
    status        VARCHAR(255)                            NOT NULL,
    CONSTRAINT pk_booking PRIMARY KEY (id),
    CONSTRAINT BOOKINGS_TO_USERS_FK FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT BOOKINGS_TO_ITEMS_FK FOREIGN KEY (item_id) REFERENCES items (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS items_comments
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text      VARCHAR(512)                            NOT NULL,
    item_id   BIGINT                                  NOT NULL,
    author_id BIGINT                                  NOT NULL,
    created   TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    CONSTRAINT pk_items_comments PRIMARY KEY (id),
    CONSTRAINT UQ_ITEMS_COMMENTS UNIQUE (item_id, author_id),
    CONSTRAINT ITEMS_COMMENTS_TO_USERS_FK FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT ITEMS_COMMENTS_TO_ITEMS_FK FOREIGN KEY (item_id) REFERENCES items (id) ON DELETE CASCADE
);