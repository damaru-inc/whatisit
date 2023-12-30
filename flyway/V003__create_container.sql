-- Table: public.container

-- DROP TABLE IF EXISTS public.container;

CREATE TABLE IF NOT EXISTS public.container
(
    id bigint NOT NULL,
    room_id bigint references room,
    name character varying(25) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT container_pkey PRIMARY KEY (id),
    CONSTRAINT container_unique_room_name UNIQUE (room_id, name)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.container
    OWNER to jboss;

