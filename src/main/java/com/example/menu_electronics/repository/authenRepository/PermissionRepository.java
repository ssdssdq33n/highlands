package com.example.menu_electronics.repository.authenRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.menu_electronics.entity.authen.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {}
