package com.backend.billiards_management.repositories;

import com.backend.billiards_management.entities.billiard_table.BilliardTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableRepository extends JpaRepository<BilliardTable, Integer> {

}
