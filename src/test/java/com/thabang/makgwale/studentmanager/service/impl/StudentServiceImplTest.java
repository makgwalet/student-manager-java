package com.thabang.makgwale.studentmanager.service.impl;

import com.thabang.makgwale.studentmanager.exception.StudentNotFoundException;
import com.thabang.makgwale.studentmanager.model.Score;
import com.thabang.makgwale.studentmanager.model.Student;
import com.thabang.makgwale.studentmanager.repository.ScoreRepository;
import com.thabang.makgwale.studentmanager.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
 class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ScoreRepository scoreRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setStudentNumber("123");
        student.setId(1L);
        student.setFirst_name("John");
        student.setLast_name("Doe");
        student.setDateOfBirth(new Date());
        student.setMobile_number("123456789");
        student.setEmail_address("john.doe@example.com");
    }

    @Test
    void testGetAllStudent() {
        List<Student> students = new ArrayList<>();
        students.add(student);

        Mockito.when(studentRepository.findAll()).thenReturn(students);

        List<Student> result = studentService.getAllStudent();

        assertEquals(students, result);
    }

    @Test
    void testGetStudentById() {
        Mockito.when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Student result = studentService.getStudentById(1L);

        assertEquals(student, result);
    }

    @Test
    void testCreateStudent() {
        Student newStudent = new Student();
        newStudent.setFirst_name("Jane");
        newStudent.setLast_name("Doe");
        newStudent.setDateOfBirth(new Date(2002, Calendar.MARCH,2));
        newStudent.setMobile_number("987654321");
        newStudent.setEmail_address("jane.doe@example.com");

        Mockito.when(studentRepository.save(Mockito.any(Student.class))).thenReturn(newStudent);

        Student result = studentService.createStudent(newStudent);

        assertEquals(newStudent, result);
    }

    @Test
    void testUpdateStudent() {
        Student updatedStudent = new Student();
        updatedStudent.setId(1L);
        updatedStudent.setFirst_name("Jane");
        updatedStudent.setLast_name("Doe");
        updatedStudent.setDateOfBirth( new Date(2002, Calendar.MARCH,2));
        updatedStudent.setMobile_number("987654321");
        updatedStudent.setEmail_address("jane.doe@example.com");

        Mockito.when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        Mockito.when(studentRepository.save(Mockito.any(Student.class))).thenReturn(updatedStudent);

        Student result = studentService.updateStudent(updatedStudent, 1L);

        assertEquals(updatedStudent, result);
    }

    @Test
    void testDeleteStudent() {
        Mockito.when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        assertDoesNotThrow(() -> studentService.deleteStudent(1L));

        Mockito.verify(studentRepository, Mockito.times(1)).delete(Mockito.any(Student.class));
    }

    @Test
    void testGetStudentByIdNotFound() {
        Long id = 1L;
        when(studentRepository.findById(id)).thenReturn(Optional.empty());
        assertThatExceptionOfType(StudentNotFoundException.class)
                .isThrownBy(() -> studentService.getStudentById(id))
                .withMessage("Student not found with id " + id);
        verify(studentRepository).findById(id);
    }



    @Test
    void testCreateScore() {
        Score score = new Score();
        score.setScore(90);

        Mockito.when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        Mockito.when(scoreRepository.findByStudentId(1L)).thenReturn(Collections.emptyList());
        Mockito.when(scoreRepository.save(Mockito.any(Score.class))).thenReturn(score);

        Score result = studentService.createScore(1L, score);

        assertEquals(score, result);
    }

    @Test
    void testSearchStudents() {
        List<Student> students = new ArrayList<>();
        students.add(student);

        Mockito.when(studentRepository.findAll()).thenReturn(students);

        List<Student> resultStudentNumber = studentService.searchStudents("StudentNumber", "123");
        List<Student> resultFirstName = studentService.searchStudents("FirstName", "John");
        List<Student> resultLastName = studentService.searchStudents("LastName", "Doe");
        List<Student> resultEmailAddress = studentService.searchStudents("EmailAddress", "john.doe@example.com");

        assertEquals(students, resultStudentNumber);
        assertEquals(students, resultFirstName);
        assertEquals(students, resultLastName);
        assertEquals(students, resultEmailAddress);
    }

    @Test
    void testSearchStudentsWithInvalidSearchCriteria() {

        String searchCriteria = "InvalidCriteria";
        String searchTerm = "foo";
        when(studentRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(StudentNotFoundException.class, () -> studentService.searchStudents(searchCriteria, searchTerm));
    }
}
