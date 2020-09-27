USE alaajo_db;

SET FOREIGN_KEY_CHECKS  = 0;
TRUNCATE TABLE table_group;
TRUNCATE TABLE group_member;
TRUNCATE TABLE group_member_group_list;
TRUNCATE TABLE table_group_group_member_list;
TRUNCATE TABLE contribution;
SET FOREIGN_KEY_CHECKS  = 1;

INSERT INTO table_group(id, name, description, frequency, min_contribution,group_is_public,start_date,withdrawal_date)
VALUES (111, 'new tableGroup', 'this is the test tableGroup', 'monthly',200,1,'2020-05-18','2020-05-20');

INSERT INTO table_group(id, name, description, frequency, min_contribution,group_is_public,start_date,withdrawal_date)
VALUES (121, 'Chicken', 'this is the test tableGroup', 'monthly',500,1,'2020-05-18','2020-05-18');

INSERT INTO table_group(id, name, description, frequency, min_contribution,group_is_public,start_date,withdrawal_date)
VALUES (151, 'new tableGroup', 'this is the test tableGroup', 'monthly',300,1,'2020-05-18','2020-05-18');


INSERT INTO table_group(id, name, description, frequency, min_contribution,group_is_public,start_date,withdrawal_date)
VALUES (131, 'new tableGroup', 'this is the test tableGroup', 'monthly',250,1,'2020-05-18','2020-05-18');


INSERT INTO table_group(id, name, description, frequency, min_contribution,group_is_public,start_date,withdrawal_date)
VALUES (141, 'new tableGroup', 'this is the test tableGroup', 'monthly',200,1,'2020-05-18','2020-05-18');

#
# INSERT INTO group_member(id, date_joined, is_admin, saver_id)
# VALUES (25, '20-12-11', false, 145);
#she doesnt
# INSERT INTO group_member_group_list(group_member_id, group_list_id)
# VALUES (25, 121);
#
# INSERT INTO table_group_group_member_list(table_group_id, group_member_list_id)
# VALUES (121, 25);



