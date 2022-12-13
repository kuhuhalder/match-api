package com.match.matchapi;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * An instance of this class is a new Student
 * in the database.
 *
 * @author Prince Rawal
 * @author Farah Lubaba Rouf
 */


@Data
@Document
public class Student
{
    @Id
    private String id;
    @Indexed(unique = true)
    private String userName;
    private String password;

    private String firstName;
    private String lastName;
    private String pronouns;
    private String campus;
    private String year;
    private String major;
    private String genderPreference;
    private String bio;

    private String contactInfo;
    private List<String> course;

    private Integer isAdmin;

    public void print()
    {
        System.out.println("id : " + this.id);
        System.out.println("userName : " + this.userName);
        System.out.println("password : " + this.password);
        System.out.println("firstName : " + this.firstName);
        System.out.println("lastName : " + this.lastName);
        System.out.println("pronouns : " + this.pronouns);
        System.out.println("campus : " + this.campus);
        System.out.println("year : " + this.year);
        System.out.println("major : " + this.major);
        System.out.println("genderPreference : " + this.genderPreference);
        System.out.println("bio : " + this.bio);
        System.out.println("course : " + this.course);
        System.out.println("isAdmin : " + this.isAdmin);

    }
}
