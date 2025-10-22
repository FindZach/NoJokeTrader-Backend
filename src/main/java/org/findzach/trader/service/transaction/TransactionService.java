package org.findzach.trader.service.transaction;

import org.findzach.trader.model.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author Zach Smith
 * @since 10/22/2025 3:54 AM
 */
public interface TransactionService extends CrudRepository<Transaction, Long> {

    List<Transaction> findByDisclosure_IdIn(List<Long> disclosureIds);
    List<Transaction> findByDisclosure_Id(Long disclosureId);
}
