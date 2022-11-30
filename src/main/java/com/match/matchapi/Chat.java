package com.match.matchapi;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document
public class Chat {
    @Id
    private String id;
    private String userOneId;
    private String userTwoId;
    private String message;


    public void print() {
        System.out.println("id : " + this.id);
        System.out.println("user1 : " + this.userOneId);
        System.out.println("user2 : " + this.userTwoId);

    }
}

