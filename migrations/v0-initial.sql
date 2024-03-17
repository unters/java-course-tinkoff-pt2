--liquibase formatted sql
--changeset unters:1

CREATE TYPE chat_status
AS ENUM ('awaiting_command', 'awaiting_url_to_track', 'awaiting_url_to_untrack');

CREATE TABLE chat (
    id              bigint      PRIMARY KEY,
    status          chat_status NOT NULL DEFAULT 'awaiting_command'
);

CREATE CAST (varchar AS chat_status) WITH INOUT AS IMPLICIT;

CREATE TABLE link (
    id              serial      PRIMARY KEY,
    url             bigint      NOT NULL,
    updated_at      timestamp   NOT NULL DEFAULT now(),
    chat_id         bigint      REFERENCES chat(id)
);

ALTER TABLE link
ADD CONSTRAINT unique_chat_urls UNIQUE (url, chat_id);
