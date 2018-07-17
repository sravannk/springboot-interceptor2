-- Sequence: iot_event_id_seq

-- DROP SEQUENCE iot_event_id_seq;

CREATE SEQUENCE iot_event_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE iot_event_id_seq
  OWNER TO postgres;


-- Table: iot_transaction

-- DROP TABLE iot_transaction;

CREATE TABLE iot_transaction
(
  requestid character varying(255),
  uri character varying(36),
  uritype character varying(16),
  operation character varying(255),
  status character varying(20),
  servicetype character varying(16),
  parent character varying(256),
  event_id bigint NOT NULL DEFAULT nextval('iot_event_id_seq'::regclass),
  CONSTRAINT iot_transaction_pkey PRIMARY KEY (event_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE iot_transaction
  OWNER TO postgres;


EVENT_KEY

CREATE TABLE iot_transaction
(
  requestid character varying(255),
  uri character varying(36),
  uritype character varying(16),
  operation character varying(255),
  status character varying(20),
  servicetype character varying(16),
  parent character varying(256),
  event_key bigint NOT NULL DEFAULT nextval('iot_event_id_seq'::regclass),
  CONSTRAINT iot_transaction_pkey PRIMARY KEY (event_key)
)
WITH (
  OIDS=FALSE
);

**************************************************************************************
CUSTOM TABLE ORACLE :-
logback/logback-classic/src/main/resources/ch/qos/logback/classic/db/script/oracle.sql

CREATE SEQUENCE logging_event_id_seq_iot MINVALUE 1 START WITH 1;


CREATE TABLE iot_transaction 
  (
    requestid VARCHAR2(254),
    uri VARCHAR(254),
    uritype VARCHAR(254),
    operation VARCHAR(254),
    status VARCHAR(254),
    servicetype VARCHAR(254),
    parent VARCHAR(254),
    event_id NUMBER(10) PRIMARY KEY
  );


CREATE TRIGGER logging_event_id_seq_iot_trig
  BEFORE INSERT ON iot_transaction
  FOR EACH ROW  
  BEGIN  
    SELECT logging_event_id_seq_iot.NEXTVAL 
    INTO   :NEW.event_id 
    FROM   DUAL;  
  END;
/