package org.example.testspring.Repository;

import org.example.testspring.Entity.CustomerLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerLevelRepository extends JpaRepository<CustomerLevel, Integer> {}
