package com.match.matchapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping("/api/students")
public class MatchController {

    @Autowired
    private MatchService matchService;

    @GetMapping(path = "/getAll")
    @ResponseBody
    @CrossOrigin(origins={"http://localhost:3000", "https://match-swensational.netlify.app"})
    public List<Student> fetchAllStudents() {
        return matchService.getAllStudents();
    }

    @Transactional
    @PostMapping(path = "/add")
    @CrossOrigin(origins={"http://localhost:3000", "https://match-swensational.netlify.app"})
    public ResponseEntity addStudent(@RequestBody Student student){
        if(matchService.addStudent(student)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }

    @Transactional
    @PostMapping(path = "/matchAdd")
    @CrossOrigin(origins={"http://localhost:3000", "https://match-swensational.netlify.app"})
    public ResponseEntity addMatch(@RequestBody Matches match){
        int resultVar = matchService.addMatches(match);
        if(resultVar != -1 && resultVar != 2 && resultVar != 3) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "/validate/{userName}/{password}")
    @CrossOrigin(origins={"http://localhost:3000", "https://match-swensational.netlify.app"})
    public ResponseEntity validation(@PathVariable String userName, @PathVariable String password){
        if (matchService.validation(userName, password)){
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
//        return ResponseEntity.ok((matchService.validation(userName, password)));
    }

    @GetMapping(path = "/getStudent/{userName}")
    @ResponseBody
    @CrossOrigin(origins={"http://localhost:3000", "https://match-swensational.netlify.app"})
    public Student getStudent(@PathVariable String userName) throws JsonProcessingException {
        return matchService.getStudent(userName);
    }

    @Transactional
    @DeleteMapping(path = "/delete/{userName}")
    @CrossOrigin(origins={"http://localhost:3000", "https://match-swensational.netlify.app"})
    public  ResponseEntity deleteStudent(@PathVariable String userName){
        if (matchService.deleteStudent(userName)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping(path = "/matches/{userName}")
    @ResponseBody
    @CrossOrigin(origins={"http://localhost:3000", "https://match-swensational.netlify.app"})
    public List<Student> findBuddies(@PathVariable String userName) throws JsonProcessingException {

        //Student studentObject= getStudent(userName);
        List<Student> students = matchService.findBuddies(userName);

        return students;

    }
    @PostMapping(path = "/update")
    @ResponseBody
    @CrossOrigin(origins={"http://localhost:3000", "https://match-swensational.netlify.app"})
    public  ResponseEntity updateStudent(@RequestBody Student student){
        if (matchService.updateStudent(student)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping(path = "areMatched/{userName1}/{userName2}")
    @CrossOrigin(origins={"http://localhost:3000", "https://match-swensational.netlify.app"})
    @ResponseBody
    public int matchExists(@PathVariable String userName1, @PathVariable String userName2){
        return matchService.matchExists(userName1, userName2);
    }

    @GetMapping(path = "findRequests/{userName}")
    @CrossOrigin(origins={"http://localhost:3000", "https://match-swensational.netlify.app"})
    @ResponseBody
    public List<Student> findRequests(@PathVariable String userName){
        return matchService.findRequests(userName);
    }

    @GetMapping(path = "findConfirmedMatches/{userName}")
    @CrossOrigin(origins={"http://localhost:3000", "https://match-swensational.netlify.app"})
    @ResponseBody
    public List<Student> findConfirmedMatches(@PathVariable String userName){
        return matchService.findConfirmedMatches(userName);
    }

    @GetMapping(path = "findRequestsSent/{userName}")
    @CrossOrigin(origins={"http://localhost:3000", "https://match-swensational.netlify.app"})
    @ResponseBody
    public List<Student> findRequestsSent(@PathVariable String userName){
        return matchService.findRequestsSent(userName);
    }

    @Transactional
    @PostMapping(path = "/addCourse")
    @CrossOrigin(origins={"http://localhost:3000", "https://match-swensational.netlify.app"})
    public ResponseEntity addCourse(@RequestBody Course course){
        if(matchService.addCourse(course)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "/getAllCourses")
    @ResponseBody
    @CrossOrigin(origins={"http://localhost:3000", "https://match-swensational.netlify.app"})
    public List<Course> fetchAllCourses() {
        return matchService.getAllCourses();
    }

    @GetMapping(path = "/getCourseById/{id}")
    @ResponseBody
    @CrossOrigin(origins={"http://localhost:3000", "https://match-swensational.netlify.app"})
    public Course getCourse(@PathVariable String id) throws JsonProcessingException {
        return matchService.getCourseById(id);
    }

    @Transactional
    @DeleteMapping(path = "/deleteCourse/{id}")
    @CrossOrigin(origins={"http://localhost:3000", "https://match-swensational.netlify.app"})
    public  ResponseEntity deleteCourse(@PathVariable String id){
        if (matchService.deleteCourse(id)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping(path = "/getAllConfirmedMatches")
    @ResponseBody
    @CrossOrigin(origins={"http://localhost:3000", "https://match-swensational.netlify.app"})
    public List<Matches> getAllConfirmedMatches() {
        return matchService.getAllConfirmedMatches();
    }

    @Transactional
    @DeleteMapping(path = "/deleteMatch/{matchId}")
    @CrossOrigin(origins={"http://localhost:3000", "https://match-swensational.netlify.app"})
    public  ResponseEntity deleteMatch(@PathVariable String matchId){
        if (matchService.deleteMatch(matchId)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
