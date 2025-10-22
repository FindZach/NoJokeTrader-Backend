package org.findzach.trader.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Date;

/**
 * @author Zach Smith
 * @since 10/21/2025
 */
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disclosure_id", nullable = false)
    @JsonIgnore  // Prevent serialization loops if using JSON
    private Disclosure disclosure;

    @Column(name = "amount_range")
    private String amountRange;

    @Column(name = "asset")
    private String asset;

    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "filing_id")
    private String filingId;

    @Column(name = "filing_status")
    private String filingStatus;

    @Column(name = "location")
    private String location;

    @Temporal(TemporalType.DATE)
    @Column(name = "notification_date")
    private Date notificationDate;

    @Column(name = "owner")
    private String owner;

    @Column(name = "ticker")
    private String ticker;

    @Temporal(TemporalType.DATE)
    @Column(name = "transaction_date")
    private Date transactionDate;

    @Column(name = "transaction_type")
    private String transactionType;

    // Constructors
    public Transaction() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Disclosure getDisclosure() {
        return disclosure;
    }

    public void setDisclosure(Disclosure disclosure) {
        this.disclosure = disclosure;
    }

    public String getAmountRange() {
        return amountRange;
    }

    public void setAmountRange(String amountRange) {
        this.amountRange = amountRange;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilingId() {
        return filingId;
    }

    public void setFilingId(String filingId) {
        this.filingId = filingId;
    }

    public String getFilingStatus() {
        return filingStatus;
    }

    public void setFilingStatus(String filingStatus) {
        this.filingStatus = filingStatus;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(Date notificationDate) {
        this.notificationDate = notificationDate;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
}