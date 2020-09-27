USE alaajo_db;

SET FOREIGN_KEY_CHECKS  = 0;
TRUNCATE TABLE admin;
TRUNCATE TABLE application_user;
SET FOREIGN_KEY_CHECKS  = 1;

INSERT INTO application_user(id, username, password, role, is_active)
VALUES (11, 'ani', 'ani', 'ADMIN', 1);

INSERT INTO alaajo_db.admin(admin_id, firstname, lastname, phone_number, email,
                           password, unique_account_id, application_user_id)
VALUES (45, 'Oluwatobi', 'Johnson', '09084878445', 'tobi@gmail.com', 'ani', '1234', 11);
