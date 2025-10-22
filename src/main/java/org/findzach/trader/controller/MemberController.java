package org.findzach.trader.controller;

import org.findzach.trader.model.Disclosure;
import org.findzach.trader.model.Member;
import org.findzach.trader.model.Transaction;
import org.findzach.trader.service.disclosure.DisclosureService;
import org.findzach.trader.service.member.MemberService;
import org.findzach.trader.service.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Zach Smith
 * @since 10/22/2025 2:48 AM
 */
@Controller
public class MemberController {

    private final MemberService memberService;
    private final DisclosureService disclosureService;
    private final TransactionService transactionService;

    @Autowired
    public MemberController(MemberService memberService, DisclosureService disclosureService, TransactionService transactionService) {
        this.memberService = memberService;
        this.disclosureService = disclosureService;
        this.transactionService = transactionService;
    }

    // Endpoint to show all members
    @GetMapping("/members")
    public String listMembers(Model model) {

        List<Member> memberList = new ArrayList<>();
        memberService.findAll().forEach(memberList::add);

        model.addAttribute("members", memberList);
        return "members"; // maps to members.html
    }

    // Endpoint to load disclosure details for a specific member
    @GetMapping("/members/{id}")
    public String memberDetails(@PathVariable Long id, Model model) {
        Optional<Member> memberOptional = memberService.findById(id);
        if (memberOptional.isPresent()) {
            Member selectedMember = memberOptional.get();

            List<Disclosure> disclosures = disclosureService.findByMember_Id(selectedMember.getId());
            List<Long> disclosureIds = disclosures.stream()
                    .map(Disclosure::getId).toList();

            model.addAttribute("member", selectedMember);
            model.addAttribute("disclosures", disclosures);
            model.addAttribute("transactions", transactionService.findByDisclosure_IdIn(disclosureIds));
        } else {
            model.addAttribute("error", "No found member with ID " + id);
        }
        return "member_details"; // maps to member-details.html
    }

    /**
     * Fetch transactions for a specific disclosure and render the view.
     *
     * @param disclosureId The ID of the disclosure
     * @param model        The Spring Model to pass data to the view
     * @return Name of the view template
     */
    @GetMapping("/disclosures/{disclosureId}/transactions")
    public String getTransactionsByDisclosure(@PathVariable Long disclosureId, Model model) {
        // Fetch the disclosure
        Optional<Disclosure> disclosure = disclosureService.findById(disclosureId);

        // Fetch transactions associated with the disclosure
        List<Transaction> transactions = transactionService.findByDisclosure_Id(disclosureId);

        // Add data to the model
        model.addAttribute("disclosure", disclosure.get());
        model.addAttribute("transactions", transactions);

        // Render transactions.html
        return "transactions";
    }
}