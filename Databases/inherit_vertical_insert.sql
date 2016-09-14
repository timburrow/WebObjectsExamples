
protocol 'jdbc:derby:';
connect 'vertical';


INSERT INTO Adult (creature_id, drivers_license ) VALUES (2, 'CA 1234567');

INSERT INTO Animal (creature_id, is_carnivore, scientific_name) VALUES (8, 1, 'Panthera leo');
INSERT INTO Animal (creature_id, is_carnivore, scientific_name) VALUES (7, 0, 'Bos taurus');
INSERT INTO Animal (creature_id, is_carnivore, scientific_name) VALUES (6, 1, 'Canis familiaris');
INSERT INTO Animal (creature_id, is_carnivore, scientific_name) VALUES (5, 1, 'Felis catus');
INSERT INTO Animal (creature_id, is_carnivore, scientific_name) VALUES (4, 0, 'Equus caballos');
INSERT INTO Animal (creature_id, is_carnivore, scientific_name) VALUES (3, 0, 'Ursus americanus');

INSERT INTO Bear (creature_id) VALUES (6);

INSERT INTO Cat (creature_id) VALUES (5);

INSERT INTO Child (creature_id) VALUES (9);
INSERT INTO Child (creature_id) VALUES (1);

INSERT INTO Cow (creature_id, is_dairy_breed) VALUES (7, 1);

INSERT INTO Creature (creature_id, date_of_birth, first_name, number_of_legs) VALUES (9, '1961-07-31 14:00:00', 'Sarah', 2);
INSERT INTO Creature (creature_id, date_of_birth, first_name, number_of_legs) VALUES (8, '1991-06-30 14:00:00', 'King', 4);
INSERT INTO Creature (creature_id, date_of_birth, first_name, number_of_legs) VALUES (7, '1989-05-31 14:00:00', 'Guernsey', 4);
INSERT INTO Creature (creature_id, date_of_birth, first_name, number_of_legs) VALUES (6, '1978-04-30 14:00:00', 'Corky', 4);
INSERT INTO Creature (creature_id, date_of_birth, first_name, number_of_legs) VALUES (5, '1980-03-31 13:00:00', 'Felix', 4);
INSERT INTO Creature (creature_id, date_of_birth, first_name, number_of_legs) VALUES (4, '1981-02-28 13:00:00', 'Blaze', 4);
INSERT INTO Creature (creature_id, date_of_birth, first_name, number_of_legs) VALUES (3, '1969-01-31 13:00:00', 'Smokey', 4);
INSERT INTO Creature (creature_id, date_of_birth, first_name, number_of_legs) VALUES (2, '1969-12-19 13:00:00', 'Rachel', 2);
INSERT INTO Creature (creature_id, date_of_birth, first_name, number_of_legs) VALUES (1, '1966-04-22 13:00:00', 'David', 2);

INSERT INTO Dog (creature_id) VALUES (6);

INSERT INTO Folder (folder_id, name, parent_node_id) VALUES (6, 'Folder 1.2', 2);
INSERT INTO Folder (folder_id, name, parent_node_id) VALUES (5, 'Folder 1.1', 2);
INSERT INTO Folder (folder_id, name, parent_node_id) VALUES (4, 'Folder 3', 1);
INSERT INTO Folder (folder_id, name, parent_node_id) VALUES (3, 'Folder 2', 1);
INSERT INTO Folder (folder_id, name, parent_node_id) VALUES (2, 'Folder 1', 1);
INSERT INTO Folder (folder_id, name, parent_node_id) VALUES (1, 'ROOT', NULL);
INSERT INTO Folder (folder_id, name, parent_node_id) VALUES (7, 'Folder 2.2', 3);

INSERT INTO Folder_Data (data_id, folder_id) VALUES (9, 6);
INSERT INTO Folder_Data (data_id, folder_id) VALUES (12, 6);
INSERT INTO Folder_Data (data_id, folder_id) VALUES (6, 7);
INSERT INTO Folder_Data (data_id, folder_id) VALUES (7, 7);
INSERT INTO Folder_Data (data_id, folder_id) VALUES (8, 7);
INSERT INTO Folder_Data (data_id, folder_id) VALUES (5, 4);
INSERT INTO Folder_Data (data_id, folder_id) VALUES (4, 3);
INSERT INTO Folder_Data (data_id, folder_id) VALUES (12, 2);
INSERT INTO Folder_Data (data_id, folder_id) VALUES (11, 4);
INSERT INTO Folder_Data (data_id, folder_id) VALUES (10, 7);
INSERT INTO Folder_Data (data_id, folder_id) VALUES (9, 3);
INSERT INTO Folder_Data (data_id, folder_id) VALUES (8, 6);
INSERT INTO Folder_Data (data_id, folder_id) VALUES (7, 5);
INSERT INTO Folder_Data (data_id, folder_id) VALUES (6, 2);
INSERT INTO Folder_Data (data_id, folder_id) VALUES (5, 1);
INSERT INTO Folder_Data (data_id, folder_id) VALUES (4, 1);

INSERT INTO Horse (breed, creature_id) VALUES ('Arabian', 4);

INSERT INTO Lion (creature_id) VALUES (8);

INSERT INTO Person (creature_id, favorite_domesticated_animal_id, last_name, ssn) VALUES (9, 5, 'Wagner', '444-55-6666');
INSERT INTO Person (creature_id, favorite_domesticated_animal_id, last_name, ssn) VALUES (2, 4, 'Proton', '222-33-4444');
INSERT INTO Person (creature_id, favorite_domesticated_animal_id, last_name, ssn) VALUES (1, 6, 'Proton', '111-22-3333');

