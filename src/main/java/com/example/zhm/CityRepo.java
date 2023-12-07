package com.example.zhm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CityRepo extends JpaRepository<City, Integer> {
    List<City> findByName(String name);

}