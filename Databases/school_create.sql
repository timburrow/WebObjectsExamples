
protocol 'jdbc:derby:';
connect 'school;create=true';


create table ADDRESS (
ADDRESS_ID INTEGER NOT NULL,
ADDRESS_TYPE INTEGER,
CITY CHAR(32),
PHONE CHAR(16),
STATE CHAR(32),
STREET VARCHAR(255),
ZIP CHAR(16)
);

create table BILLING_ADDRESS (
ACCOUNT_NUMBER CHAR(16),
ADDRESS_ID INTEGER NOT NULL,
EXPIRATION_DATE TIMESTAMP,
PAYMENT_METHOD CHAR(16)
);

create table CLASSROOM (
BUILDING CHAR(64),
CLASSROOM_ID INTEGER NOT NULL,
NAME CHAR(32)
);

create table COURSE (
COURSE_ID INTEGER NOT NULL,
DETAILS VARCHAR(256),
NAME CHAR(32),
SHORT_DESCRIPTION CHAR(64)
);

create table EMPLOYEE (
ADDRESS_ID INTEGER,
AGE INTEGER,
DATE_OF_BIRTH TIMESTAMP,
EMPLOYEE_TYPE INTEGER,
FIRST_NAME VARCHAR(255),
LAST_NAME VARCHAR(255),
LOGIN CHAR(8),
MANAGER_ID INTEGER,
OFFICE CHAR(32),
PASSWORD CHAR(8),
PERSON_ID INTEGER NOT NULL,
SALARY INTEGER,
STATUS INTEGER,
DEGREE CHAR(32),
DEPARTMENT CHAR(64),
SCHEDULED_CLASS_ID INTEGER
);

create table HOME_ADDRESS (
ADDRESS_ID INTEGER NOT NULL,
EMAIL_ADDRESS CHAR(64)
);

create table PARENT (
ADDRESS_ID INTEGER,
AGE INTEGER,
DATE_OF_BIRTH TIMESTAMP,
FIRST_NAME VARCHAR(255),
LAST_NAME VARCHAR(255),
LOGIN CHAR(8),
PASSWORD CHAR(8),
PERSON_ID INTEGER NOT NULL,
STATUS INTEGER
);

create table SCHEDULE (
DAY CHAR(32),
HOURS INTEGER,
SCHEDULE_ID INTEGER NOT NULL,
TIME TIMESTAMP
);

create table SCHEDULED_CLASS (
CLASSROOM_ID INTEGER NOT NULL,
COURSE_ID INTEGER NOT NULL,
SCHEDULE_ID INTEGER NOT NULL,
SCHEDULED_CLASS_ID INTEGER NOT NULL,
TEACHER_ID INTEGER NOT NULL
);

create table STUDENT (
ADDRESS_ID INTEGER,
AGE INTEGER,
DATE_OF_BIRTH TIMESTAMP,
FIRST_NAME VARCHAR(255),
LAST_NAME VARCHAR(255),
LOGIN CHAR(8),
PASSWORD CHAR(8),
PERSON_ID INTEGER NOT NULL,
STATUS INTEGER
);

create table STUDENT_SCHEDULED_CLASS (
SCHEDULED_CLASS_ID INTEGER NOT NULL,
STUDENT_ID INTEGER NOT NULL
);

create table STUDENTPARENT (
PARENT_ID INTEGER NOT NULL,
STUDENT_ID INTEGER NOT NULL
);

create table WORK_ADDRESS (
ADDRESS_ID INTEGER NOT NULL,
COMPANY CHAR(64),
MAIL_STOP CHAR(64)
);

