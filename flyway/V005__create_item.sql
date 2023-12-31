-- Table: public.item

-- DROP TABLE IF EXISTS public.item;

CREATE TABLE IF NOT EXISTS public.item
(
    id bigint NOT NULL,
    container_id bigint references container NOT NULL,
    name character varying(128) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT item_pkey PRIMARY KEY (id),
    CONSTRAINT item_unique_container_name UNIQUE (container_id, name)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.item
    OWNER to jboss;

