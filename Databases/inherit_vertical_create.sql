
protocol 'jdbc:derby:';
connect 'vertical;create=true';

create table Adult (
drivers_license CHAR(50),
creature_id INTEGER NOT NULL
);

create table Animal (
scientific_name CHAR(50),
is_carnivore INTEGER,
creature_id INTEGER NOT NULL
);

create table Bear (
creature_id INTEGER NOT NULL
);

create table Cat (
creature_id INTEGER NOT NULL
);

create table Child (
creature_id INTEGER NOT NULL
);

create table Cow (
is_dairy_breed INTEGER,
creature_id INTEGER NOT NULL
);

create table Creature (
date_of_birth TIMESTAMP,
first_name CHAR(50),
number_of_legs INTEGER,
creature_id INTEGER NOT NULL
);

create table Dog (
creature_id INTEGER NOT NULL
);

create table Folder (
name CHAR(50),
parent_node_id INTEGER,
folder_id INTEGER NOT NULL
);

create table Folder_Data (
folder_id INTEGER NOT NULL,
data_id INTEGER NOT NULL
);

create table Horse (
breed CHAR(50),
creature_id INTEGER NOT NULL
);

create table Lion (
creature_id INTEGER NOT NULL
);

create table Person (
favorite_domesticated_animal_id INTEGER,
last_name CHAR(50),
ssn CHAR(12),
creature_id INTEGER NOT NULL
);

