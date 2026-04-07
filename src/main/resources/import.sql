INSERT INTO tb_role(role_name) VALUES ('ADMIN');
INSERT INTO tb_role(role_name) VALUES ('CLIENT');

INSERT INTO tb_user(name, email, password, active, role_id) VALUES ('Jeferson', 'jeff@gmail.com', 123456, true, 1);
INSERT INTO tb_user(name, email, password, active, role_id) VALUES ('Kauan', 'kauan@gmail.com', 123456, true, 2);