
protocol 'jdbc:derby:';
connect 'realestate;create=true';


create table AGENT_PHOTO (
PHOTO BLOB,
AGENT_ID INTEGER NOT NULL
);

create table AGENT_RATING (
USER_ID INTEGER NOT NULL,
SUMMARY BLOB,
RATING_ID INTEGER NOT NULL,
AGENT_RATING_ID INTEGER NOT NULL
);

create table CONTACT_INFO (
CONTACT CHAR(40) NOT NULL,
CONTACT_TYPE_ID INTEGER,
NOTES VARCHAR(255),
USER_ID INTEGER NOT NULL,
CONTACT_ID INTEGER NOT NULL
);

create table CONTACT_TYPE (
TYPE CHAR(40) NOT NULL,
CONTACT_TYPE_ID INTEGER NOT NULL
);

create table FEATURE (
FEATURE CHAR(40) NOT NULL,
FEATURE_ID INTEGER NOT NULL
);

create table LISTING (
AGENT_ID INTEGER NOT NULL,
ASKING_PRICE DECIMAL(10,2) NOT NULL,
BATHROOMS FLOAT,
BEDROOMS FLOAT,
LOT_SQFT INTEGER,
LISTING_IDENTIFIER CHAR(20) NOT NULL,
LISTING_TYPE_ID INTEGER NOT NULL,
SELLING_PRICE DECIMAL(10,2),
SIZE_SQFT INTEGER,
MOVIE_URL VARCHAR(255),
YEAR_BUILT INTEGER,
LISTING_ID INTEGER NOT NULL,
ISSOLD INTEGER
);

create table LISTING_ADDRESS (
APTNUM CHAR(10),
CITY CHAR(40) NOT NULL,
STATE CHAR(2) NOT NULL,
STREET CHAR(80) NOT NULL,
ZIP CHAR(10) NOT NULL,
LISTING_ID INTEGER NOT NULL
);

create table LISTING_FEATURE (
FEATURE_ID INTEGER NOT NULL,
LISTING_ID INTEGER NOT NULL
);

create table LISTING_PHOTO (
PHOTO BLOB,
DESCRIPTION VARCHAR(255),
LISTING_ID INTEGER NOT NULL,
LISTING_PHOTO_ID INTEGER NOT NULL,
IS_PRIMARY_PHOTO INTEGER,
PHOTO_TYPE CHAR(8)
);

create table LISTING_TYPE (
LISTING_TYPE CHAR(40) NOT NULL,
LISTING_TYPE_ID INTEGER NOT NULL
);

create table RATING (
RATING_DESCRIPTION CHAR(40) NOT NULL,
RATING_ID INTEGER NOT NULL,
RATING INTEGER NOT NULL
);

create table SUGGESTIONS (
CUSTOMER_ID INTEGER NOT NULL,
LISTING_ID INTEGER NOT NULL
);

create table USERS (
FIRST_NAME CHAR(80) NOT NULL,
LAST_NAME CHAR(80) NOT NULL,
LOGIN CHAR(20) NOT NULL,
PASSWORD CHAR(20) NOT NULL,
USER_TYPE INTEGER NOT NULL,
AGENT_ID INTEGER,
USER_ID INTEGER NOT NULL
);

create table USER_DEFAULTS (
USER_DEFAULTS BLOB,
USER_ID INTEGER NOT NULL,
USER_DEFAULTS_ID INTEGER NOT NULL
);

