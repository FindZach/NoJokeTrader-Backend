package org.findzach.trader.repository;

import org.findzach.trader.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Zach Smith
 * @since 10/21/2025
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {


    List<Transaction> findByDisclosure_IdIn(List<Long> disclosureIds);
    List<Transaction> findByDisclosure_Id(Long disclosureId);
    List<Transaction> findByTicker(String ticker);
}
