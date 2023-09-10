package dbs.bankingsystem.backend.service;

import dbs.bankingsystem.backend.config.JwtTokenUtil;
import dbs.bankingsystem.backend.repositories.TransactionRepository;
import dbs.bankingsystem.backend.dto.Constants;
import dbs.bankingsystem.backend.dto.TransferMoneyDto;
import dbs.bankingsystem.backend.entity.*;
import dbs.bankingsystem.backend.exception.BadRequestException;
import dbs.bankingsystem.backend.repositories.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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

    /*@Autowired
    private JwtTokenUtil jwtTokenUtil;*/
    @Autowired
    private UserService userService;

    private final Logger logger = LogManager.getLogger(getClass());

    //public String transferMoney(TransferMoneyDto transferMoneyDto, String token) {
    public String transferMoney(TransferMoneyDto transferMoneyDto, String email) {
        //String transferFrom = jwtTokenUtil.getUsernameFromToken(token);
        String transferFrom = userService.getUsernameByEmail(email);
        Account accountTo = accountService.getAccountByAccountNo(transferMoneyDto.getTransferTo());
        Account accountFrom = accountService.getAccountByAccountNo(transferFrom);

        if (accountTo == null) {
            logger.info("Account not found with account no : {}", transferMoneyDto.getTransferTo());
            throw new BadRequestException("Account not found");
        }
        if (accountTo.getAccountNo().equals(accountFrom.getAccountNo())) {
            throw new BadRequestException("Can not transfer yourself");
        }
        if (accountFrom.getAccountBalance() < transferMoneyDto.getAmount()) {
            logger.error("Not enough balance in account : {}", transferFrom);
            throw new BadRequestException("Not enough balance");
        }

        return makeTransaction(accountFrom, accountTo, transferMoneyDto.getAmount());


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
        return Constants.TRANSACTION_DONE;

    }

    //public List<Transaction> getTransactionHistory(String token) {
    //String accountNo = jwtTokenUtil.getUsernameFromToken(token);
    public List<Transaction> getTransactionHistory(String email) {

        String accountNo = userService.getUsernameByEmail(email);
        List<Transaction> transactions = new ArrayList<>();
        transactions.addAll(transactionRepository.findByTransferFrom(accountNo));
        transactions.addAll(transactionRepository.findByTransferTo(accountNo));
        transactions.sort(Collections.reverseOrder());
        return transactions;
    }
}
