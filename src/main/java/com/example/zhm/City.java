package com.example.zhm;

import jakarta.persistence.*;

@Entity
@Table(name="city")
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String name;

    public City(String name) {
        this.name = name;
    }

    public City() {
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
        return "City{" +
                ", name='" + name + '\'' +
                '}';
    }

    public String getWordAsString() {
        return name;
    }
}


