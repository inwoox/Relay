INSERT INTO mem_auth(member_auth_seq,authority, register_date ) VALUES(1,'ROLE_ADMIN','2020-01-01');
INSERT INTO mem_auth(member_auth_seq,authority, register_date ) VALUES(2,'ROLE_MEMBER','2020-01-01');

INSERT INTO member(member_seq,delete_date , member_email, member_id, member_password,register_date, update_date  ) VALUES(1,null,'admin@naver.com','admin' ,'$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqaFS','2020-01-01',null );

INSERT INTO member_auth_map(member_auth_seq ,member_seq, member_auth_seq) VALUES(1,1,1);
INSERT INTO member_auth_map(member_auth_seq ,member_seq, member_auth_seq) VALUES(2,1,2);