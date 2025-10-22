package org.findzach.trader.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Zach Smith
 * @since 10/21/2025
 */
@Entity
@Table(name = "disclosures", uniqueConstraints = @UniqueConstraint(columnNames = {"doc_id"}))
public class Disclosure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @JsonIgnore  // Prevent serialization loops if using JSON
    private Member member;

    @Column(name = "filing_type", nullable = false)
    private String filingType;

    @Column(name = "filing_year", nullable = false)
    private int filingYear;

    @Temporal(TemporalType.DATE)
    @Column(name = "filing_date")
    private Date filingDate;

    @Column(name = "doc_id", nullable = false)
    private String docId;

    @Column(name = "filer_status")
    private String filerStatus;

    @Column(name = "transaction_count")
    private int transactionCount;

    @Column(name = "url")
    private String url;

    @OneToMany(mappedBy = "disclosure", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

    // Constructors
    public Disclosure() {}

    public Disclosure(String filingType, int filingYear, Date filingDate, String docId) {
        this.filingType = filingType;
        this.filingYear = filingYear;
        this.filingDate = filingDate;
        this.docId = docId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getFilingType() {
        return filingType;
    }

    public void setFilingType(String filingType) {
        this.filingType = filingType;
    }


    public Date getFilingDate() {
        return filingDate;
    }

    public void setFilingDate(Date filingDate) {
        this.filingDate = filingDate;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getFilerStatus() {
        return filerStatus;
    }

    public void setFilerStatus(String filerStatus) {
        this.filerStatus = filerStatus;
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(int transactionCount) {
        this.transactionCount = transactionCount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getFilingYear() {
        return filingYear;
    }

    public void setFilingYear(int filingYear) {
        this.filingYear = filingYear;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        transaction.setDisclosure(this);
    }
}