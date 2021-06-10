set search_path = andromeda;

DROP TABLE IF EXISTS account;
CREATE TABLE account (
   id bigserial NOT NULL,
   type character varying NOT NULL,
   username character varying NOT NULL,
   password character varying NOT NULL,
   personId character varying NOT NULL,
   active boolean NOT NULL,
   extra character varying NOT NULL,
   createAt timestamp without time zone NOT NULL,
   updateAt timestamp without time zone NOT NULL,
   PRIMARY KEY (id)
);

DROP TABLE IF EXISTS loginRecord;
CREATE TABLE loginRecord (
   id bigserial NOT NULL,
   accountId bigserial NOT NULL,
   personId character varying NOT NULL,
   ip character varying NOT NULL,
   platform character varying NOT NULL,
   loginTime character varying NOT NULL,
   logoutTime character varying NOT NULL,
   createAt timestamp without time zone NOT NULL,
   updateAt timestamp without time zone NOT NULL,
   PRIMARY KEY (id)
);

DROP TABLE IF EXISTS twofa;
CREATE TABLE twofa (
   id bigserial NOT NULL,
   personId character varying NOT NULL,
   type character varying NOT NULL,
   codeTo character varying NOT NULL, 
   loginAccountId character varying NOT NULL,
   token character varying NOT NULL,
   code character varying NOT NULL,
   createAt timestamp without time zone NOT NULL,
   updateAt timestamp without time zone NOT NULL,
   PRIMARY KEY (id)
);

DROP TABLE IF EXISTS codeSendRecord;
CREATE TABLE codeSendRecord (
   id bigserial NOT NULL,
   type character varying NOT NULL,
   username character varying NOT NULL,
   code character varying NOT NULL,
   createAt timestamp without time zone NOT NULL,
   updateAt timestamp without time zone NOT NULL,
   PRIMARY KEY (id)
);