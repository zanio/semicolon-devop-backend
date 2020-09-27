USE heroku_d8a9ffdf04c4b36;

SET FOREIGN_KEY_CHECKS  = 0;
TRUNCATE TABLE application_user;
SET FOREIGN_KEY_CHECKS  = 1;


INSERT INTO application_user(id, username, password, role, is_active)
VALUES (12, 'aniefioka@gmail.com', 'Password1$', 'SUPER_ADMIN', 1);

INSERT INTO application_user(id, username, password, role, is_active)
VALUES (13, 't.omotosho@semicolon.africa', 'Password1$', 'SUPER_ADMIN', 1);