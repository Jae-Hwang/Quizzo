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
    s1 VARCHAR2(100),
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

CREATE TABLE answers (
    quid NUMBER REFERENCES questions(quid),
    answer VARCHAR2(100) NOT NULL
);
CREATE INDEX answers_quid ON answers (quid);


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

CREATE OR REPLACE PROCEDURE create_quiz 
(
in_quiz_name IN VARCHAR2,
in_created_by IN NUMBER)
IS
BEGIN
    INSERT INTO quizzes (qid, name, created_by)
        VALUES (quizzes_id_seq.NEXTVAL, in_quiz_name, in_created_by);
END;
/

CREATE OR REPLACE PROCEDURE create_question_multi
(
    in_desc IN VARCHAR2,
    in_s1 IN VARCHAR2,
    in_s2 IN VARCHAR2,
    in_s3 IN VARCHAR2,
    in_s4 IN VARCHAR2,
    in_s5 IN VARCHAR2,
    in_catagory IN VARCHAR2,
    in_created_by IN NUMBER,
    in_answer IN VARCHAR2)
IS
    gen_id NUMBER;
BEGIN
    SELECT (QUESTIONS_ID_SEQ.nextval) INTO gen_id FROM dual;
    INSERT INTO questions (quid, description, type, s1, s2, s3, s4, s5, catagory, created_by)
        VALUES (gen_id, in_desc, 1, in_s1, in_s2, in_s3, in_s4, in_s5, in_catagory, in_created_by);
    INSERT INTO answers (quid, answer)
        VALUES (gen_id, in_answer);
END;
/

CREATE OR REPLACE PROCEDURE create_question_truefalse
(
    in_desc IN VARCHAR2,
    in_catagory IN VARCHAR2,
    in_created_by IN NUMBER,
    in_answer IN VARCHAR2)
IS
    gen_id NUMBER;
BEGIN
    SELECT (QUESTIONS_ID_SEQ.nextval) INTO gen_id FROM dual;
    INSERT INTO questions (quid, description, type, catagory, created_by)
        VALUES (gen_id, in_desc, 2, in_catagory, in_created_by);
    INSERT INTO answers (quid, answer)
        VALUES (gen_id, in_answer);
END;
/

CREATE OR REPLACE PROCEDURE create_question_short
(
    in_desc IN VARCHAR2,
    in_catagory IN VARCHAR2,
    in_created_by IN NUMBER,
    in_answer IN VARCHAR2)
IS
    gen_id NUMBER;
BEGIN
    SELECT (QUESTIONS_ID_SEQ.nextval) INTO gen_id FROM dual;
    INSERT INTO questions (quid, description, type, catagory, created_by)
        VALUES (gen_id, in_desc, 3, in_catagory, in_created_by);
    INSERT INTO answers (quid, answer)
        VALUES (gen_id, in_answer);
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

CALL create_question_multi
(
    'Test Question',
    'a',
    'b',
    'c',
    'd',
    'e',    
    'test',
    1,
    '1'
);

SELECT * FROM questions;
SELECT * FROM answers;
SELECT * FROM questions
    JOIN answers USING (quid);

SELECT user_id, username, firstname, lastname
    FROM
        (SELECT users.user_id, users.username, users.firstname, users.lastname, row_number()
            over (ORDER BY users.user_id ASC) line_number
            FROM users
            WHERE users.type = 1 AND users.active = 1)
        WHERE line_number BETWEEN 1 AND 10 ORDER BY line_number;


CALL create_question_truefalse
    ('The first name of the trainer for Performance Engineering Batch is Williams.',
    'Test',
    50,
    't');
    
CALL create_question_truefalse
    ('My name is Jae.',
    'Test',
    50,
    'f');
    
CALL create_question_truefalse
    ('My name is Boris.',
    'Test',
    50,
    'f');
    
CALL create_question_truefalse
    ('Angular is lie.',
    'Test',
    50,
    't');
    
CALL create_question_truefalse
    ('This app is amazing.',
    'Test',
    50,
    'f');
    
CALL create_question_short
    ('What is name of the trainer for Performance Engineering Batch?',
    'Test',
    50,
    'Williams');
    
CALL create_question_short
    ('What is Angular?',
    'Test',
    50,
    'Lie');


INSERT INTO quizzes (qid, name, created_by)
    VALUES (QUESTIONS_ID_SEQ.nextval, 'Test Quiz', 50);


INSERT INTO quizzes_composite_key (qid, quid)
    VALUES (15, 8);
    
INSERT INTO quizzes_composite_key (qid, quid)
    VALUES (15, 9);
    
INSERT INTO quizzes_composite_key (qid, quid)
    VALUES (15, 10);

SELECT * FROM questions
    JOIN quizzes_composite_key using (quid)
    WHERE qid = 15;

SELECT quid, answer FROM answers
    JOIN questions using (quid)
    JOIN quizzes_composite_key using (quid)
    WHERE qid = 15;
*/