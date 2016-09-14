
protocol 'jdbc:derby:';
connect 'horizontal;create=true';


create table Adult (
date_of_birth TIMESTAMP,
favorite_domesticated_animal_id INTEGER,
first_name CHAR(50),
last_name CHAR(50),
drivers_license CHAR(50),
number_of_legs INTEGER,
ssn CHAR(12),
creature_id INTEGER NOT NULL
);

create table Bear (
scientific_name CHAR(50),
date_of_birth TIMESTAMP,
first_name CHAR(50),
creature_id INTEGER NOT NULL,
number_of_legs INTEGER,
is_carnivore INTEGER
);

create table Cat (
scientific_name CHAR(50),
date_of_birth TIMESTAMP,
first_name CHAR(50),
creature_id INTEGER NOT NULL,
number_of_legs INTEGER,
is_carnivore INTEGER
);

create table Child (
date_of_birth TIMESTAMP,
favorite_domesticated_animal_id INTEGER,
first_name CHAR(50),
last_name CHAR(50),
number_of_legs INTEGER,
ssn CHAR(12),
creature_id INTEGER NOT NULL
);

create table Cow (
scientific_name CHAR(50),
date_of_birth TIMESTAMP,
first_name CHAR(50),
creature_id INTEGER NOT NULL,
number_of_legs INTEGER,
is_dairy_breed INTEGER,
is_carnivore INTEGER
);

create table Dog (
scientific_name CHAR(50),
date_of_birth TIMESTAMP,
first_name CHAR(50),
creature_id INTEGER NOT NULL,
number_of_legs INTEGER,
is_carnivore INTEGER
);

create table Folder (
parent_node_id INTEGER,
folder_id INTEGER NOT NULL,
name VARCHAR(255)
);

create table Folder_Data (
data_id INTEGER NOT NULL,
folder_id INTEGER NOT NULL
);

create table Horse (
scientific_name CHAR(50),
date_of_birth TIMESTAMP,
first_name CHAR(50),
creature_id INTEGER NOT NULL,
number_of_legs INTEGER,
breed CHAR(50),
is_carnivore INTEGER
);

create table Lion (
scientific_name CHAR(50),
date_of_birth TIMESTAMP,
first_name CHAR(50),
creature_id INTEGER NOT NULL,
number_of_legs INTEGER,
is_carnivore INTEGER
);

