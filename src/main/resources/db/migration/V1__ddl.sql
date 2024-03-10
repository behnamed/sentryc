
create table public.producers
(
    id         uuid not null primary key,
    name       varchar(255),
    created_at timestamp not null
);

create table public.marketplace
(
    id          varchar(255) not null primary key,
    description varchar(255)
);

create table public.seller_infos
(
    id             uuid not null primary key,
    marketplace_id varchar(255) constraint "FK_marketplace_id" references public.marketplace(id),
    name           varchar(2048) not null,
    url            varchar(2048),
    country        varchar(255),
    external_id    varchar(255),
    constraint "UK_marketplace_id_external_id" unique (marketplace_id, external_id)
);
CREATE INDEX IDX_seller_name ON seller_infos(name);

create table public.sellers
(
    id             uuid not null primary key,
    producer_id    uuid not null constraint "FK_producer_id" references public.producers(id),
    seller_info_id uuid constraint "FK_seller_info_id" references public.seller_infos(id),
    state          varchar(255) default 'REGULAR'::character varying not null,
    constraint "UK_producer_id_seller_info_id_state" unique (producer_id, seller_info_id, state)
);