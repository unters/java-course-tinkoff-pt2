--liquibase formatted sql
--changeset unters:1

CREATE TYPE chat_status
AS ENUM ('AWAITING_COMMAND', 'AWAITING_URL_TO_TRACK', 'AWAITING_URL_TO_UNTRACK');

CREATE CAST (varchar AS chat_status) WITH INOUT AS IMPLICIT;

CREATE TABLE chat (
    id              bigint          PRIMARY KEY,
    status          chat_status     NOT NULL DEFAULT 'AWAITING_COMMAND'
);

CREATE TABLE tracking (
    id              serial          PRIMARY KEY,
    chat_id         bigint          REFERENCES chat(id),
    updated_at      timestamp       NOT NULL DEFAULT now(),
    url             varchar(512)    NOT NULL
);

ALTER TABLE tracking
ADD CONSTRAINT unique_chat_urls UNIQUE (url, chat_id);
