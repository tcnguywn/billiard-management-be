package com.backend.billiards_management.repositories;

import com.backend.billiards_management.entities.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    Optional<Employee> findByKeycloakId(String keycloakId);
}
