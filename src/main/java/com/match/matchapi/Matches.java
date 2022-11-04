package com.match.matchapi;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Data
@Document
public class Matches {
    @Id
    private String id;
    private String userOneId;
    private String userTwoId;


    public void print() {
        System.out.println("id : " + this.id);
        System.out.println("user1 : " + this.userOneId);
        System.out.println("user2 : " + this.userTwoId);

    }
}

