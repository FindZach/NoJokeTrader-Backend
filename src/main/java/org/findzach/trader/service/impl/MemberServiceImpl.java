package org.findzach.trader.service.impl;

import org.findzach.trader.model.Member;
import org.findzach.trader.repository.MemberRepository;
import org.findzach.trader.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author Zach Smith
 * @since 10/21/2025
 */
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository repository;

    public MemberServiceImpl(MemberRepository repository) {
        this.repository = repository;
    }

    public boolean doesMemberExist(String firstName, String lastName, String stateDistrict) {
        return repository.findByFirstNameAndLastNameAndStateDistrict(firstName, lastName, stateDistrict).isPresent();
    }

    @Transactional(readOnly = true)  // Ensures managed entity, joins caller's tx
    @Override
    public Optional<Member> getMemberByNameAndDistrict(String firstName, String lastName, String stateDistrict) {
        return repository.findByFirstNameAndLastNameAndStateDistrict(firstName, lastName, stateDistrict);
    }

    @Override
    public <S extends Member> S save(S entity) {
        return repository.save(entity);
    }

    @Override
    public <S extends Member> Iterable<S> saveAll(Iterable<S> entities) {
        return repository.saveAll(entities);
    }

    @Override
    public Optional<Member> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return repository.existsById(aLong);
    }

    @Override
    public Iterable<Member> findAll() {
        return repository.findAll();
    }

    @Override
    public Iterable<Member> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Member entity) {
        repository.delete(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {
        repository.deleteAllById(longs);
    }

    @Override
    public void deleteAll(Iterable<? extends Member> entities) {
        repository.deleteAll(entities);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
