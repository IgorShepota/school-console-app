package ua.foxminded.schoolconsoleapp.entitу;

import java.util.Objects;

public class Course {

  private Integer id;
  private String courseName;

  private Course(Builder builder) {
    this.id = builder.id;
    this.courseName = builder.courseName;
  }

  public Integer getId() {
    return id;
  }

  public String getCourseName() {
    return courseName;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static Builder builder(Course course) {
    return new Builder(course);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Course course = (Course) o;
    return Objects.equals(id, course.id) &&
        Objects.equals(courseName, course.courseName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getCourseName());
  }

  @Override
  public String toString() {
    return "Course{" +
        "courseId=" + id +
        ", courseName='" + courseName + '\'' +
        ", courseDescription='" + '\'' +
        '}';
  }

  public static class Builder {

    private Integer id;
    private String courseName;

    private Builder() {
    }

    private Builder(Course course) {
      this.id = course.id;
      this.courseName = course.courseName;
    }

    public Course build() {
      return new Course(this);
    }

    public Builder withId(Integer courseId) {
      this.id = courseId;
      return this;
    }

    public Builder withCourseName(String courseName) {
      this.courseName = courseName;
      return this;
    }
  }

}
