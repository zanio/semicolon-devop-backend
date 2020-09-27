USE alaajo_db;

SET FOREIGN_KEY_CHECKS  = 0;
TRUNCATE TABLE password_reset;
TRUNCATE TABLE application_user;
SET FOREIGN_KEY_CHECKS  = 1;

INSERT INTO
application_user(id, username, password, role, is_active) VALUES (17, 'aniefiok', 'Password1$', "ADMIN", 1);


INSERT INTO password_reset(id, token, application_user_id,expiry_date,is_password_reset)
VALUES (1431, 'gg65gfggf',17, 7200000,0);

