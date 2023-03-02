set search_path = andromeda;

INSERT INTO account (username, phone, email, wechat, weibo, google, facebook, password, personId, active, extra, createAt, updateAt) VALUES
    ('ou1111', '9790000000', 'a@a.c', '', '', '', '', '$2a$12$skxwj/31krAwfR8sglepzelWjFTMmsGGeTMmgmDh8DaTBi.xGBoyO', 1, true, 'SUPER', '2019-01-01T00:00:00', '2019-01-01T00:00:00'),
    ('chiu1111', '', '', '', '', '', '', '$2a$12$skxwj/31krAwfR8sglepzelWjFTMmsGGeTMmgmDh8DaTBi.xGBoyO', 2, true, 'SUPER', '2019-01-01T00:00:00', '2019-01-01T00:00:00');

INSERT INTO loginRecord (personId, ip, platform, loginTime, logoutTime, createAt, updateAt) VALUES
    (1, '127.0.0.1', 'chrom', '2019-01-01T00:00:00', '2019-01-01T00:00:00', '2019-01-01T00:00:00', '2019-01-01T00:00:00'),
    (1, 'www.google.com', 'iphone', '2019-01-01T00:00:00', '2019-01-01T00:00:00', '2019-01-01T00:00:00', '2019-01-01T00:00:00');

INSERT INTO twofa (personId, type, codeTo, token, code, codeExp, createAt, updateAt) VALUES
    (1, 'NONE', '', '', '', '1000-01-01T00:00:00', '2019-01-01T00:00:00', '2019-01-01T00:00:00');

INSERT INTO codeSendRecord (type, username, code, createAt, updateAt) VALUES
    ('REGISTER', '9790000000', '111', '2019-01-01T00:00:00', '2019-01-01T00:00:00'),
    ('FORGET_PASSWORD', '9790000000', '222', '2019-01-01T00:00:00', '2019-01-01T00:00:00');

INSERT INTO accountRecover (personId, phone, email, q1, q2, q3, a1, a2, a3, code, codeExp, codeCount, createAt, updateAt) VALUES
    (1, '9790000000', 'a@a.c', 'q1', 'q2', 'q3', 'a1', 'a2', 'a3', '', '1000-01-01T00:00:00', 0, '2019-01-01T00:00:00', '2019-01-01T00:00:00');
