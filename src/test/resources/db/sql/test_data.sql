INSERT INTO groups (group_name)
VALUES ('XV-46'),
       ('VC-09'),
       ('BB-02');


INSERT INTO courses (course_name, course_description)
VALUES ('Mathematics', ''),
       ('Biology', ''),
       ('Chemistry', ''),
       ('Physics', ''),
       ('History', ''),
       ('Literature', ''),
       ('Geography', ''),
       ('Computer Science', ''),
       ('Art', ''),
       ('Music', '');

INSERT INTO students (group_id, first_name, last_name)
VALUES (1, 'Elizabeth', 'Harris'),
       (1, 'Laura', 'Taylor'),
       (2, 'James', 'Smith');

INSERT INTO student_courses (student_id, course_id)
VALUES (1, 1),
       (2, 1);