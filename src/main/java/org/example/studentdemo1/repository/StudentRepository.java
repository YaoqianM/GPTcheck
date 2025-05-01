package org.example.studentdemo1.repository;
import org.example.studentdemo1.entity.Student;
//import org.springframework.data.repository.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
}
