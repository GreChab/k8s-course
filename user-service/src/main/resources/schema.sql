DROP TABLE IF EXISTS public.t_user;

CREATE TABLE t_user
(
    id smallserial NOT NULL,
    username character varying(255),
    amount character varying(255),
    CONSTRAINT t_user_pkey PRIMARY KEY (id)
)