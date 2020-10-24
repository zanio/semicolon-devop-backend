CREATE DATABASE ds_suite_test_db;
CREATE USER ds_suite_test WITH PASSWORD 'ds_suite_test_123';
GRANT ALL PRIVILEGES ON DATABASE "ds_suite_test_db" to ds_suite_test;