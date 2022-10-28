CREATE TYPE shift_type AS ENUM (
    's1',
    's2',
    's3'
);

CREATE TABLE Worker
(
    "id"      uuid PRIMARY KEY,
    "name"    varchar,
    "surname" varchar
);

CREATE TABLE Shift_History
(
    "id"          uuid PRIMARY KEY,
    "worker_id"   uuid references Worker (id),
    "shift"       shift_type,
    "worked_date" date
);