-- Table: public.reply_entity

-- DROP TABLE IF EXISTS public.reply_entity;

CREATE TABLE IF NOT EXISTS public.reply_entity
(
    created_on timestamp(6) without time zone,
    id bigint NOT NULL,
    modified_on timestamp(6) without time zone,
    topic_id bigint,
    user_id bigint,
    reply_body character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT reply_entity_pkey PRIMARY KEY (id)
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.reply_entity
    OWNER to admin;

-- SEQUENCE: public.reply_entity_seq

-- DROP SEQUENCE IF EXISTS public.reply_entity_seq;

CREATE SEQUENCE IF NOT EXISTS public.reply_entity_seq
    INCREMENT 50
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE public.reply_entity_seq
    OWNER TO admin;