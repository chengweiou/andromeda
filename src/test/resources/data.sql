INSERT INTO `account` (`username`, `password`, `personId`, `active`, `extra`, `createAt`, `updateAt`) VALUES ('ou', '$2a$12$OOw7Nx4IgoVAOkOyc61lK.GUqC.vxjYlyDJK4AnIj/qPxhX32moEi', '1', 1, 'none', '2019-01-01T00:00:00', '2019-01-01T00:00:00');
INSERT INTO `account` (`username`, `password`, `personId`, `active`, `extra`, `createAt`, `updateAt`) VALUES ('chiu', '$2a$12$OOw7Nx4IgoVAOkOyc61lK.GUqC.vxjYlyDJK4AnIj/qPxhX32moEi', '1', 1, 'none', '2019-01-01T00:00:00', '2019-01-01T00:00:00');

INSERT INTO `loginRecord` (`accountId`, `personId`, `ip`, `platform`, `loginTime`, `logoutTime`, `createAt`, `updateAt`) VALUES (1, '1', '127.0.0.1', 'chrom', '2019-01-01T00:00:00', '2019-01-01T00:00:00', '2019-01-01T00:00:00', '2019-01-01T00:00:00');
INSERT INTO `loginRecord` (`accountId`, `personId`, `ip`, `platform`, `loginTime`, `logoutTime`, `createAt`, `updateAt`) VALUES (2, '1', 'www.google.com', 'iphone', '2019-01-01T00:00:00', '2019-01-01T00:00:00', '2019-01-01T00:00:00', '2019-01-01T00:00:00');
