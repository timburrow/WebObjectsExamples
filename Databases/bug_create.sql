
protocol 'jdbc:derby:';
connect 'bug;create=true';

CREATE TABLE BUG (
  DATE_MODIFIED DATE,
  BUG_ID INTEGER NOT NULL UNIQUE,
  COMP_ID INTEGER NOT NULL,
  DATE_SUBMITTED DATE,
  PEOPLE_ID INTEGER NOT NULL,
  PR_ID INTEGER NOT NULL,
  STATE_ID INTEGER,
  SUBJECT CHAR(100),
  WAS_READ INTEGER,
  DESCRIPTION CHAR(100),
  ORIGINATOR_ID INTEGER
);

CREATE TABLE COMPONENT (
  COMP_ID INTEGER NOT NULL UNIQUE,
  PEOPLE_ID INTEGER,
  DESCRIPTION CHAR(30) NOT NULL
);

CREATE TABLE PEOPLE (
  FIRSTNAME CHAR(30),
  IS_ADMIN INTEGER,
  LASTNAME CHAR(30),
  PASSWORD CHAR(10),
  PEOPLE_ID INTEGER NOT NULL UNIQUE
);

CREATE TABLE PRIORITY (
  PR_ID INTEGER NOT NULL UNIQUE,
  DESCRIPTION CHAR(30)
);

CREATE TABLE STATE (
  STATE_ID INTEGER NOT NULL UNIQUE,
  DESCRIPTION CHAR(30)
);

