DROP TABLE IF EXISTS public.t_post;

CREATE TABLE IF NOT EXISTS public.t_post
(
    id smallserial NOT NULL,
    author_id bigint,
    posted_at date,
    text character varying(255),
    CONSTRAINT t_post_pkey PRIMARY KEY (id)
)