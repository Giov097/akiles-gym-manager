BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "clients" (
	"dni"	bigint NOT NULL,
	"createdAt"	datetime,
	"enabled"	boolean,
	"fullName"	varchar(255),
	PRIMARY KEY("dni")
);
CREATE TABLE IF NOT EXISTS "users" (
	"username"	varchar(255) NOT NULL,
	"createdAt"	datetime,
	"password"	varchar(255),
	PRIMARY KEY("username")
);
INSERT INTO "clients" ("dni","createdAt","enabled","fullName") VALUES (40167870,1670696700385,NULL,'Gerónimo Giovenale');
INSERT INTO "clients" ("dni","createdAt","enabled","fullName") VALUES (41228762,1670710083522,1,'Sheila Barreto');
INSERT INTO "users" ("username","createdAt","password") VALUES ('admin',NULL,'1004');
COMMIT;
