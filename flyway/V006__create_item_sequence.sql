-- SEQUENCE: public.item_sequence

-- DROP SEQUENCE IF EXISTS public.item_sequence;

CREATE SEQUENCE IF NOT EXISTS public.item_sequence
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE public.item_sequence
    OWNER TO jboss;
