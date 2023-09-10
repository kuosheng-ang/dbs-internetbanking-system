package dbs.bankingsystem.backend.service;

import dbs.bankingsystem.backend.config.JwtTokenUtil;
import dbs.bankingsystem.backend.dto.TransferMoneyDto;
import dbs.bankingsystem.backend.entity.Account;
import dbs.bankingsystem.backend.entity.Transaction;
import dbs.bankingsystem.backend.entity.User;
import dbs.bankingsystem.backend.exception.BadRequestException;
import dbs.bankingsystem.backend.repositories.TransactionRepository;
import dbs.bankingsystem.backend.repositories.UserRepository;
import dbs.bankingsystem.backend.repositories.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: ANG KUO SHENG CLEMENT
 * @date: 9-Sep-2023
 */

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    private final Logger logger = LogManager.getLogger(TransactionService.class);
    private final UserRepository userRepository;

    public TransactionService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public Transaction getTransactionByAccount(String accountNumber) {
        return transactionRepository.findByAccountNumber(accountNumber);
    }

    //public String transferMoney(TransferMoneyDto transferMoneyDto, String token) {
    public String transferMoney(TransferMoneyDto transferMoneyDto, String email) {
        //String transferFrom = jwtTokenUtil.getUsernameFromToken(token);
        String transferFrom = userService.getUsernameByEmail(email);
        Account accountTo = accountService.getAccountByAccountNo(transferMoneyDto.getTransferTo());
        Account accountFrom = accountService.getAccountByAccountNo(transferFrom);
        if (accountTo == null) {
            this.logger.info("Account not found with account no : {}", transferMoneyDto.getTransferTo());
            throw new BadRequestException("Account not found");
        } else if (accountTo.getAccountNo().equals(accountFrom.getAccountNo())) {
            throw new BadRequestException("Can not transfer yourself");
        } else if (accountFrom.getAccountBalance() < transferMoneyDto.getAmount()) {
            throw new BadRequestException("Not enough balance");
        } else {
            return this.makeTransaction(accountFrom, accountTo, transferMoneyDto.getAmount());
        }
    }

    private String makeTransaction(Account accountFrom, Account accountTo, Double amount) {
        Transaction transaction = new Transaction();
        transaction.setTxAmount(amount);
        transaction.setTransferFrom(accountFrom.getAccountNo());
        transaction.setTransferTo(accountTo.getAccountNo());
        transaction.setTxDateTime(new Date());
        transactionRepository.save(transaction);
        accountFrom.setAccountBalance(accountFrom.getAccountBalance() - amount);
        accountService.updateAccount(accountFrom);
        accountTo.setAccountBalance(accountTo.getAccountBalance() + amount);
        accountService.updateAccount(accountTo);
        return "Transaction Done";
    }

    //public List<Transaction> getTransactionHistory(String token) {
    //String accountNo = jwtTokenUtil.getUsernameFromToken(token);
    public List<Transaction> getTransactionHistory(String email) {
        User user = userService.getUserFromEmail(email);
        List<Transaction> transactions = new ArrayList();
        transactions.addAll(transactionRepository.findByTransferFrom(user.getAccountDetails().getAccountNo()));
        transactions.addAll(transactionRepository.findByTransferTo(user.getAccountDetails().getAccountNo()));
        transactions.sort(Collections.reverseOrder());
        return transactions;
    }


}
