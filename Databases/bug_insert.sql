
protocol 'jdbc:derby:';
connect 'bug';

INSERT INTO BUG VALUES ('Aug 17, 1999', 1, 126, 'Aug 17, 1999', 3, 1, 1, 'Welcome to the bug tracker app!', 0, 'As an admin, your first task will be to create a few user account and a few components', 3);

INSERT INTO COMPONENT VALUES (12, 3, 'BugTracker');

INSERT INTO PEOPLE VALUES (NULL, 1, 'Admin', 'Admin', 3);

INSERT INTO PRIORITY VALUES (1, '1. Highest');
INSERT INTO PRIORITY VALUES (2, '2. High');
INSERT INTO PRIORITY VALUES (3, '3. Low');
INSERT INTO PRIORITY VALUES (4, '4. Lowest');

INSERT INTO STATE VALUES (1, '1. Analyze');
INSERT INTO STATE VALUES (4, '4. Closed');
INSERT INTO STATE VALUES (2, '2. Integrate');
INSERT INTO STATE VALUES (3, '3. Verify');

