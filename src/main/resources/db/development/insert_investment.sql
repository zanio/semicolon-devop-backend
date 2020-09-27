USE alaajo_db;

SET FOREIGN_KEY_CHECKS  = 0;
TRUNCATE TABLE investment;
SET FOREIGN_KEY_CHECKS  = 1;

INSERT INTO investment(invest_id, image_url, invest_title, maximum_amount, minimum_amount,units,duration,category,
type, expected_return,description)
VALUES (111, '', 'title', 2.1,5.1,10,4,"category","type",1.4,"description");

INSERT INTO investment(invest_id, image_url, invest_title, maximum_amount, minimum_amount,units,duration,category,
type, expected_return,description)
VALUES (113, '', 'title', 2.1,5.1,10,4,"category","type",1.4,"description");

INSERT INTO investment(invest_id, image_url, invest_title, maximum_amount, minimum_amount,units,duration,category,
type, expected_return,description)
VALUES (143, '', 'title', 2.1,5.1,10,4,"category","type",1.4,"description");


