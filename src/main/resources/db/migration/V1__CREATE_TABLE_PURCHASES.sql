CREATE TABLE purchases
(
    id               INTEGER PRIMARY KEY,
    amount           DOUBLE      NOT NULL,
    currency         VARCHAR(50) NOT NULL,
    currency_country VARCHAR(50) NOT NULL,
    description      VARCHAR(50) NOT NULL,
    transaction_date DATETIME    NOT NULL,
    creation_date    DATETIME    NOT NULL
);

CREATE SEQUENCE "PURCHASES_SEQ"
    MINVALUE 1
    MAXVALUE 999999999
    INCREMENT BY 1
    START WITH 1;