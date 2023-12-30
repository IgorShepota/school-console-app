INSERT INTO groups (group_name)
VALUES ('CF-64');
INSERT INTO groups (group_name)
VALUES ('YU-99');
INSERT INTO groups (group_name)
VALUES ('VJ-97');

INSERT INTO courses (course_name)
VALUES ('Mathematics');
INSERT INTO courses (course_name)
VALUES ('Biology');
INSERT INTO courses (course_name)
VALUES ('Chemistry');

INSERT INTO students (first_name, last_name, group_id)
VALUES ('Amanda', 'Williams', 2);
INSERT INTO students (first_name, last_name, group_id)
VALUES ('James', 'Taylor', 1);
INSERT INTO students (first_name, last_name, group_id)
VALUES ('David', 'Smith', 1);
INSERT INTO students (first_name, last_name, group_id)
VALUES ('James', 'Wilson', 1);
INSERT INTO students (first_name, last_name, group_id)
VALUES ('Robert', 'Jones', 2);
INSERT INTO students (first_name, last_name, group_id)
VALUES ('Michael', 'Williams', 2);
INSERT INTO students (first_name, last_name, group_id)
VALUES ('Michael', 'Taylor', 3);
INSERT INTO students (first_name, last_name, group_id)
VALUES ('Robert', 'Davis', 3);
INSERT INTO students (first_name, last_name, group_id)
VALUES ('James', 'Jones', 2);
INSERT INTO students (first_name, last_name)
VALUES ('William', 'Wilson');

INSERT INTO student_courses (student_id, course_id)
VALUES (1, 3);
INSERT INTO student_courses (student_id, course_id)
VALUES (1, 1);
INSERT INTO student_courses (student_id, course_id)
VALUES (1, 2);
INSERT INTO student_courses (student_id, course_id)
VALUES (2, 1);
INSERT INTO student_courses (student_id, course_id)
VALUES (3, 1);
INSERT INTO student_courses (student_id, course_id)
VALUES (3, 2);
INSERT INTO student_courses (student_id, course_id)
VALUES (4, 2);
INSERT INTO student_courses (student_id, course_id)
VALUES (5, 3);
INSERT INTO student_courses (student_id, course_id)
VALUES (6, 3);
INSERT INTO student_courses (student_id, course_id)
VALUES (6, 2);
INSERT INTO student_courses (student_id, course_id)
VALUES (6, 1);
INSERT INTO student_courses (student_id, course_id)
VALUES (7, 3);
INSERT INTO student_courses (student_id, course_id)
VALUES (7, 1);
INSERT INTO student_courses (student_id, course_id)
VALUES (7, 2);
INSERT INTO student_courses (student_id, course_id)
VALUES (8, 2);
INSERT INTO student_courses (student_id, course_id)
VALUES (9, 2);
INSERT INTO student_courses (student_id, course_id)
VALUES (9, 3);
INSERT INTO student_courses (student_id, course_id)
VALUES (9, 1);
INSERT INTO student_courses (student_id, course_id)
VALUES (10, 1);