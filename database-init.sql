CREATE DATABASE mmldb;
CREATE USER 'minecraft'@'localhost' IDENTIFIED BY 'mmlPwd12';
GRANT ALL PRIVILEGES ON mmldb.* TO 'minecraft'@'localhost';
FLUSH PRIVILEGES;

USE mmldb;

CREATE TABLE Player (
    mcId VARCHAR(40) PRIMARY KEY,
    nick VARCHAR(120),
    lifeCount INT,
    score INT,
    deaths INT,
    killedPlayers INT,
    killedMobs INT,
    killedHostileMobs INT,
    gainedXP INT,
    spendTimeOnServer BIGINT,
    lastLoginTs DATETIME,
    createTs DATETIME,
    updateTs DATETIME,
    INDEX nick (nick)
);
