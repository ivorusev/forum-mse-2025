-- Table: public.topic_entity

-- DROP TABLE IF EXISTS public.topic_entity;

CREATE TABLE IF NOT EXISTS public.topic_entity
(
    created_on timestamp(6) without time zone,
    id bigint NOT NULL,
    modified_on timestamp(6) without time zone,
    title character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT topic_entity_pkey PRIMARY KEY (id),
    CONSTRAINT topic_entity_title_key UNIQUE (title)
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.topic_entity
    OWNER to admin;

-- SEQUENCE: public.topic_entity_seq

-- DROP SEQUENCE IF EXISTS public.topic_entity_seq;

CREATE SEQUENCE IF NOT EXISTS public.topic_entity_seq
    INCREMENT 50
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE public.topic_entity_seq
    OWNER TO admin;