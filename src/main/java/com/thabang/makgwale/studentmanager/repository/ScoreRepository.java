package com.thabang.makgwale.studentmanager.repository;

import com.thabang.makgwale.studentmanager.model.Score;
import com.thabang.makgwale.studentmanager.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreRepository extends JpaRepository<Score,Long> {
    List<Score> findByStudentId(long student_id);

}
