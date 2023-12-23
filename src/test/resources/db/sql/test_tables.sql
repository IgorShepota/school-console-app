DROP TABLE IF EXISTS student_courses;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS groups;
DROP TABLE IF EXISTS courses;

CREATE TABLE groups
(
  group_id   SERIAL PRIMARY KEY,
  group_name VARCHAR(255) NOT NULL
);

CREATE TABLE students
(
  student_id SERIAL PRIMARY KEY,
  group_id   INTEGER REFERENCES groups (group_id),
  first_name VARCHAR(255) NOT NULL,
  last_name  VARCHAR(255) NOT NULL
);

CREATE TABLE courses
(
  course_id          SERIAL PRIMARY KEY,
  course_name        VARCHAR(255) NOT NULL,
  course_description TEXT
);

CREATE TABLE student_courses
(
  student_id INT,
  course_id  INT,
  PRIMARY KEY (student_id, course_id),
  FOREIGN KEY (student_id) REFERENCES students (student_id),
  FOREIGN KEY (course_id) REFERENCES courses (course_id)
);