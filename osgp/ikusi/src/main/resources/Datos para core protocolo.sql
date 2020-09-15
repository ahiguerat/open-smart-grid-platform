/*Toda esta informacion debe de ir a la bd OSGP_CORE */

INSERT INTO public.protocol_info
(id, creation_time, modification_time, "version", protocol, protocol_version, outgoing_requests_property_prefix, incoming_responses_property_prefix, incoming_requests_property_prefix, outgoing_responses_property_prefix, parallel_requests_allowed)
VALUES(9, '2019-12-19 00:00:00.000', '2019-12-19 00:00:00.000', 0, 'ORM', '1.0', 'jms.protocol.orm.outgoing.requests', 'jms.protocol.orm.incoming.responses', 'jms.protocol.orm.incoming.requests', 'jms.protocol.orm.outgoing.responses', true);

INSERT INTO public.device_function_mapping
(function_group, "function")
VALUES('OWNER', 'ACTIVATE_DEVICE');

CREATE TABLE public.orm_rtu_device (
	id bigserial NOT NULL,
	device_identification varchar(40) NOT NULL,
	creation_time timestamp NOT NULL,
	modification_time timestamp NOT NULL,
	"version" int8 NULL,
	CONSTRAINT orm_rtu_device_device_identification_key UNIQUE (device_identification),
	CONSTRAINT orm_rtu_device_pk PRIMARY KEY (id)
);


CREATE TABLE public."position" (
	id bigserial NOT NULL,
	creation_time timestamp NOT NULL,
	modification_time timestamp NOT NULL,
	num_pos varchar NULL,
	"type" varchar NULL,
	"name" varchar NULL,
	status varchar NULL,
	orm_rtu_device_id int8 NOT NULL,
	"version" int8 NULL,
	CONSTRAINT position_pk PRIMARY KEY (id)
);

ALTER TABLE public."position" ADD CONSTRAINT position_fk FOREIGN KEY (orm_rtu_device_id) REFERENCES orm_rtu_device(id);

