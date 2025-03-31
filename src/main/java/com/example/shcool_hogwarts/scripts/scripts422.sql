CREATE TABLE car
(
    Id    SERIAL PRIMARY KEY,
    Make  varchar NOT NULL,
    Model VARCHAR NOT NULL,
    Price INTEGER check (Price > 0)
);
CREATE TABLE people
(
    Name    varchar PRIMARY KEY,
    Age     INTEGER CHECK (Age > 0),
    License BOOLEAN
);