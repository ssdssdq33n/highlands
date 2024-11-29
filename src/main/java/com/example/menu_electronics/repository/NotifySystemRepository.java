package com.example.menu_electronics.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.menu_electronics.entity.NotifySystem;

@Repository
public interface NotifySystemRepository extends JpaRepository<NotifySystem, Long> {
    List<NotifySystem> findAllByReadNotifyEquals(boolean read);
}
