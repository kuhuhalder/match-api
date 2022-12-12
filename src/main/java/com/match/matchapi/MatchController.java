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

/**
 * Controller for Match Service, contains all API's for our
 * web app
 *
 * @author Prince Rawal
 * @author Farah Lubaba Rouf
 */

@AllArgsConstructor
@Controller
@RequestMapping("/api/students")
public class MatchController {

    @Autowired
    private MatchService matchService;

    /**
     * Gets a list of all students
     */

    @GetMapping(path = "/getAll")
    @ResponseBody
    @CrossOrigin(origins={"http://localhost:3000", "https://match-swensational.netlify.app"})
    public List<Student> fetchAllStudents() {
        return matchService.getAllStudents();
    }

    /**
     * Adds a new student to the database
     */

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

    /**
     * Adds a match to the database
     */

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

    /**
     * Checks that the student exists in the database
     */

    @GetMapping(path = "/validate/{userName}/{password}")
    @CrossOrigin(origins={"http://localhost:3000", "https://match-swensational.netlify.app"})
    public ResponseEntity validation(@PathVariable String userName, @PathVariable String password){
        if (matchService.validation(userName, password)){
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
//        return ResponseEntity.ok((matchService.validation(userName, password)));
    }

    /**
     * Gets an existinf student object from the database
     */

    @GetMapping(path = "/getStudent/{userName}")
    @ResponseBody
    @CrossOrigin(origins={"http://localhost:3000", "https://match-swensational.netlify.app"})
    public Student getStudent(@PathVariable String userName) throws JsonProcessingException {
        return matchService.getStudent(userName);
    }

    /**
     * Deletes a student from the database
     */

    @Transactional
    @DeleteMapping(path = "/delete/{userName}")
    @CrossOrigin(origins={"http://localhost:3000", "https://match-swensational.netlify.app"})
    public  ResponseEntity deleteStudent(@PathVariable String userName){
        if (matchService.deleteStudent(userName)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Displays the potential matches for a student in descending order of similarity index
     */

    @GetMapping(path = "/matches/{userName}")
    @ResponseBody
    @CrossOrigin(origins={"http://localhost:3000", "https://match-swensational.netlify.app"})
    public List<Student> findBuddies(@PathVariable String userName) throws JsonProcessingException {

        //Student studentObject= getStudent(userName);
        List<Student> students = matchService.findBuddies(userName);

        return students;

    }

    /**
     * Updates a student's profile
     */

    @PostMapping(path = "/update")
    @ResponseBody
    @CrossOrigin(origins={"http://localhost:3000", "https://match-swensational.netlify.app"})
    public  ResponseEntity updateStudent(@RequestBody Student student){
        if (matchService.updateStudent(student)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Checks that 2 students are matched
     */

    @GetMapping(path = "areMatched/{userName1}/{userName2}")
    @CrossOrigin(origins={"http://localhost:3000", "https://match-swensational.netlify.app"})
    @ResponseBody
    public int matchExists(@PathVariable String userName1, @PathVariable String userName2){
        return matchService.matchExists(userName1, userName2);
    }

    /**
     * Finds the requests for a student
     */

    @GetMapping(path = "findRequests/{userName}")
    @CrossOrigin(origins={"http://localhost:3000", "https://match-swensational.netlify.app"})
    @ResponseBody
    public List<Student> findRequests(@PathVariable String userName){
        return matchService.findRequests(userName);
    }

    /**
     * Finds the confirmed matches for a student
     */

    @GetMapping(path = "findConfirmedMatches/{userName}")
    @CrossOrigin(origins={"http://localhost:3000", "https://match-swensational.netlify.app"})
    @ResponseBody
    public List<Student> findConfirmedMatches(@PathVariable String userName){
        return matchService.findConfirmedMatches(userName);
    }

    /**
     * Finds a student's sent requests
     */

    @GetMapping(path = "findRequestsSent/{userName}")
    @CrossOrigin(origins={"http://localhost:3000", "https://match-swensational.netlify.app"})
    @ResponseBody
    public List<Student> findRequestsSent(@PathVariable String userName){
        return matchService.findRequestsSent(userName);
    }

    /**
     * Adds a course to the database
     */

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

    /**
     * Gets a list of all courses in the database
     */

    @GetMapping(path = "/getAllCourses")
    @ResponseBody
    @CrossOrigin(origins={"http://localhost:3000", "https://match-swensational.netlify.app"})
    public List<Course> fetchAllCourses() {
        return matchService.getAllCourses();
    }

    /**
     * Gets a course by id
     */

    @GetMapping(path = "/getCourseById/{id}")
    @ResponseBody
    @CrossOrigin(origins={"http://localhost:3000", "https://match-swensational.netlify.app"})
    public Course getCourse(@PathVariable String id) throws JsonProcessingException {
        return matchService.getCourseById(id);
    }

    /**
     * Deletes a course from the database
     */

    @Transactional
    @DeleteMapping(path = "/deleteCourse/{id}")
    @CrossOrigin(origins={"http://localhost:3000", "https://match-swensational.netlify.app"})
    public  ResponseEntity deleteCourse(@PathVariable String id){
        if (matchService.deleteCourse(id)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Displays a list of all matches for the admin
     */

    @GetMapping(path = "/getAllConfirmedMatches")
    @ResponseBody
    @CrossOrigin(origins={"http://localhost:3000", "https://match-swensational.netlify.app"})
    public List<Matches> getAllConfirmedMatches() {
        return matchService.getAllConfirmedMatches();
    }

    /**
     * Deletes a match given the id, only for admin
     */

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
