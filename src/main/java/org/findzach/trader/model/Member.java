package org.findzach.trader.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zach Smith
 * @since 10/21/2025
 */
@Entity
@Table(name = "members", uniqueConstraints = @UniqueConstraint(columnNames = {"first_name", "last_name", "state_district"}))
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "prefix")
    private String prefix;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "suffix")
    private String suffix;

    @Column(name = "state_district", nullable = false)
    private String stateDistrict;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Disclosure> disclosures = new ArrayList<>();

    // Constructors
    public Member() {}

    public Member(String prefix, String firstName, String lastName, String suffix, String stateDistrict) {
        this.prefix = prefix;
        this.firstName = firstName;
        this.lastName = lastName;
        this.suffix = suffix;
        this.stateDistrict = stateDistrict;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getStateDistrict() {
        return stateDistrict;
    }

    public void setStateDistrict(String stateDistrict) {
        this.stateDistrict = stateDistrict;
    }

    public List<Disclosure> getDisclosures() {
        return disclosures;
    }

    public void setDisclosures(List<Disclosure> disclosures) {
        this.disclosures = disclosures;
    }

    public void addDisclosure(Disclosure disclosure) {
        disclosures.add(disclosure);
        disclosure.setMember(this);
    }
}