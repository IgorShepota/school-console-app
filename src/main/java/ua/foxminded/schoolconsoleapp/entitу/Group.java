package ua.foxminded.schoolconsoleapp.entitу;

import java.util.Objects;

public class Group {

  private Integer id;
  private String groupName;

  private Group(Builder builder) {
    this.id = builder.id;
    this.groupName = builder.groupName;
  }

  public Integer getId() {
    return id;
  }

  public String getGroupName() {
    return groupName;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static Builder builder(Group group) {
    return new Builder(group);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Group group = (Group) o;
    return id == group.id &&
        Objects.equals(groupName, group.groupName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, groupName);
  }

  @Override
  public String toString() {
    return "Group{" +
        "groupId=" + id +
        ", groupName='" + groupName + '\'' +
        '}';
  }

  public static class Builder {

    private Integer id;
    private String groupName;

    private Builder() {
    }

    private Builder(Group group) {
      this.id = group.id;
      this.groupName = group.groupName;
    }

    public Group build() {
      return new Group(this);
    }

    public Builder withId(Integer groupId) {
      this.id = groupId;
      return this;
    }

    public Builder withGroupName(String groupName) {
      this.groupName = groupName;
      return this;
    }
  }

}
