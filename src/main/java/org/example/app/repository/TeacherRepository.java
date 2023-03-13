package org.example.app.repository;

import org.example.app.entities.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface TeacherRepository extends UserRepository<Teacher>, JpaRepository<Teacher, Long> {

    @Modifying
    @Query("select count('teacher_id') from Teacher as t")
    Integer countAllById();

    @Override
    Page<Teacher> findAll(Pageable pageable);
}
