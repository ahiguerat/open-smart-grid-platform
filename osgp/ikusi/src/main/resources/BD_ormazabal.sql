create table device_firmware_status(
	id_device_firmware_status serial primary key,
	device_identification varchar(40),
	status varchar(40),
	message varchar(500),
	firmware varchar(255),
	network_address varchar(30),
	creation_time timestamp default now(),
	modify_time timestamp	
);

/*
ALTER TABLE public.device_firmware_status ADD firmware varchar(255) NULL;
ALTER TABLE public.device_firmware_status ADD network_address varchar(30) NULL;
ALTER TABLE public.device_firmware_status ADD category varchar(200) NULL;

*/

create table "severity"(
	id_severity serial primary key,
	category varchar(200),
	code varchar(250),
	color varchar(50),
	description varchar(500),
	creation_time timestamp default now(),
	modify_time timestamp
);

/*ALTER TABLE public.severity ADD description varchar(500) NULL;
ALTER TABLE public.severity ADD category varchar(200) NULL;

*/


CREATE TABLE "location" (
	id_location serial NOT NULL,
	id_location_child int4 NULL,
	code varchar(50) NULL,
	"name" varchar(250) NULL,
	description varchar(250) NULL,
	creation_time timestamp NULL DEFAULT now(),
	modify_time timestamp NULL,
	CONSTRAINT location_pkey PRIMARY KEY (id_location),
	CONSTRAINT fk_child_location FOREIGN KEY (id_location_child) REFERENCES location(id_location)
);


CREATE TABLE public.sub_station (
	id_sub_station serial NOT NULL,
	code varchar(50) NULL,
	"name" varchar(250) NULL,
	description varchar(250) NULL,
	creation_time timestamp NULL DEFAULT now(),
	modify_time timestamp NULL,
	CONSTRAINT sub_station_pkey PRIMARY KEY (id_sub_station)
);



CREATE TABLE public.sub_location (
	id_location int4 NOT NULL,
	id_sub_station int4 NOT NULL,
	creation_time timestamp NULL DEFAULT now(),
	modify_time timestamp NULL,
	CONSTRAINT sub_location_id_sub_station_key UNIQUE (id_sub_station),
	CONSTRAINT sub_location_pkey PRIMARY KEY (id_location, id_sub_station)
);

ALTER TABLE public.sub_location ADD CONSTRAINT sub_location_id_location_fkey FOREIGN KEY (id_location) REFERENCES location(id_location);
ALTER TABLE public.sub_location ADD CONSTRAINT sub_location_id_sub_station_fkey FOREIGN KEY (id_sub_station) REFERENCES sub_station(id_sub_station);


CREATE TABLE public.line (
	id_line serial NOT NULL,
	code varchar(50) NULL,
	"name" varchar(250) NULL,
	description varchar(250) NULL,
	creation_time timestamp NULL DEFAULT now(),
	modify_time timestamp NULL,
	CONSTRAINT line_pkey PRIMARY KEY (id_line)
);


CREATE TABLE public.line_sub_station (
	id_sub_station int4 NOT NULL,
	id_line int4 NOT NULL,
	creation_time timestamp NULL DEFAULT now(),
	modify_time timestamp NULL,
	CONSTRAINT line_sub_station_id_line_key UNIQUE (id_line),
	CONSTRAINT line_sub_station_pkey PRIMARY KEY (id_sub_station, id_line)
);

ALTER TABLE public.line_sub_station ADD CONSTRAINT line_sub_station_id_line_fkey FOREIGN KEY (id_line) REFERENCES line(id_line);
ALTER TABLE public.line_sub_station ADD CONSTRAINT line_sub_station_id_sub_station_fkey FOREIGN KEY (id_sub_station) REFERENCES sub_station(id_sub_station);
;

CREATE TABLE public.station_ct (
	id_station_ct serial NOT NULL,
	code varchar(50) NULL,
	"name" varchar(250) NULL,
	description varchar(250) NULL,
	creation_time timestamp NULL DEFAULT now(),
	modify_time timestamp NULL,
	CONSTRAINT station_ct_pkey PRIMARY KEY (id_station_ct)
);


CREATE TABLE public.line_ct (
	id_line int4 NOT NULL,
	id_station_ct int4 NOT NULL,
	CONSTRAINT line_ct_id_station_ct_key UNIQUE (id_station_ct),
	CONSTRAINT line_ct_pkey PRIMARY KEY (id_line, id_station_ct)
);

ALTER TABLE public.line_ct ADD CONSTRAINT line_ct_id_line_fkey FOREIGN KEY (id_line) REFERENCES line(id_line);
ALTER TABLE public.line_ct ADD CONSTRAINT line_ct_id_station_ct_fkey FOREIGN KEY (id_station_ct) REFERENCES station_ct(id_station_ct);


CREATE TABLE public.device_station_ct (
	id serial NOT NULL,
	device_identification varchar(40) NOT NULL,
	gateway_device_id int4 NOT NULL,
	id_station_ct int4 NULL,
	creation_time timestamp NULL DEFAULT now(),
	modify_time timestamp NULL,
	CONSTRAINT device_station_ct_pk PRIMARY KEY (id)
);

ALTER TABLE public.device_station_ct ADD CONSTRAINT device_station_ct_id_station_ct_fkey FOREIGN KEY (id_station_ct) REFERENCES station_ct(id_station_ct);


CREATE TABLE public.station_ct_sub_station (
	id_station_ct int4 NULL,
	id_sub_station int4 NULL,
	creation_time timestamp NULL DEFAULT now(),
	modify_time timestamp NULL,
	CONSTRAINT station_ct_sub_station_id_sub_station_key UNIQUE (id_sub_station)
);

ALTER TABLE public.station_ct_sub_station ADD CONSTRAINT station_ct_sub_station_id_station_ct_fkey FOREIGN KEY (id_station_ct) REFERENCES station_ct(id_station_ct);
ALTER TABLE public.station_ct_sub_station ADD CONSTRAINT station_ct_sub_station_id_sub_station_fkey FOREIGN KEY (id_sub_station) REFERENCES sub_station(id_sub_station);


CREATE TABLE public.users (
  user_id serial NOT NULL,
  firts_name varchar(101) NOT NULL,
  last_name varchar(101) NOT NULL,
  username varchar(120) NOT NULL,
  "password" varchar(200) NOT NULL,
  CONSTRAINT user_pkey PRIMARY KEY (user_id),
  CONSTRAINT users_un UNIQUE (username)
);

INSERT INTO public.users (firts_name,last_name,username,"password") VALUES 
('Jesus Etelberto','Sanchez','jesus.sanchez.ext','$2a$10$TjIM2LKS7GVmipLxO/Jk4.wWMFK.VLD9qrxcLxOz.lISXkiEmzzLq')
;


