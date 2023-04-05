package com.thabang.makgwale.studentmanager.repository;

import com.thabang.makgwale.studentmanager.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByStudentNumber(String studentNumber);

    //List<Student> findByStudentNumberContainingAndFirstNameContainingAndLastNameContainingAndEmailAddressContaining();
}
