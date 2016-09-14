
protocol 'jdbc:derby:';
connect 'singletable';

INSERT INTO Creature ( breed, creature_id, date_of_birth, drivers_license, favorite_domesticated_animal_id, first_name, is_carnivore, is_dairy_breed, last_name, number_of_legs, scientific_name, ssn, type) VALUES (NULL, 8, '0091-06-28 13:00:00', NULL, NULL, 'King', 1, NULL, NULL, 4, 'Panthera leo', NULL, 'Lion');
INSERT INTO Creature ( breed, creature_id, date_of_birth, drivers_license, favorite_domesticated_animal_id, first_name, is_carnivore, is_dairy_breed, last_name, number_of_legs, scientific_name, ssn, type) VALUES (NULL, 7, '1989-05-31 14:00:00', NULL, NULL, 'Guernsey', 0, 1, NULL, 4, 'Bos taurus', NULL, 'Cow');
INSERT INTO Creature ( breed, creature_id, date_of_birth, drivers_license, favorite_domesticated_animal_id, first_name, is_carnivore, is_dairy_breed, last_name, number_of_legs, scientific_name, ssn, type) VALUES (NULL, 6, '1978-04-30 14:00:00', NULL, NULL, 'Corky', 1, NULL, NULL, 4, 'Canis familiaris', NULL, 'Dog');
INSERT INTO Creature ( breed, creature_id, date_of_birth, drivers_license, favorite_domesticated_animal_id, first_name, is_carnivore, is_dairy_breed, last_name, number_of_legs, scientific_name, ssn, type) VALUES (NULL, 5, '1980-03-31 13:00:00', NULL, NULL, 'Felix', 1, NULL, NULL, 4, 'Felis catus', NULL, 'Cat');
INSERT INTO Creature ( breed, creature_id, date_of_birth, drivers_license, favorite_domesticated_animal_id, first_name, is_carnivore, is_dairy_breed, last_name, number_of_legs, scientific_name, ssn, type) VALUES ('Arabian', 4, '1981-02-28 13:00:00', NULL, NULL, 'Blaze', 0, NULL, NULL, 4, 'Equus caballos', NULL, 'Horse');
INSERT INTO Creature ( breed, creature_id, date_of_birth, drivers_license, favorite_domesticated_animal_id, first_name, is_carnivore, is_dairy_breed, last_name, number_of_legs, scientific_name, ssn, type) VALUES (NULL, 3, '1969-01-31 13:00:00', NULL, NULL, 'Smokey', 0, NULL, NULL, 4, 'Ursus americanus', NULL, 'Bear');
INSERT INTO Creature ( breed, creature_id, date_of_birth, drivers_license, favorite_domesticated_animal_id, first_name, is_carnivore, is_dairy_breed, last_name, number_of_legs, scientific_name, ssn, type) VALUES (NULL, 2, '1969-12-19 13:00:00', 'CA 1234567', 4, 'Rachel', NULL, NULL, 'Proton', 2, NULL, '222-33-4444', 'Adult');
INSERT INTO Creature ( breed, creature_id, date_of_birth, drivers_license, favorite_domesticated_animal_id, first_name, is_carnivore, is_dairy_breed, last_name, number_of_legs, scientific_name, ssn, type) VALUES (NULL, 1, '1966-04-22 13:00:00', NULL, 6, 'David', NULL, NULL, 'Proton', 2, NULL, '111-22-3333', 'Child');
INSERT INTO Creature ( breed, creature_id, date_of_birth, drivers_license, favorite_domesticated_animal_id, first_name, is_carnivore, is_dairy_breed, last_name, number_of_legs, scientific_name, ssn, type) VALUES (NULL, 9, '1961-07-31 14:00:00', NULL, 5, 'Sarah', NULL, NULL, 'Wagner', 2, NULL, '444-55-6666', 'Child');

INSERT INTO Folder (folder_id, name, parent_node_id) VALUES (6, 'Folder 1.2', 2);
INSERT INTO Folder (folder_id, name, parent_node_id) VALUES (5, 'Folder 1.1', 2);
INSERT INTO Folder (folder_id, name, parent_node_id) VALUES (4, 'Folder 3', 1);
INSERT INTO Folder (folder_id, name, parent_node_id) VALUES (3, 'Folder 2', 1);
INSERT INTO Folder (folder_id, name, parent_node_id) VALUES (2, 'Folder 1', 1);
INSERT INTO Folder (folder_id, name, parent_node_id) VALUES (1, 'ROOT', NULL);
INSERT INTO Folder (folder_id, name, parent_node_id) VALUES (7, 'Folder 2.1', 3);

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
INSERT INTO Folder_Data (data_id, folder_id) VALUES (5, 7);
INSERT INTO Folder_Data (data_id, folder_id) VALUES (4, 7);
INSERT INTO Folder_Data (data_id, folder_id) VALUES (3, 7);
INSERT INTO Folder_Data (data_id, folder_id) VALUES (9, 6);
INSERT INTO Folder_Data (data_id, folder_id) VALUES (6, 6);

