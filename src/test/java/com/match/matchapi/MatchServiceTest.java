package com.match.matchapi;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MatchServiceTest {


    @Mock private StudentRepository studentRepository;
    @Mock private MatchRepository matchRepository;
    @Mock private CourseRepository courseRepository;
    @Mock private MongoTemplate mongoTemplate;
    private MatchService underTest;

    @BeforeEach
    void setUp() {
        underTest = new MatchService(studentRepository,courseRepository,matchRepository,mongoTemplate);
    }

    @Test
    void getAllStudents() {
        underTest.getAllStudents();
        verify(studentRepository).findAll();
    }

    @Test
    void validation() {
    }

    @Test
    void addStudent() {
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