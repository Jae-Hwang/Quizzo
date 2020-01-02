DROP USER quizzo_user CASCADE;

CREATE USER quizzo_user
IDENTIFIED BY quizzo_pass
DEFAULT TABLESPACE users
TEMPORARY TABLESPACE temp
QUOTA 10G ON users;

GRANT CONNECT TO quizzo_user;
GRANT RESOURCE TO quizzo_user;
GRANT CREATE SESSION TO quizzo_user;
GRANT CREATE TABLE TO quizzo_user;
GRANT CREATE VIEW TO quizzo_user;

conn quizzo_user/quizzo_pass;

/************************************
Tables and sequences
************************************/
CREATE SEQUENCE users_id_seq;
CREATE TABLE users (
    user_id NUMBER PRIMARY KEY,
    type NUMBER DEFAULT 1 NOT NULL,
    username VARCHAR2(20) NOT NULL UNIQUE,
    password VARCHAR2(20) NOT NULL,
    firstname VARCHAR2(15) NOT NULL,
    lastname VARCHAR2(15),
    active NUMBER DEFAULT 1 NOT NULL
);
CREATE INDEX users_active ON users (active);


CREATE SEQUENCE groups_id_seq;
CREATE TABLE groups (
    gid NUMBER PRIMARY KEY,
    name VARCHAR2(20) NOT NULL UNIQUE,
    active NUMBER DEFAULT 1 NOT NULL
);
CREATE INDEX group_active ON groups (active);


CREATE TABLE groups_composite_key (
    user_id NUMBER REFERENCES users(user_id) NOT NULL,
    gid NUMBER REFERENCES groups(gid) NOT NULL
);
CREATE UNIQUE INDEX groups_key_user_id_gid ON groups_composite_key (user_id, gid);


CREATE SEQUENCE questions_id_seq;
CREATE TABLE questions (
    quid NUMBER PRIMARY KEY,
    description VARCHAR2(500) NOT NULL,
    type NUMBER DEFAULT 1 NOT NULL,
    s1 VARCHAR2(100) NOT NULL,
    S2 VARCHAR2(100),
    S3 VARCHAR2(100),
    S4 VARCHAR2(100),
    S5 VARCHAR2(100),
    catagory VARCHAR2(20) NOT NULL,
    active NUMBER DEFAULT 1 NOT NULL,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by NUMBER REFERENCES users(user_id) NOT NULL
);
CREATE INDEX questions_active_type ON questions (active, type);


CREATE SEQUENCE quizzes_id_seq;
CREATE TABLE quizzes (
    qid NUMBER PRIMARY KEY,
    name VARCHAR2(20) NOT NULL,
    active NUMBER DEFAULT 1 NOT NULL,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by NUMBER REFERENCES users(user_id) NOT NULL
);
CREATE INDEX quizzes_active ON quizzes (active);


CREATE TABLE quizzes_composite_key (
    quid NUMBER REFERENCES questions(quid) NOT NULL,
    qid NUMBER REFERENCES quizzes(qid) NOT NULL
);
CREATE UNIQUE INDEX quizzes_key_qid_quid ON quizzes_composite_key (qid, quid);


CREATE TABLE assigned_users_quizzes (
    user_id NUMBER REFERENCES users(user_id) NOT NULL,
    qid NUMBER REFERENCES quizzes(qid) NOT NULL,
    assigned TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
CREATE UNIQUE INDEX users_quizzes_user_id_qid ON assigned_users_quizzes (user_id, qid);


CREATE TABLE assigned_groups_quizzes (
    gid NUMBER REFERENCES groups(gid) NOT NULL,
    qid NUMBER REFERENCES quizzes(qid) NOT NULL,
    assigned TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
CREATE UNIQUE INDEX groups_quizzes_gid_qid ON assigned_groups_quizzes (gid, qid);


CREATE TABLE results (
    user_id NUMBER REFERENCES users(user_id) NOT NULL,
    qid NUMBER REFERENCES quizzes(qid) NOT NULL,
    grade NUMBER,
    answers VARCHAR2(4000) NOT NULL,
    finished TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
CREATE UNIQUE INDEX results_user_id_qid ON results (user_id, qid);


/*******************************************************
Stored Procedures
*******************************************************/

CREATE OR REPLACE PROCEDURE regist_user 
(
in_username IN VARCHAR2,
in_password IN VARCHAR2,
in_firstname IN VARCHAR2,
in_lastname IN VARCHAR2)
IS
BEGIN
    INSERT INTO users (user_id, username, password, firstname, lastname)
        VALUES (users_id_seq.NEXTVAL, in_username, in_password, in_firstname, in_lastname);
END;
/

CREATE OR REPLACE PROCEDURE regist_manager 
(
in_username IN VARCHAR2,
in_password IN VARCHAR2,
in_firstname IN VARCHAR2,
in_lastname IN VARCHAR2)
IS
BEGIN
    INSERT INTO users (user_id, type, username, password, firstname, lastname)
        VALUES (users_id_seq.NEXTVAL, 0, in_username, in_password, in_firstname, in_lastname);
END;
/

CREATE OR REPLACE PROCEDURE create_group 
(
in_group_name IN VARCHAR2)
IS
BEGIN
    INSERT INTO groups (gid, name)
        VALUES (groups_id_seq.NEXTVAL, in_group_name);
END;
/

/***********************
 Sample SQL statements
************************

select index_name, column_name
    from user_ind_columns
    where table_name = 'USERS';
    
CALL regist_user('jae', 'pass', 'Jae', 'Hwang');
CALL regist_manager('adm', 'pass', 'William', 'Gentry');
SELECT * FROM users;

CALL create_group('test group');
SELECT * FROM groups;
*/