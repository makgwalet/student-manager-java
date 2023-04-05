package com.thabang.makgwale.studentmanager.service;

import com.thabang.makgwale.studentmanager.model.Score;
import com.thabang.makgwale.studentmanager.model.Student;

import java.text.ParseException;
import java.util.List;

public interface StudentService {

    Student getStudentById(Long id);
    List<Student> getAllStudent();

    Student createStudent(Student student) throws ParseException;

    Student updateStudent(Student student, Long id);

    Score createScore(Long studentId, Score score);

    void deleteStudent(Long id);

    List<Student> searchStudents(String searchCriteria,String searchTerm);
}
