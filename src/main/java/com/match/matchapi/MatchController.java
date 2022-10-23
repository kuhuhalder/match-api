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

    @GetMapping
    public List<Student> fetchAllStudents() {
        return matchService.getAllStudents();
    }

    @Transactional
    @PostMapping
    @CrossOrigin(origins="http://localhost:3000")
    public ResponseEntity addStudent(@RequestBody Student student){
        if(matchService.addStudent(student)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "/{userName}/{password}")
    @CrossOrigin(origins="http://localhost:3000")
    public ResponseEntity validation(@PathVariable String userName, @PathVariable String password){
        if (matchService.validation(userName, password)){
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }

    @GetMapping(path = "/{userName}")
    @ResponseBody
    public String getStudent(@PathVariable String userName) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(matchService.getStudent(userName));
    }

    @Transactional
    @DeleteMapping(path = "/{userName}/{password}")
    @CrossOrigin(origins="http://localhost:3000")
    public  ResponseEntity deleteStudent(@PathVariable String userName, @PathVariable String password){
        if (matchService.deleteStudent(userName, password)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping(path = "/matches")
    public String matchStudent(@RequestBody Student student) throws JsonProcessingException {
        System.out.println("GOT HERE");
        List<Student> students = matchService.matchStudents(student);
        System.out.println(students);
        String returnStudent = "[";
        if(students == null || students.size() <= 0){
            return "No matches";
        }

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        returnStudent  = ow.writeValueAsString(matchService.matchStudents(student));
        for(int i = 1; i<students.size(); i++) {
            returnStudent = returnStudent + "," + ow.writeValueAsString(matchService.matchStudents(student));
        }
        return returnStudent + "]";

    }
}
