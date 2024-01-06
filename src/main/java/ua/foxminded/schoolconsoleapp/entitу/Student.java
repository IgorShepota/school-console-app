package ua.foxminded.schoolconsoleapp.entitу;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class Student {

  private Integer id;
  private Integer groupId;
  private String firstName;
  private String lastName;

}
