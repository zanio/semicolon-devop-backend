USE `alaajo_db`;

SET FOREIGN_KEY_CHECKS  = 0;
TRUNCATE TABLE saver;
TRUNCATE TABLE application_user;
TRUNCATE TABLE wallet;
TRUNCATE TABLE transaction;
TRUNCATE TABLE bank_details;
SET FOREIGN_KEY_CHECKS  = 1;

INSERT INTO application_user(id, username, password, role, is_active) VALUES (11, 'tboy', 'test123', 'ADMIN', 1);
INSERT INTO application_user(id, username, password, role, is_active) VALUES (122, 'tobi.tosho@gmail.com', 'password', 'USER', 1);
INSERT INTO alaajo_db.saver(id, firstname, lastname, phone_number, email,
                           password, unique_account_id, application_user_id)


VALUES (145, 'Omotosho', 'Oluwatobi', '09084888445', 'tobi.tosho@gmail.com', 'password', '1234', 122);
INSERT INTO alaajo_db.wallet(wallet_id, balance, currency, type, name, saver_id)
VALUES (321, 500.00, 'NGN', 'GENERAL','',145),
(322, 0.0, 'NGN', 'GOAL','',145),
(323, 0.0, 'NGN', 'INVESTMENT','',145),
(325, 0.0, 'NGN', 'FIXED','',145),
(327, 0.0, 'NGN', 'GROUP','',145);

# INSERT INTO bank_details(id, bank_name, bank_code, account_number, paystack_recipient_code, saver_id)
# VALUES (11, 'Omotosho Oluwatobi', '058', '0212701415', 'RCP_wcmudnyh762yd4z', '145');

INSERT INTO transaction(transaction_id, type, customer_code, origin_id, domain, gateway_response,
                        to_wallet, reference, paid_at, created_at, description,
                        currency, channel, status, amount, saver_id)

VALUES(131, 'CREDIT', 'SLJDLJS', 0,'','SUCCESS','GENERAL','LAPJDF080845','','','TEST TRANSaction', 'NGN', 'card','success','5655',145),
(132, 'CREDIT', 'SLJDLJS', 0,'','SUCCESS','FIXED','LAPJDt080845','','','TEST TRANSaction', 'NGN', 'card','success','5655',145),
(133, 'CREDIT', 'SLJDLJS', 0,'','SUCCESS','GROUP','LAPJDg080845','','','TEST TRANSaction', 'NGN', 'card','success','5655',145),
(134, 'CREDIT', 'SLJDLJS', 0,'','SUCCESS','GOAL','LAPJDnne80845','','','TEST TRANSaction', 'NGN', 'card','success','5655',145);


INSERT INTO transaction(transaction_id, type, created_at,
                            from_wallet, reference, description,
                            currency,  status, amount, transfer_code, saver_id)
VALUES
(135, 'DEBIT','', 'GENERAL','LAPJDFjjjf080845','Test Transaction', 'NGN','success','560','tkjffT48jfjk',145);


INSERT INTO application_user(id, username, password, role, is_active) VALUES (12, 'superadmin', 'Password1$', 'ADMIN', 1);
INSERT INTO application_user(id, username, password, role, is_active) VALUES (13, 'admin', 'Password1$', 'ADMIN', 1);
INSERT INTO application_user(id, username, password, role, is_active) VALUES (142, 'anie', 'Password1$', 'ADMIN', 1);

 INSERT INTO saver(id, firstname, lastname, phone_number, email,
                            password, unique_account_id, application_user_id)
 VALUES (45, 'superadimn', 'Password1$', '09084878445', 'tobi@gmail.com', 'password1', '1234', 12);

  INSERT INTO saver(id, firstname, lastname, phone_number, email,
                            password, unique_account_id, application_user_id)
 VALUES (80, 'admin', 'Password1$', '09081278445', 'tobie@gmail.com', 'password1', '1234', 13);

 INSERT INTO saver(id, firstname, lastname, phone_number, email,
                            password, unique_account_id, application_user_id)
 VALUES (82, 'Akpan', 'Aniefiok', '09084878400', 'tkldbi@gmail.com', 'password1', '1234', 142);



