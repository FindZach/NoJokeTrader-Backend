package org.findzach.trader.controller;

import org.findzach.trader.model.Transaction;
import org.findzach.trader.service.disclosure.DisclosureService;
import org.findzach.trader.service.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author Zach Smith
 * @since 10/22/2025 4:08 AM
 */
@Controller
public class TransactionController {

    private final DisclosureService disclosureService;
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(DisclosureService disclosureService, TransactionService transactionService) {
        this.disclosureService = disclosureService;
        this.transactionService = transactionService;
    }

    @GetMapping("/transactions")
    public String showTransactions(Model model) {
        model.addAttribute("transactions", transactionService.findAll());
        return "transactions"; // maps to transactions.html
    }

    @GetMapping("/transactions/ticker/{ticker}")
    public String showTransactionsByTicker(Model model, @PathVariable String ticker) {

        List<Transaction> transactions = transactionService.findByTicker(ticker);

        model.addAttribute("transactions", transactions);
        model.addAttribute("ticker", ticker);

        return "transactions/transactions_by_ticker";
    }

}
