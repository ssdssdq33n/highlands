package com.example.menu_electronics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.menu_electronics.entity.FeedBack;

@Repository
public interface FeedBackRepository extends JpaRepository<FeedBack, Long> {}
