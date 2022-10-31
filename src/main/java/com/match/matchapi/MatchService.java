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

    public boolean deleteStudent(String userName) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userName").is(userName));
        List<Student> students = mongoTemplate.find(query, Student.class);
        if(students == null) {
            return false;
        }

            matchRepository.deleteById(students.get(0).getId());
            return true;

    }

    public Student getStudent(String userName) {
        return mongoTemplate.findById(userName, Student.class);
    }

    public boolean updateStudent(Student student) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(student.getId()));
        List<Student> students = mongoTemplate.find(query, Student.class);
        if(students == null || students.size()<=0){
            return false;
        }

        student.setUserName(students.get(0).getUserName());

        if(student.getCourse() == null){
            student.setCourse(students.get(0).getCourse());
        }
        if(student.getCampus() == null){
            student.setCampus(students.get(0).getCampus());
        }
        if(student.getFirstName() == null){
            student.setFirstName(students.get(0).getFirstName());
        }
        if(student.getLastName() == null){
            student.setLastName(students.get(0).getLastName());
        }
        if(student.getBio() == null){
            student.setBio(students.get(0).getBio());
        }
        if(student.getMajor() == null){
            student.setMajor(students.get(0).getMajor());
        }
        if(student.getPronouns() == null){
            student.setPronouns(students.get(0).getPronouns());
        }
        if(student.getPassword() == null){
            student.setPassword(students.get(0).getPassword());
        }
        if(student.getYear() == null){
            student.setYear(students.get(0).getYear());
        }
        if(student.getGenderPreference() == null){
            student.setGenderPreference(students.get(0).getGenderPreference());
        }

        matchRepository.deleteById(student.getId());
        matchRepository.insert(student);
        return true;

    }
    public List<Student> matchStudents (Student student) {
        //list of all students
        List<Student> allStudents = new ArrayList<>();
        allStudents = getAllStudents();

        //points array
        List<Integer> points = new ArrayList<>();

        //getting students with same course
        Query query = new Query();
        query.addCriteria(Criteria.where("course").is(student.getCourse()));
        List<Student> studentsWithSameCourse = mongoTemplate.find(query, Student.class);

        //adding points to all students array
        // 3 points for campus
        int indexOfPointsArray;
        for(int i=0;i<studentsWithSameCourse.size();i++) {
            if(allStudents.contains(studentsWithSameCourse.get(i))) {
                indexOfPointsArray = allStudents.indexOf(studentsWithSameCourse.get(i));
                points.add(indexOfPointsArray,3);
            }
        }

        //campus
        Query campusQuery = new Query();
        campusQuery.addCriteria(Criteria.where("campus").is(student.getCampus()));
        List<Student> studentsWithSameCampus = mongoTemplate.find(campusQuery, Student.class);
        //2 points for campus
        for(int i=0;i<studentsWithSameCampus.size();i++) {
            if(allStudents.contains(studentsWithSameCampus.get(i))) {
                indexOfPointsArray = allStudents.indexOf(studentsWithSameCampus.get(i));
                points.add(indexOfPointsArray,points.get(i).intValue()+2);
            }
        }

        Query majorQuery = new Query();
        majorQuery.addCriteria(Criteria.where("major").is(student.getMajor()));
        List<Student> studentsWithSameMajor = mongoTemplate.find(majorQuery, Student.class);
        //2 points for major
        for(int i=0;i<studentsWithSameCampus.size();i++) {
            if(allStudents.contains(studentsWithSameMajor.get(i))) {
                indexOfPointsArray = allStudents.indexOf(studentsWithSameMajor.get(i));
                points.add(indexOfPointsArray,points.get(i).intValue()+2);
            }
        }

        Query genderQuery = new Query();
        genderQuery.addCriteria(Criteria.where("gender").is(student.getGenderPreference()));
        List<Student> studentsWithSameGender = mongoTemplate.find(genderQuery, Student.class);
        //2 points for gender
        for(int i=0;i<studentsWithSameGender.size();i++) {
            if(allStudents.contains(studentsWithSameGender.get(i))) {
                indexOfPointsArray = allStudents.indexOf(studentsWithSameGender.get(i));
                points.add(indexOfPointsArray,points.get(i).intValue()+2);
            }
        }

        //NOW MERGESORT

//        for(int i=0;i<students.size();i++) {
//            if(students.get(i).getUserName().equals(student.getUserName())) {
//                students.remove(i);
//            }
//        }
//       List<String> studentIds = new ArrayList<>();
//        for(int i=0;i<students.size();i++) {
//            studentIds.add(students.get(i).getId());
//        }
//        return studentIds;
        //return students;
    }

    private void merge(List<Integer> points,List<Student> students,int l,int m, int r) {
        // Find sizes of two subarrays to be merged
        int n1 = m - l + 1;
        int n2 = r - m;

        /* Create temp arrays */
        int Lpoints[] = new int[n1];
        int Rpoints[] = new int[n2];

        Student Lstudents[] = new Student[n1];
        Student Rstudents[] = new Student[n2];

        /*Copy data to temp arrays*/
        for (int i = 0; i < n1; ++i)
            Lpoints[i] = points.get(l + i);
        for (int j = 0; j < n2; ++j)
            Rpoints[j] = points.get(m + 1 + j);

        for (int i = 0; i < n1; ++i)
            Lstudents[i] = students.get(l + i);
        for (int j = 0; j < n2; ++j)
            Rstudents[j] = students.get(m + 1 + j);

        // Initial indexes of first and second subarrays
        int i = 0, j = 0;

        // Initial index of merged subarray array
        int k = l;
        while (i < n1 && j < n2) {
            if (Lpoints[i] <= Rpoints[j]) {
                points.add(k, Lpoints[i]);
                students.add(k,Lstudents[i]);
                i++;
            }
            else {
                points.add(k, Rpoints[j]);
                students.add(k,Rstudents[j]);
                j++;
            }
            k++;
        }

        /* Copy remaining elements of L[] if any */
        while (i < n1) {
            points.add(k, Lpoints[i]);
            students.add(k,Lstudents[i]);
            i++;
            k++;
        }

        /* Copy remaining elements of R[] if any */
        while (j < n2) {
            points.add(k,Rpoints[j]);
            students.add(k,Rstudents[j]);
            j++;
            k++;
        }
    }
    void sort(List<Integer> points,List<Student> students, int l, int r)
    {
        if (l < r) {
            // Find the middle point
            int m = l + (r - l) / 2;

            // Sort first and second halves
            sort(points,students, l, m);
            sort(points,students, m + 1, r);

            // Merge the sorted halves
            merge(points, students, l, m, r);
        }
    }

}
