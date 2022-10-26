package com.match.matchapi;

import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


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
    private Integer year;
    private String major;
    private String genderPreference;
    private String bio;

   // private ArrayList<String> courses;
    private String course;

    private Integer isAdmin;

}
