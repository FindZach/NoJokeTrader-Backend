package org.findzach.trader.service.disclosure;

import org.findzach.trader.model.Disclosure;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author Zach Smith
 * @since 10/22/2025 3:53 AM
 */
public interface DisclosureService extends CrudRepository<Disclosure, Long> {
    /**
     * Queries for Disclosure based on DocID
     * @param docId
     * @return
     */
    Optional<Disclosure> findByDocId(String docId);

    /**
     * Finds all disclosures related to a specific member
     *
     * @param memberId
     * @return List of disclosures
     */
    List<Disclosure> findByMember_Id(Long memberId);
}
