package com.smart.demo.repository;

import com.smart.demo.entity.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<AdminEntity, Integer> {

}
