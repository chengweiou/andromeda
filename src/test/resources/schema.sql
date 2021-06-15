set search_path = andromeda;

DROP TABLE IF EXISTS account;
CREATE TABLE account (
   id bigserial NOT NULL,
   username character varying NOT NULL,
   phone character varying NOT NULL,
   email character varying NOT NULL,
   wechat character varying NOT NULL,
   weibo character varying NOT NULL,
   google character varying NOT NULL,
   facebook character varying NOT NULL,
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
   token character varying NOT NULL,
   code character varying NOT NULL,
   codeExp timestamp without time zone NOT NULL,
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

DROP TABLE IF EXISTS accountRecover;
CREATE TABLE accountRecover (
   id bigserial NOT NULL,
   personId character varying NOT NULL,
   phone character varying NOT NULL,
   email character varying NOT NULL,
   q1 character varying NOT NULL,
   q2 character varying NOT NULL,
   q3 character varying NOT NULL,
   a1 character varying NOT NULL,
   a2 character varying NOT NULL,
   a3 character varying NOT NULL,
   code character varying NOT NULL,
   codeExp timestamp without time zone NOT NULL,
   codeCount smallint NOT NULL,
   createAt timestamp without time zone NOT NULL,
   updateAt timestamp without time zone NOT NULL,
   PRIMARY KEY (id)
);