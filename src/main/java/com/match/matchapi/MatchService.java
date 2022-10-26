package com.match.matchapi;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MatchService {

    @Autowired
    private final MatchRepository matchRepository;

    @Autowired
    private final MongoTemplate mongoTemplate;

    public List<Student> getAllStudents() {
        return matchRepository.findAll();
    }

    public boolean validation(String username, String password) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userName").is(username).andOperator(
                Criteria.where("password").is(password)
        ));

        List<Student> students = mongoTemplate.find(query, Student.class);

        if (students.isEmpty()){
            return false;
        }

        return students.size() == 1;
    }

    public boolean addStudent(Student student) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userName").is(student.getUserName()));
        List<Student> students = mongoTemplate.find(query, Student.class);

        if (students.size() > 0){
            return false;
        }
        matchRepository.insert(student);
        return true;
    }

    public boolean deleteStudent(String userName, String password) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userName").is(userName));
        List<Student> students = mongoTemplate.find(query, Student.class);

        if(validation(userName, password)){
            matchRepository.deleteById(students.get(0).getId());
            return true;
        }
        return false;
    }

    public Student getStudent(String userName) {
        return mongoTemplate.findById(userName, Student.class);
    }

//    public Student updateStudent(Student student) {
//        Query query = new Query();
//        query.addCriteria(Criteria.where("userName").is(student.getUserName()));
//        List<Student> students = mongoTemplate.find(query, Student.class);
//    }
    public List<Student> matchStudents (Student student) {
        Query query = new Query();
        query.addCriteria(Criteria.where("course").is(student.getCourse()));
        List<Student> students = mongoTemplate.find(query, Student.class);
        for(int i=0;i<students.size();i++) {
            if(students.get(i).getUserName().equals(student.getUserName())) {
                students.remove(i);
            }
        }
        return students;
    }
}
