package org.findzach.trader.repository;

import org.findzach.trader.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Zach Smith
 * @since 10/21/2025
 */
public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * Attempts to find Congress Member
     * @param firstName
     * @param lastName
     * @param stateDistrict
     * @return
     */
    Optional<Member> findByFirstNameAndLastNameAndStateDistrict(String firstName, String lastName, String stateDistrict);

}
