set search_path = andromeda;

INSERT INTO account (type, username, password, personId, active, extra, createAt, updateAt) VALUES
    ('NORMAL', 'ou', '$2a$12$OOw7Nx4IgoVAOkOyc61lK.GUqC.vxjYlyDJK4AnIj/qPxhX32moEi', '1', true, 'SUPER', '2019-01-01T00:00:00', '2019-01-01T00:00:00'),
    ('WECHAT', 'chiu', '$2a$12$OOw7Nx4IgoVAOkOyc61lK.GUqC.vxjYlyDJK4AnIj/qPxhX32moEi', '1', true, 'SUPER', '2019-01-01T00:00:00', '2019-01-01T00:00:00');

INSERT INTO accountNew (username, phone, email, wechat, weibo, google, facebook, password, personId, active, extra, createAt, updateAt) VALUES
    ('ou', '9790000000', 'a@a.c', '', '', '', '', '$2a$12$OOw7Nx4IgoVAOkOyc61lK.GUqC.vxjYlyDJK4AnIj/qPxhX32moEi', '1', true, 'SUPER', '2019-01-01T00:00:00', '2019-01-01T00:00:00'),
    ('chiu', '', '', '', '', '', '', '$2a$12$OOw7Nx4IgoVAOkOyc61lK.GUqC.vxjYlyDJK4AnIj/qPxhX32moEi', '2', true, 'SUPER', '2019-01-01T00:00:00', '2019-01-01T00:00:00');

INSERT INTO loginRecord (accountId, personId, ip, platform, loginTime, logoutTime, createAt, updateAt) VALUES
    (1, '1', '127.0.0.1', 'chrom', '2019-01-01T00:00:00', '2019-01-01T00:00:00', '2019-01-01T00:00:00', '2019-01-01T00:00:00'),
    (2, '1', 'www.google.com', 'iphone', '2019-01-01T00:00:00', '2019-01-01T00:00:00', '2019-01-01T00:00:00', '2019-01-01T00:00:00');

INSERT INTO twofa (personId, type, codeTo, loginAccountId, token, code, codeExp, createAt, updateAt) VALUES
    ('1', 'NONE', '', 0, '', '', '1000-01-01T00:00:00', '2019-01-01T00:00:00', '2019-01-01T00:00:00');

INSERT INTO codeSendRecord (type, username, code, createAt, updateAt) VALUES
    ('REGISTER', '9790000000', '111', '2019-01-01T00:00:00', '2019-01-01T00:00:00'),
    ('FORGET_PASSWORD', '9790000000', '222', '2019-01-01T00:00:00', '2019-01-01T00:00:00');

INSERT INTO accountRecover (personId, phone, email, q1, q2, q3, a1, a2, a3, code, codeExp, codeCount, createAt, updateAt) VALUES
    ('1', '9790000000', 'a@a.c', 'q1', 'q2', 'q3', 'a1', 'a2', 'a3', '', '1000-01-01T00:00:00', 0, '2019-01-01T00:00:00', '2019-01-01T00:00:00');
