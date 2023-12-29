-- SEQUENCE: public.container_sequence

-- DROP SEQUENCE IF EXISTS public.container_sequence;

CREATE SEQUENCE IF NOT EXISTS public.container_sequence
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE public.container_sequence
    OWNER TO jboss;
