package org.findzach.trader.model.xml;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * @author Zach Smith
 * @since 10/21/2025
 */
@XmlRootElement(name = "Member")
public class DisclosureMember {

    private String prefix;
    private String last;
    private String first;
    private String suffix;
    private String filingType;
    private String stateDst;
    private int year;
    private String filingDate;
    private String docID;

    @XmlElement(name = "Prefix")
    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @XmlElement(name = "Last")
    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    @XmlElement(name = "First")
    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    @XmlElement(name = "Suffix")
    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    @XmlElement(name = "FilingType")
    public String getFilingType() {
        return filingType;
    }

    public void setFilingType(String filingType) {
        this.filingType = filingType;
    }

    @XmlElement(name = "StateDst")
    public String getStateDst() {
        return stateDst;
    }

    public void setStateDst(String stateDst) {
        this.stateDst = stateDst;
    }

    @XmlElement(name = "Year")
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @XmlElement(name = "FilingDate")
    public String getFilingDate() {
        return filingDate;
    }

    public void setFilingDate(String filingDate) {
        this.filingDate = filingDate;
    }

    @XmlElement(name = "DocID")
    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    @Override
    public String toString() {
        return "Member{" +
                "prefix='" + prefix + '\'' +
                ", last='" + last + '\'' +
                ", first='" + first + '\'' +
                ", suffix='" + suffix + '\'' +
                ", filingType='" + filingType + '\'' +
                ", stateDst='" + stateDst + '\'' +
                ", year=" + year +
                ", filingDate='" + filingDate + '\'' +
                ", docID='" + docID + '\'' +
                '}';
    }
}
