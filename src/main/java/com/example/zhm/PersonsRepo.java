package com.example.zhm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PersonsRepo extends JpaRepository<Person, Integer> {
    List<Person> findByName(String name);

}