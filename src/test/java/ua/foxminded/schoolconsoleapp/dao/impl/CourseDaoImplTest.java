package ua.foxminded.schoolconsoleapp.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import ua.foxminded.schoolconsoleapp.dao.DaoTestBase;
import ua.foxminded.schoolconsoleapp.entitу.Course;
import ua.foxminded.schoolconsoleapp.entitу.Student;

class CourseDaoImplTest extends DaoTestBase {

  @Test
  void checkStudentEnrolledInCourseShouldWorkCorrectlyIfStudentAndCourseExist() {
    int studentId = 1;
    int courseId = 1;

    assertThat(courseDao.checkStudentEnrolledInCourse(studentId, courseId)).isTrue();
  }

  @Test
  void checkStudentEnrolledInCourseShouldWorkCorrectlyIfStudentNotEnrolled() {
    int studentId = 1;
    int courseId = 50;

    assertThat(courseDao.checkStudentEnrolledInCourse(studentId, courseId)).isFalse();
  }

  @Test
  void enrollStudentToCourseShouldWorkCorrectlyIfStudentAndCourseExist() {
    Student student = entityManager.find(Student.class, 3);
    Course course = entityManager.find(Course.class, 5);

    courseDao.enrollStudentToCourse(student, course);

    assertThat(courseDao.checkStudentEnrolledInCourse(student.getId(), course.getId())).isTrue();
  }

  @Test
  void removeStudentFromCourseShouldWorkCorrectlyIfStudentAndCourseExist() {
    Student student = entityManager.find(Student.class, 1);
    Course course = entityManager.find(Course.class, 1);

    courseDao.removeStudentFromCourse(student, course);

    assertThat(courseDao.checkStudentEnrolledInCourse(student.getId(), course.getId())).isFalse();
  }

  @Test
  void getCourseIdByNameShouldWorkCorrectlyIfCourseExists() {
    String courseName = "Art";
    int courseId = 9;

    assertThat(courseDao.getCourseIdByName(courseName)).isPresent()
        .hasValueSatisfying(retrieved -> assertThat(retrieved.getId()).isEqualTo(courseId));
  }

  @Test
  void getCourseIdByNameShouldReturnEmptyListIfCourseNotExists() {
    String courseName = "Algebra";

    assertThat(courseDao.getCourseIdByName(courseName)).isEmpty();
  }

  @Test
  void getEnrolledCoursesForStudentShouldWorkCorrectlyIfStudentAndCourseExist() {
    int studentId = 1;
    List<Course> courses = courseDao.getEnrolledCoursesForStudent(studentId);

    assertThat(courses).isNotEmpty()
        .hasSize(1)
        .allSatisfy(course -> assertThat(course.getCourseName()).isEqualTo("Mathematics"));
  }

  @Test
  void getEnrolledCoursesForStudentShouldShouldReturnEmptyListIfCourseNotExists() {
    int studentId = 10;
    List<Course> courses = courseDao.getEnrolledCoursesForStudent(studentId);

    assertThat(courses).isEmpty();
  }

  @Test
  void saveShouldWorkCorrectlyWhileCourseEntityIsCorrect() {
    Course course = Course.builder()
        .withCourseName("Algebra")
        .build();

    courseDao.save(course);

    assertThat(courseDao.findById(course.getId())).isPresent()
        .hasValueSatisfying(retrieved -> {
          assertThat(retrieved.getId()).isEqualTo(course.getId());
          assertThat(retrieved.getCourseName()).isEqualTo(course.getCourseName());
        });
  }

  @Test
  void findByIdShouldWorkCorrectlyIfCourseExists() {
    int courseIdToFind = 1;

    assertThat(courseDao.findById(courseIdToFind)).isPresent()
        .hasValueSatisfying(retrieved -> {
          assertThat(retrieved.getId()).isEqualTo(1);
          assertThat(retrieved.getCourseName()).isEqualTo("Mathematics");
        });
  }

  @Test
  void findByIdShouldWorkCorrectlyIfCourseNoFound() {
    int nonExistentId = 50;

    assertThat(courseDao.findById(nonExistentId)).isEmpty();
  }

  @Test
  void findAllShouldWorkCorrectlyIfCoursesExist() {
    List<Course> courses = courseDao.findAll();

    assertThat(courses).isNotEmpty()
        .hasSize(10)
        .allSatisfy(course -> {
          assertThat(course.getId()).isNotNull();
          assertThat(course.getCourseName()).isNotNull();
        });
  }

  @Test
  void findAllWithPaginationShouldWorkCorrectlyIfStudentsExist() {
    List<Course> courses = courseDao.findAll(1, 2);

    assertThat(courses).isNotEmpty()
        .hasSize(2)
        .allMatch(course ->
            (course.getId() == 1 || course.getId() == 2) &&
                (course.getCourseName().equals("Mathematics") || course.getCourseName()
                    .equals("Biology"))
        );
  }

  @Test
  void updateShouldWorkCorrectlyIfCourseExists() {
    Course course = Course.builder()
        .withId(1)
        .withCourseName("Algebra")
        .build();

    courseDao.update(course);

    assertThat(courseDao.findById(course.getId())).isPresent()
        .hasValueSatisfying(updatedCourse -> {
          assertThat(updatedCourse.getId()).isEqualTo(course.getId());
          assertThat(updatedCourse.getCourseName()).isEqualTo(course.getCourseName());
        });
  }

  @Test
  void deleteShouldWorkCorrectlyIfCourseExists() {
    int courseId = 1;
    courseDao.deleteById(courseId);

    assertThat(courseDao.findById(courseId)).isNotPresent();
  }

  @Test
  void deleteShouldWorkCorrectlyIfCourseNotExists() {
    int courseId = 50;

    assertThat(courseDao.deleteById(courseId)).isFalse();
  }

}
