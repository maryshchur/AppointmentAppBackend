package org.example.app.repository;

import org.example.app.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends UserRepository<Student>, JpaRepository<Student, Long> {
}
