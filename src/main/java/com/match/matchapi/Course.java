package com.match.matchapi;

import org.springframework.data.annotation.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;



@Data
@Document

/**
 * An instance of this class is a new Course
 * in the database.
 *
 * @author Prince Rawal
 * @author Farah Lubaba Rouf
 */

public class Course {
    @Id
    private String id;
    private String courseName;
}
