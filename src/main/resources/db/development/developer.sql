SET search_path TO 'ds_suite_test';

SET FOREIGN_KEY_CHECKS  = 0;
TRUNCATE TABLE ds_suite_test.public.developer;
TRUNCATE TABLE ds_suite_test.public.application_user;
TRUNCATE TABLE ds_suite_test.public.app;
SET FOREIGN_KEY_CHECKS  = 1;

INSERT INTO ds_suite_test.public.application_user(id, username, password, role, is_active) VALUES (12, 'test_admin', 'Password1$', 'ADMIN', 1);


INSERT INTO ds_suite_test.public.developer(id, firstname, lastname, phone_number, username,auth_id,password,
                                    password, application_user_id)
VALUES (45, 'superadimn', 'Password1$', '09084878445', 'tobi@gmail.com', '93bf6e70-087e-11eb-94f2-17029daecd99',
        'password1', '1234', 12);


INSERT INTO ds_suite_test.public.tech_stack(id, tech_stack_type, type_of_application)
VALUES (21, 'PYTHON', 'DECOUPLED');



INSERT INTO ds_suite_test.public.app(id, description, domain, name,title, developer_id, tech_stack_id)
VALUES (120, 'This is the application description', 'https://app.herokuapp.com', 'hello-world',45, 21);



