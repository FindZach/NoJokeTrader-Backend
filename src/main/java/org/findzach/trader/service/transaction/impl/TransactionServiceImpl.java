package org.findzach.trader.service.transaction.impl;

import org.findzach.trader.model.Transaction;
import org.findzach.trader.repository.TransactionRepository;
import org.findzach.trader.service.transaction.TransactionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Zach Smith
 * @since 10/22/2025 3:55 AM
 */
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;

    public TransactionServiceImpl(TransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public <S extends Transaction> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Transaction> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Transaction> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<Transaction> findAll() {
        return repository.findAll();
    }

    @Override
    public Iterable<Transaction> findAllById(Iterable<Long> longs) {
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
    public void delete(Transaction entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Transaction> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Transaction> findByDisclosure_IdIn(List<Long> disclosureIds) {
        return repository.findByDisclosure_IdIn(disclosureIds);
    }

    @Override
    public List<Transaction> findByDisclosure_Id(Long disclosureId) {
        return repository.findByDisclosure_Id(disclosureId);
    }

    @Override
    public List<Transaction> findByTicker(String ticker) {
        return repository.findByTicker(ticker);
    }
}
