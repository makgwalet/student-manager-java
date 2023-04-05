package com.thabang.makgwale.studentmanager.service.impl;

import com.thabang.makgwale.studentmanager.exception.StudentNotFoundException;
import com.thabang.makgwale.studentmanager.model.Score;
import com.thabang.makgwale.studentmanager.model.Student;
import com.thabang.makgwale.studentmanager.repository.ScoreRepository;
import com.thabang.makgwale.studentmanager.repository.StudentRepository;
import com.thabang.makgwale.studentmanager.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;


@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    private static final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);


    @Override
    public List<Student> getAllStudent() {
        return studentRepository.findAll();
    }

    @Override //Method to retrieve student by ID
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id " + id));
    }
    @Override //Method to create student
    public Student createStudent(Student student) throws ParseException {

        //if the current score is null, set the score to 0 and avarage to 0 but the zero is not saved to the score storage
        student.setCurrent_score(student.getCurrent_score() == null ? 0 : student.getCurrent_score());
        student.setAverage_score(student.getCurrent_score().doubleValue());
        student.setStudentNumber(generateStudentNumber(student.getFirst_name(),student.getLast_name()));

        return studentRepository.save(student);
    }

    @Override
    public Student updateStudent(Student studentDetails, Long id) {
        log.info("updating details for student id:{}",id);
        Student existingStudent = getStudentById(id);

        existingStudent.setFirst_name(studentDetails.getFirst_name());
        existingStudent.setLast_name(studentDetails.getLast_name());
        existingStudent.setDateOfBirth(studentDetails.getDateOfBirth());
        existingStudent.setMobile_number(studentDetails.getMobile_number());
        existingStudent.setEmail_address(studentDetails.getEmail_address());
        //save updates
        return studentRepository.save(existingStudent);
    }


    //Method to delete student by id
    @Override
    public void deleteStudent(Long id) {
        log.info("deleting student no: {} ", id);
        Student existingStudent = getStudentById(id);
        studentRepository.delete(existingStudent);
    }

    @Override //method to search for student // using filter to filter the get all method by different search terms
    public List<Student> searchStudents(String searchCriteria,String searchTerm) {
        log.info("searching student [search criteria:{}], [search term:{}]",searchCriteria,searchTerm);
        List<Student> students = studentRepository.findAll();
        switch (searchCriteria) {
            case "StudentNumber":
                return students.stream()
                        .filter(student -> student.getStudentNumber().contains(searchTerm))
                        .collect(Collectors.toList());
            case "FirstName":
                return students.stream()
                        .filter(student -> student.getFirst_name().contains(searchTerm))
                        .collect(Collectors.toList());
            case "LastName":
                return students.stream()
                        .filter(student -> student.getLast_name().contains(searchTerm))
                        .collect(Collectors.toList());
            case "EmailAddress":
                return students.stream()
                        .filter(student -> student.getEmail_address().contains(searchTerm))
                        .collect(Collectors.toList());
            default:
                throw new StudentNotFoundException("No student found");
        }

    }

    @Override //method to create or add score for student.
    public Score createScore(Long studentId, Score score) {
        log.info("create/add score for student id: {}",studentId);
        Student student = getStudentById(studentId);

        // Check if score is valid
        if (score.getScore() < 0 || score.getScore() > 100) {
            throw new IllegalArgumentException("Score must be between 0 and 100");
        }

        // Update student's current score and average score
        List<Score> scores = scoreRepository.findByStudentId(studentId);
        double totalScore = score.getScore();
        for (Score s : scores) {
            totalScore += s.getScore();
        }
        double averageScore = totalScore / (scores.size() + 1);
        student.setCurrent_score(score.getScore());
        student.setAverage_score(averageScore);

        // Save score and update student
        score.setStudent(student);
        score.setDate(LocalDate.now());
        Score savedScore = scoreRepository.save(score);
        studentRepository.save(student);
        return savedScore;
    }

    //Methode to generate Student number
    private String generateStudentNumber(String firstName, String lastName) {
        log.info("generate student number");
        // Generate student number by concatenating first letter of first name and last name
        int randomInt = ThreadLocalRandom.current().nextInt(100000, 1000000);
        String studentNumber = firstName.substring(0, 1).toUpperCase() +randomInt+ lastName.substring(0, 1).toUpperCase();
        // Check if student number already exists
        //if exist gerenate a new student number
        Optional<Student> existingStudent = studentRepository.findByStudentNumber(studentNumber);
        if (existingStudent.isPresent()) {
            randomInt = ThreadLocalRandom.current().nextInt(100000, 1000000);
            return firstName.substring(0, 1).toUpperCase() +randomInt+ lastName.substring(0, 1).toUpperCase();
        }
        return studentNumber;
    }
}
