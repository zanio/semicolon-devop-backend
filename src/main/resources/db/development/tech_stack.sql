
TRUNCATE TABLE ds_suite_test_db.public.application_user CASCADE;
TRUNCATE TABLE ds_suite_test_db.public.app CASCADE;
COMMIT;

TRUNCATE TABLE ds_suite_test_db.public.admin CASCADE;
COMMIT;
TRUNCATE TABLE ds_suite_test_db.public.developer CASCADE;
TRUNCATE TABLE ds_suite_test_db.public.tech_stack CASCADE;
-- TRUNCATE TABLE ds_suite_test_db.public.app CASCADE;
-- TRUNCATE TABLE ds_suite_test_db.public.repository CASCADE;
COMMIT;



INSERT INTO ds_suite_test_db.public.application_user(id, username, password, role, is_active)
VALUES (12, 'test_admin', 'Password1$', 'USER', true);

INSERT INTO ds_suite_test_db.public.application_user(id, username, password, role, is_active)
VALUES (13, 'zanio', 'Password1$', 'USER', true);


INSERT INTO ds_suite_test_db.public.developer
(id, auth_id, firstname, date_joined, image_url, password, phone_number, username, application_user_id)
VALUES (45, '93bf6e70-087e-11eb-94f2-17029daecd99', 'ANIEFIOK', '2020-05-18', 'http://localhost:3000', 'Password1$',
        '09084878445',
        'goat', 12);

INSERT INTO ds_suite_test_db.public.developer
(id, auth_id, firstname, date_joined, image_url, password, phone_number, username, application_user_id)
VALUES (46, '93bf6e70-087e-141eb-94f2-17029daecd99', 'AKPAN', '2020-05-18', 'http://localhost:3000', 'Password1$',
        '09084872445',
        'zanio', 13);

INSERT INTO ds_suite_test_db.public.tech_stack(id, tech_stack_type,type_of_application)
VALUES (1, 2, 1 );

INSERT INTO ds_suite_test_db.public.tech_stack(id, tech_stack_type,type_of_application)
VALUES (2, 1, 0 );