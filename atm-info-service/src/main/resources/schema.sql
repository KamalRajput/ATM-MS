create table IF NOT EXISTS atminfo (atm_id integer not null, fifty_euro_qty integer not null, five_euro_qty integer not null,
ten_euro_qty integer not null, twenty_euro_qty integer not null,atm_balance double precision not null, primary key (atm_id));
