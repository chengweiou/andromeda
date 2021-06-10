set search_path = andromeda;

INSERT INTO account (type, username, password, personId, active, extra, createAt, updateAt) VALUES
    ('NORMAL', 'ou', '$2a$12$OOw7Nx4IgoVAOkOyc61lK.GUqC.vxjYlyDJK4AnIj/qPxhX32moEi', '1', true, 'SUPER', '2019-01-01T00:00:00', '2019-01-01T00:00:00'),
    ('WECHAT', 'chiu', '$2a$12$OOw7Nx4IgoVAOkOyc61lK.GUqC.vxjYlyDJK4AnIj/qPxhX32moEi', '1', true, 'SUPER', '2019-01-01T00:00:00', '2019-01-01T00:00:00');

INSERT INTO loginRecord (accountId, personId, ip, platform, loginTime, logoutTime, createAt, updateAt) VALUES
    (1, '1', '127.0.0.1', 'chrom', '2019-01-01T00:00:00', '2019-01-01T00:00:00', '2019-01-01T00:00:00', '2019-01-01T00:00:00'),
    (2, '1', 'www.google.com', 'iphone', '2019-01-01T00:00:00', '2019-01-01T00:00:00', '2019-01-01T00:00:00', '2019-01-01T00:00:00');

INSERT INTO twofa (personId, type, codeTo, loginAccountId, token, code, createAt, updateAt) VALUES
    ('1', 'NONE', '', 0, '', '', '2019-01-01T00:00:00', '2019-01-01T00:00:00');

INSERT INTO codeSendRecord (type, username, code, createAt, updateAt) VALUES
    ('REGISTER', '9790000000', '111', '2019-01-01T00:00:00', '2019-01-01T00:00:00'),
    ('FORGET_PASSWORD', '9790000000', '111', '2019-01-01T00:00:00', '2019-01-01T00:00:00');
