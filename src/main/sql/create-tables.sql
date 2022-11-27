create table calculation
(
    chat_id                    long not null
        constraint calculation_pk
            primary key,
    state                      text,
    last_cold_water_meters     integer,
    current_cold_water_meters  integer,
    last_hot_water_meters      integer,
    current_hot_water_meters   integer,
    last_electricity_meters    integer,
    current_electricity_meters integer
);
create unique index calculation_chat_id_uindex
    on calculation (chat_id);

