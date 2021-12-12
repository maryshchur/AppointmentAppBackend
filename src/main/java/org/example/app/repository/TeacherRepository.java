package org.example.app.repository;

import org.example.app.entities.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository extends UserRepository<Teacher>, JpaRepository<Teacher, Long> {

    @Override
    Page<Teacher> findAll(Pageable pageable);
}
