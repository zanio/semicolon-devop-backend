USE alaajo_db;

SET FOREIGN_KEY_CHECKS  = 0;
TRUNCATE TABLE application_user;
SET FOREIGN_KEY_CHECKS  = 1;


INSERT INTO application_user(id, username, password, role, is_active)
VALUES (12, 'aniefiok@gmail.com', 'Password1$', 'SUPER_ADMIN', 1);

INSERT INTO application_user(id, username, password, role, is_active)
VALUES (13, 'akp.an@yahoo.com', 'Password1$', 'SUPER_ADMIN', 1);

 