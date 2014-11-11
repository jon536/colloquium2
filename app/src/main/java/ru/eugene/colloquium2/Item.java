package ru.eugene.colloquium2;

/**
 * Created by eugene on 11/4/14.
 */
public class Item {
    private String name = "";
    private int vote = 0;
    private int id = 0;

    Item() {}

    Item(int id) {
        this.id = id;
    }

    Item(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }

    int getVote() {
        return vote;
    }

    int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    void setName(String name) {
        this.name = name;
    }

    void setVote(int vote) {
        this.vote = vote;
    }
}
