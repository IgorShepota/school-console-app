package ua.foxminded.schoolconsoleapp.entitу;

import java.util.Objects;

public class Student {

  private Integer id;
  private Integer groupId;
  private String firstName;
  private String lastName;

  private Student(Builder builder) {
    this.id = builder.id;
    this.groupId = builder.groupId;
    this.firstName = builder.firstName;
    this.lastName = builder.lastName;
  }

  public Integer getId() {
    return id;
  }

  public Integer getGroupId() {
    return groupId;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static Builder builder(Student student) {
    return new Builder(student);
  }

  public static Student copy(Student entity, String encodepassword) {
    return new Student(null);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Student student = (Student) o;
    return Objects.equals(id, student.id) &&
        Objects.equals(groupId, student.groupId) &&
        Objects.equals(firstName, student.firstName) &&
        Objects.equals(lastName, student.lastName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getGroupId(), getFirstName(), getLastName());
  }

  @Override
  public String toString() {
    return "Student{" +
        "id=" + id +
        ", groupId=" + groupId +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        '}';
  }

  public static class Builder {

    private Integer id;
    private Integer groupId;
    private String firstName;
    private String lastName;

    private Builder() {
    }

    private Builder(Student student) {
      this.id = student.id;
      this.groupId = student.groupId;
      this.firstName = student.firstName;
      this.lastName = student.lastName;
    }

    public Student build() {
      return new Student(this);
    }

    public Builder withId(Integer studentId) {
      this.id = studentId;
      return this;
    }

    public Builder withGroupId(Integer groupId) {
      this.groupId = groupId;
      return this;
    }

    public Builder withFirstName(String firstName) {
      this.firstName = firstName;
      return this;
    }

    public Builder withLastName(String lastName) {
      this.lastName = lastName;
      return this;
    }
  }

}
