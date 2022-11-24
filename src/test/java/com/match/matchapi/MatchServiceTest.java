package com.match.matchapi;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MatchServiceTest {


    @Mock private StudentRepository studentRepository;
    @Mock private MatchRepository matchRepository;
    @Mock private CourseRepository courseRepository;
    @Mock private MongoTemplate mongoTemplate;
    @Mock
    Student student;
    private MatchService underTest;

    @BeforeEach
    void setUp() {

        underTest = new MatchService(studentRepository,courseRepository,matchRepository,mongoTemplate);
    }

    @Test
    void canGetAllStudents() {
        underTest.getAllStudents();
        verify(studentRepository).findAll();
    }

    @Test
    void validation() {
    }

    @Test
    void canAddStudent() {

//        underTest.addStudent(student);
//        ArgumentCaptor<Student> studentArgumentCaptor =
//                ArgumentCaptor.forClass(Student.class);
//        verify(studentRepository).save(studentArgumentCaptor.capture());
//        Student capturedStudent = studentArgumentCaptor.getValue();
//        assertThat(capturedStudent).isEqualTo(student);
        Boolean bool1 = underTest.addStudent(student);
        assertTrue(bool1);


    }

    @Test
    void deleteStudent() {

    }

    @Test
    void getStudent() {
    }

    @Test
    void updateStudent() {
    }

    @Test
    void matchStudent() {
    }

    @Test
    void matchExists() {
    }

    @Test
    void addMatches() {
    }
}