
protocol 'jdbc:derby:';
connect 'horizontal';

INSERT INTO Adult (creature_id, date_of_birth, drivers_license, favorite_domesticated_animal_id, first_name, last_name, number_of_legs, ssn) VALUES (2, '1969-12-19 13:00:00', 'CA 1234567', 4, 'Rachel', 'Proton', 2, '222-33-4444');

INSERT INTO Bear (creature_id, date_of_birth, first_name, is_carnivore, number_of_legs, scientific_name) VALUES (3, '1969-01-31 13:00:00', 'Smokey' , 0, 4, 'Ursus americanus');

INSERT INTO Cat (creature_id, date_of_birth, first_name, is_carnivore, number_of_legs, scientific_name) VALUES (5, '1980-03-31 13:00:00', 'Felix', 1, 4, 'Felis catus');

INSERT INTO Child (creature_id, date_of_birth, favorite_domesticated_animal_id, first_name, last_name, number_of_legs, ssn) VALUES (1, '1966-04-22 13:00:00', 6, 'David', 'Proton', 2, '111-22-3333');
INSERT INTO Child (creature_id, date_of_birth, favorite_domesticated_animal_id, first_name, last_name, number_of_legs, ssn) VALUES (9, '1961-07-31 14:00:00', 5, 'Sarah', 'Wagner', 2, '444-55-6666');

INSERT INTO Cow (creature_id, date_of_birth, first_name, is_carnivore, is_dairy_breed, number_of_legs, scientific_name) VALUES (7, '1989-05-31 14:00:00', 'Guernsey', 0, 1, 4, 'Bos taurus');

INSERT INTO Dog (creature_id, date_of_birth, first_name, is_carnivore, number_of_legs, scientific_name) VALUES (6, '1978-04-30 14:00:00', 'Corky', 1, 4, 'Canis familiaris');

INSERT INTO Folder (folder_id, name, parent_node_id) VALUES (6, 'Folder 1.2', 2);
INSERT INTO Folder (folder_id, name, parent_node_id) VALUES (5, 'Folder 1.1', 2);
INSERT INTO Folder (folder_id, name, parent_node_id) VALUES (4, 'Folder 3', 1);
INSERT INTO Folder (folder_id, name, parent_node_id) VALUES (3, 'Folder 2', 1);
INSERT INTO Folder (folder_id, name, parent_node_id) VALUES (2, 'Folder 1', 1);
INSERT INTO Folder (folder_id, name, parent_node_id) VALUES (1, 'ROOT', NULL);
INSERT INTO Folder (folder_id, name, parent_node_id) VALUES (7, 'Folder 2.1', 3);

INSERT INTO Folder_Data (data_id, folder_id) VALUES (6, 6);
INSERT INTO Folder_Data (data_id, folder_id) VALUES (9, 6);
INSERT INTO Folder_Data (data_id, folder_id) VALUES (3, 7);
INSERT INTO Folder_Data (data_id, folder_id) VALUES (4, 7);
INSERT INTO Folder_Data (data_id, folder_id) VALUES (5, 7);
INSERT INTO Folder_Data (data_id, folder_id) VALUES (2, 4);
INSERT INTO Folder_Data (data_id, folder_id) VALUES (1, 3);
INSERT INTO Folder_Data (data_id, folder_id) VALUES (9, 2);
INSERT INTO Folder_Data (data_id, folder_id) VALUES (8, 4);
INSERT INTO Folder_Data (data_id, folder_id) VALUES (7, 7);
INSERT INTO Folder_Data (data_id, folder_id) VALUES (6, 3);
INSERT INTO Folder_Data (data_id, folder_id) VALUES (5, 6);
INSERT INTO Folder_Data (data_id, folder_id) VALUES (4, 5);
INSERT INTO Folder_Data (data_id, folder_id) VALUES (3, 2);
INSERT INTO Folder_Data (data_id, folder_id) VALUES (2, 1);
INSERT INTO Folder_Data (data_id, folder_id) VALUES (1, 1);

INSERT INTO Horse (breed, creature_id, date_of_birth, first_name, is_carnivore, number_of_legs, scientific_name) VALUES ('Arabian', 4, '1981-02-28 14:00:00', 'Blaze', 0, 4, 'Equus caballos');

INSERT INTO Lion (creature_id, date_of_birth, first_name, is_carnivore, number_of_legs, scientific_name) VALUES (8, '1991-06-30 14:00:00', 'King', 1, 4, 'Panthera leo');

