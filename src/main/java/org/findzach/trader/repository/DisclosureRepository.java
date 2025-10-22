package org.findzach.trader.repository;

import org.findzach.trader.model.Disclosure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Zach Smith
 * @since 10/21/2025
 */
public interface DisclosureRepository extends JpaRepository<Disclosure, Long> {
    /**
     * Queries for Disclosure based on DocID
     * @param docId
     * @return
     */
    Optional<Disclosure> findByDocId(String docId);
}
