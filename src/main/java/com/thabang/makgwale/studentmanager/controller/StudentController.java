package com.thabang.makgwale.studentmanager.controller;

import com.thabang.makgwale.studentmanager.model.ApiResponse;
import com.thabang.makgwale.studentmanager.model.Score;
import com.thabang.makgwale.studentmanager.model.Student;
import com.thabang.makgwale.studentmanager.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/getStudent/{id}")
    public ResponseEntity<?> getStudentById(@Valid @PathVariable Long id){
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @GetMapping(value = "/students")
    public ResponseEntity<?> getStudents(){
        System.out.println("getting students");
        return ResponseEntity.ok(studentService.getAllStudent());
    }

    @PostMapping(value = "/createStudent")
    public ApiResponse<?> createStudent(@Valid @RequestBody Student student, BindingResult result){
        if(result.hasErrors())
            return new ApiResponse<>(false,"Invalid Student deetails",null);
        try{
           return new ApiResponse<>(true,"successfully created a student",studentService.createStudent(student));
        }catch (Exception e){
            return new ApiResponse<>(false,"[ERROR]"+e.getMessage(),null);
        }
    }

    @DeleteMapping(value = "/deleteStudent/{id}")
    public ResponseEntity<?> deleteStudent(@Valid @PathVariable Long id){
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/updateEmployee/{id}")
    public ResponseEntity<?> updateEmployee(@Valid @PathVariable Long id, @Valid @RequestBody Student student,BindingResult result){
        if (result.hasErrors())
            return ResponseEntity.badRequest().body("Invalid student details");

        return ResponseEntity.ok(studentService.updateStudent(student,id));
    }

    @PostMapping("/createScore/{id}")
    public Score addScore(@Valid @RequestBody Score score,@Valid @PathVariable Long id){
        return studentService.createScore(id,score);
    }

    @GetMapping("/searchStudent/{searchCriteria}/{searchTerm}")
    public ResponseEntity<?> searchStudents(@Valid @PathVariable String searchCriteria, @Valid  @PathVariable String searchTerm) {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.searchStudents(searchCriteria, searchTerm));
    }
}
