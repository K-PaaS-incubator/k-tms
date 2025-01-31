DROP DATABASE IF EXISTS TESS;
CREATE DATABASE TESS DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;   

-- user create 
DELETE FROM mysql.user where user='mysql';
DELETE FROM mysql.db where mysql.db.user ='mysql'; 

grant all privileges on TESS.* to 'mysql'@'localhost' identified by 'mysql' with grant option; 
flush privileges;


-- Function or Procedure or Trigger CRUD Setting
SET GLOBAL LOG_BIN_TRUST_FUNCTION_CREATORS = ON;
