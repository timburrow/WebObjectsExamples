
protocol 'jdbc:derby:';
connect 'discussion';

INSERT INTO BOARD (ALLOWS_POSTING, BOARD_ID, BOARD_NAME, DEFAULT_ACCESS_LEVEL, LAST_POSTING_NUMBER, PARENT_BOARD_ID) VALUES (NULL, 1, 'Apple Technologies' , 1, NULL, NULL);
INSERT INTO BOARD (ALLOWS_POSTING, BOARD_ID, BOARD_NAME, DEFAULT_ACCESS_LEVEL, LAST_POSTING_NUMBER, PARENT_BOARD_ID) VALUES (1, 2, 'WebObjects', 2, NULL, 1);
INSERT INTO BOARD (ALLOWS_POSTING, BOARD_ID, BOARD_NAME, DEFAULT_ACCESS_LEVEL, LAST_POSTING_NUMBER, PARENT_BOARD_ID) VALUES (1, 3, 'Mac OS X', 2, NULL, 1);

INSERT INTO MESSAGE (CONTENT, DATE_POSTED, MESSAGE_ID, PARENT_MESSAGE_ID, PERSON_ID, SUBJECT) VALUES ('This is an administrator posting to all default discussion boards.\n\nWe hope you enjoy using this application.', '2001-10-24 02:56:22' , 1, NULL, 1, 'Welcome To DiscussionBoard');
INSERT INTO MESSAGE (CONTENT, DATE_POSTED, MESSAGE_ID, PARENT_MESSAGE_ID, PERSON_ID, SUBJECT) VALUES ('Yeah, it''s fun discussing Mac OS X.', '2001-10-24 02:58:40' , 2, 1, 2, 'Welcome To DiscussionBoard');
INSERT INTO MESSAGE (CONTENT, DATE_POSTED, MESSAGE_ID, PARENT_MESSAGE_ID, PERSON_ID, SUBJECT) VALUES ('woadmin', '2001-10-24 02:59:09' , 3, 1, 2, 'Welcome To DiscussionBoard');

INSERT INTO PERSON (CN, DESCRIPTION, PERSON_ID, SEE_ALSO, SN, TELEPHONE_NUMBER, USER_PASSWORD) VALUES ('woadmin', 'Default WebObjects Administrator', 1, NULL, 'woadmin', NULL, 'woadmin');
INSERT INTO PERSON (CN, DESCRIPTION, PERSON_ID, SEE_ALSO, SN, TELEPHONE_NUMBER, USER_PASSWORD) VALUES ('wouser' , 'Default WebObjects User' , 2, NULL, 'wouser' , NULL, 'wouser');

INSERT INTO POSTING (BOARD_ID, MESSAGE_ID, POSTING_NUMBER, POSTING_PRIORITY) VALUES (3, 1, NULL, NULL);
INSERT INTO POSTING (BOARD_ID, MESSAGE_ID, POSTING_NUMBER, POSTING_PRIORITY) VALUES (2, 1, NULL, NULL);
INSERT INTO POSTING (BOARD_ID, MESSAGE_ID, POSTING_NUMBER, POSTING_PRIORITY) VALUES (3, 2, NULL, NULL);
INSERT INTO POSTING (BOARD_ID, MESSAGE_ID, POSTING_NUMBER, POSTING_PRIORITY) VALUES (2, 3, NULL, NULL);

INSERT INTO PROFILE (ALIAS, IS_ADMINSTRATOR, PERSON_ID, PROFILE_ID, REFRESH_PING_RATE, USER_DEFAULTS) VALUES ('WebObjects Administrator', 1, 1, 1, 30, '{\n"question = window, task = queryWindow._EOWindowWidth" = "436";\n"question = window, task = queryWindow._EOWindowHeight" = "378";\n"question = modalDialog, task = select, entity = Board._EOWindowWidth" = "387";\n"question = window, task = form, entity = Person._EOWindowHeight" = "406";\n"question = window, task = form, entity = Person._EOWindowHeight" = "406";\n"question = modalDialog, task = select, entity = Board._EOWindowHeight" = "189";\n"question = window, task = form, entity = Person._EOWindowWidth" = "315";\n"question = window, task = form, entity = Board._EOWindowWidth" = "315";\n"question = window, task = form, entity = Board._EOWindowHeight" = "400";\n"question = window, task = form, entity = Message._EOWindowHeight" = "585";\n"question = window, task = form, entity = Message._EOWindowWidth" = "315";\n}');
INSERT INTO PROFILE (ALIAS, IS_ADMINSTRATOR, PERSON_ID, PROFILE_ID, REFRESH_PING_RATE, USER_DEFAULTS) VALUES ('WebObjects User' , 0, 2, 2, 30, '{\n"question = window, task = preferences._EOWindowWidth" = "362";\n"question = window, task = subscriptions._EOWindowWidth" = "200";\n"question = window, task = preferences._EOWindowY" = "0";\n"question = window, task = preferences._EOWindowX" = "389";\n"question = window, task = subscriptions._EOWindowY" = "290";\n"question = window, task = subscriptions._EOWindowX" = "470";\n"question = window, task = subscriptions._EOWindowHeight" = "257";\n"question = window, task = preferences._EOWindowHeight" = "237";\n}');

INSERT INTO SUBSCRIPTION (ACCESS_LEVEL, BOARD_ID, IS_FAVORITE, PERSON_ID, SHOULD_SEND_DIGEST, SUBSCRIPTION_ID) VALUES (NULL, 3, NULL, 2, NULL, 2);
INSERT INTO SUBSCRIPTION (ACCESS_LEVEL, BOARD_ID, IS_FAVORITE, PERSON_ID, SHOULD_SEND_DIGEST, SUBSCRIPTION_ID) VALUES (NULL, 2, NULL, 2, NULL, 1);

