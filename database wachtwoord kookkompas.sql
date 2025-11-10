 DROP DATABASE IF EXISTS `kookkompas`;
 CREATE DATABASE `kookkompas`;
 USE `kookkompas`;
 CREATE USER IF NOT EXISTS 'userRecipe'@'localhost' IDENTIFIED BY 'userRecipePW'; 
 GRANT CREATE, ALTER, INDEX, REFERENCES, SELECT, INSERT, UPDATE, DELETE ON kookkompas.* TO 'userRecipe'@'localhost';