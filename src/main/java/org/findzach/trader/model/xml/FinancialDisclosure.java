package org.findzach.trader.model.xml;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

/**
 * @author Zach Smith
 * @since 10/21/2025
 */
@XmlRootElement(name = "FinancialDisclosure")
public class FinancialDisclosure {
    private List<DisclosureMember> members;

    @XmlElement(name = "Member")
    public List<DisclosureMember> getMembers() {
        return members;
    }

    public void setMembers(List<DisclosureMember> members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return "FinancialDisclosure{members=" + members + '}';
    }
}
