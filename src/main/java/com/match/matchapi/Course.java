package com.match.matchapi;

import org.springframework.data.annotation.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;



@Data
@Document

public class Course {
    @Id
    private String id;
    private String courseName;
}
