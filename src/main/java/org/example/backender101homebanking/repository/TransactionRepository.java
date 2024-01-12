package org.example.backender101homebanking.repository;

import org.example.backender101homebanking.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("mysql-transaction")
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}
