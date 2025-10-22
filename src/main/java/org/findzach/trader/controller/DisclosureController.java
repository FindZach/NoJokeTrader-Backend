package org.findzach.trader.controller;

import org.findzach.trader.service.disclosure.DisclosureService;
import org.findzach.trader.service.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Zach Smith
 * @since 10/22/2025 5:06 AM
 */
@Controller
public class DisclosureController {


    private final DisclosureService disclosureService;
    private final TransactionService transactionService;

    @Autowired
    public DisclosureController(DisclosureService disclosureService, TransactionService transactionService) {
        this.disclosureService = disclosureService;
        this.transactionService = transactionService;
    }

    @GetMapping("/disclosures")
    public String showDisclosures(Model model) {
        model.addAttribute("disclosures", disclosureService.findAll());
        return "disclosures"; // maps to disclosures.html
    }

}
