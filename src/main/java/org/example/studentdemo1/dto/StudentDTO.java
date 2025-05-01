package org.example.studentdemo1.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data  // Generates getters, setters, toString, equals, hashCode
@NoArgsConstructor  // Generates no-args constructor
@AllArgsConstructor // Generates all-args constructor
public class StudentDTO {
    private long id;
    private String firstName;
    private String lastName;
    private int age;
    private String gender;
    private String email;

    public StudentDTO(long id, String firstName, String lastName, String email) {
    }
}
