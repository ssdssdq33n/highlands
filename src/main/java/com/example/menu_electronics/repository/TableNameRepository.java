package com.example.menu_electronics.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.menu_electronics.entity.TableName;

@Repository
public interface TableNameRepository extends JpaRepository<TableName, Long> {
    Optional<TableName> findByName(String name);
}
