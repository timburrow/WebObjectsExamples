
protocol 'jdbc:derby:';
connect 'singletable;create=true';

create table Creature (
scientific_name CHAR(50),
date_of_birth TIMESTAMP,
is_carnivore INTEGER,
first_name CHAR(50),
number_of_legs INTEGER,
type CHAR(50),
breed CHAR(50),
favorite_domesticated_animal_id INTEGER,
last_name CHAR(50),
drivers_license CHAR(50),
ssn CHAR(12),
is_dairy_breed INTEGER,
creature_id int NOT NULL
);

create table Folder (
name CHAR(50),
parent_node_id INTEGER,
folder_id INTEGER NOT NULL
);

create table Folder_Data (
folder_id int NOT NULL,
data_id int NOT NULL
);

