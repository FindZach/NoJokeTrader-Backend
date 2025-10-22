package org.findzach.trader.service;

import org.findzach.trader.model.Member;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * @author Zach Smith
 * @since 10/21/2025
 */
public interface MemberService extends CrudRepository<Member, Long> {

    boolean doesMemberExist(String firstName, String lastName, String stateDistrict);

    Optional<Member> getMemberByNameAndDistrict(String firstName, String lastName, String stateDistrict);
}
