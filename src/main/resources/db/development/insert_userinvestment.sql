USE alaajo_db;

SET FOREIGN_KEY_CHECKS  = 0;
TRUNCATE TABLE saver;
TRUNCATE TABLE application_user;
TRUNCATE TABLE investment;
TRUNCATE TABLE user_investment;
SET FOREIGN_KEY_CHECKS  = 1;

INSERT INTO application_user(id, username, password, role, is_active) VALUES (12, 'superadmin', 'Password1$', "ADMIN", 1);


 INSERT INTO alaajo_db.saver(id, firstname, lastname, phone_number, email,
                            password, unique_account_id, application_user_id)
 VALUES (45, 'superadimn', 'Password1$', '09084878445', 'tobi@gmail.com', 'password1', '1234', 12);

 INSERT INTO investment(invest_id, image_url, invest_title, maximum_amount, minimum_amount,units,duration,category,
type, expected_return,description)
VALUES (143, '', 'title', 2.1,5.1,10,4,"category","type",1.4,"description");

INSERT INTO user_investment(id, investment_id_invest_id, user_id_id,start_date)
VALUES (1430, 143,45, '2020-05-18');

