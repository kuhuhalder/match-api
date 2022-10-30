package com.match.matchapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping("/api/students")
public class MatchController {

    @Autowired
    private MatchService matchService;

    @GetMapping(path = "/getAll")
    @ResponseBody
    @CrossOrigin(origins="http://localhost:3000")
    public List<Student> fetchAllStudents() {
        return matchService.getAllStudents();
    }

    @Transactional
    @PostMapping(path = "/add")
    @CrossOrigin(origins="http://localhost:3000")
    public ResponseEntity addStudent(@RequestBody Student student){
        if(matchService.addStudent(student)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "/validate/{userName}/{password}")
    @CrossOrigin(origins="http://localhost:3000")
    public ResponseEntity validation(@PathVariable String userName, @PathVariable String password){
        if (matchService.validation(userName, password)){
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }

    @GetMapping(path = "/getStudent/{userName}")
    @ResponseBody
    @CrossOrigin(origins="http://localhost:3000")
    public Student getStudent(@PathVariable String userName) throws JsonProcessingException {
        return matchService.getStudent(userName);
    }

    @Transactional
    @DeleteMapping(path = "/delete/{userName}")
    @CrossOrigin(origins="http://localhost:3000")
    public  ResponseEntity deleteStudent(@PathVariable String userName){
        if (matchService.deleteStudent(userName)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping(path = "/matches/{userName}")
    @ResponseBody
    @CrossOrigin(origins="http://localhost:3000")
    public List<Student> matchStudent(@PathVariable String userName) throws JsonProcessingException {

        Student studentObject= getStudent(userName);
        List<Student> students = matchService.matchStudents(studentObject);

//        String returnStudent = "[";
//        if(students == null || students.size() <= 0){
//            return "No matches";
//        }
//
//        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//        returnStudent  = ow.writeValueAsString(matchService.matchStudents(studentObject));
//        for(int i = 1; i<students.size(); i++) {
//            returnStudent = returnStudent + "," + ow.writeValueAsString(matchService.matchStudents(studentObject));
//        }
//        System.out.println(returnStudent);
//        returnStudent = returnStudent + "]";
//
//        return returnStudent;
        return students;

    }
    @PostMapping(path = "/update")
    @ResponseBody
    @CrossOrigin(origins="http://localhost:3000")
    public  ResponseEntity updateStudent(@RequestBody Student student){
        student.print();
        if (matchService.updateStudent(student)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
