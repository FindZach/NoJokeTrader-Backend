package org.findzach.trader.service.disclosure.impl;

import org.findzach.trader.model.Disclosure;
import org.findzach.trader.repository.DisclosureRepository;
import org.findzach.trader.service.disclosure.DisclosureService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Zach Smith
 * @since 10/22/2025 3:55 AM
 */
@Service
public class DisclosureServiceImpl implements DisclosureService {

    private final DisclosureRepository repository;

    public DisclosureServiceImpl(DisclosureRepository repository) {
        this.repository = repository;
    }

    @Override
    public <S extends Disclosure> S save(S entity) {
        return repository.save(entity);
    }

    @Override
    public <S extends Disclosure> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Disclosure> findById(Long aLong) {
        return repository.findById(aLong);
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<Disclosure> findAll() {
        return repository.findAll();
    }

    @Override
    public Iterable<Disclosure> findAllById(Iterable<Long> longs) {
        return repository.findAllById(longs);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Disclosure entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Disclosure> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public Optional<Disclosure> findByDocId(String docId) {
        return repository.findByDocId(docId);
    }

    @Override
    public List<Disclosure> findByMember_Id(Long memberId) {
        return repository.findByMember_Id(memberId);
    }
}
