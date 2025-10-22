package org.findzach.trader.repository;

import org.findzach.trader.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Zach Smith
 * @since 10/21/2025
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
