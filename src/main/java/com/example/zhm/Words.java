package com.example.zhm;

import jakarta.persistence.*;

@Entity
@Table(name ="words")
public class Words {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String name;

    public Words(String name) {
        this.name = name;
    }

    public Words() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getWordAsString() {
        return this.name;
    }
}
