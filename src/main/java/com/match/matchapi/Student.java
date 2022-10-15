package com.match.matchapi;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Data
@Document
public class Student {
    @Id
    private String id;
    @Indexed(unique = true)
    private String userName;
    private String password;

    private String firstName;
    private String lastName;
    private String pronouns;
    private String campus;
    private int year;
    private String major;
    private String genderPreference;
    private String bio;

    private ArrayList<String> courses;

    private int isAdmin;

}
