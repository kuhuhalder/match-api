package com.match.matchapi;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * An instance of this class is a new Match
 * between two students, stored in the
 * match database
 *
 * @author Prince Rawal
 * @author Farah Lubaba Rouf
 */


@Data
@Document
public class Matches
{
    @Id
    private String id;
    private String userOneId;
    private String userTwoId;


    public void print()
    {
        System.out.println("id : " + this.id);
        System.out.println("user1 : " + this.userOneId);
        System.out.println("user2 : " + this.userTwoId);

    }
}

