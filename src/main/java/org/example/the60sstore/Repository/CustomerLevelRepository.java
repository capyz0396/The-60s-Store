package org.example.the60sstore.Repository;

import org.example.the60sstore.Entity.CustomerLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerLevelRepository extends JpaRepository<CustomerLevel, Integer> {}
