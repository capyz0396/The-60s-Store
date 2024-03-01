package org.example.testspring.Repository;

import org.example.testspring.Entity.AccessHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessHistoryRepository extends JpaRepository<AccessHistory, Integer> {}
