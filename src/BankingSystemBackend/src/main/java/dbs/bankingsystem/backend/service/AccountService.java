package dbs.bankingsystem.backend.service;

import dbs.bankingsystem.backend.config.JwtTokenUtil;
import dbs.bankingsystem.backend.repositories.*;
import dbs.bankingsystem.backend.dto.Constants;
import dbs.bankingsystem.backend.entity.Account;
import dbs.bankingsystem.backend.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author: ANG KUO SHENG CLEMENT
 * @date: 9-Sep-2023
 */

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    TransactionService transactionService;

    private final Logger logger = LogManager.getLogger(getClass());


    /*public Account getAccountFromToken(String token) {
        String contactNo = jwtTokenUtil.getUsernameFromToken(token);
        return accountRepository.findByAccountNo(contactNo);
    }*/

    public String createAccount(User newUser, String accountType) {
        Account account = new Account();
        Date now = new Date();
        account.setUser(newUser);
        account.setAccountNo(newUser.getAccountDetails().getAccountNo());
        account.setAccountType(accountType);
        account.setAccountStatus("active");
        account.setCreateDateTime(now);
        accountRepository.save(account);
        return Constants.ACCOUNT_CREATED;
    }

    public Account getAccountByUsername(String username) {
        return accountRepository.findAccountByUsername(username);
    }

    /** check if new user by username or email has an existing account, return false if user already exist with an account
     *  for this project, just assume that any user (ie: identified by username or email) is limited to only 1 valid account.
     *  return false if new user already have an existing account which is "ACTIVE"
     *
     * @param username
     * @param email
     * @return boolean
     */
    public boolean checkAccountByUsernameOrEmail(String username, String email) {
        Account userAccount =  accountRepository.findAccountByUsernameOrEmail(username, email);
        if (userAccount != null && userAccount.getAccountStatus().equals("active")){
            return false;
        }
        else if (userAccount == null){
            return true;
        }
        return false;
    }

    public void setAccountDormant(String username, String email) {
        Account userAccount =  accountRepository.findAccountByUsernameOrEmail(username, email);
        if (userAccount.getAccountStatus().equals("active")){
            userAccount.setAccountStatus("dormant");
            userAccount.setAccountBalance(0.00);
            userAccount.setUpdateDateTime(new Date());
        }

    }

    public void setAccountDisabled(String username, String email) {
        Account userAccount =  accountRepository.findAccountByUsernameOrEmail(username, email);
        if (userAccount.getAccountStatus().equals("active")){
            userAccount.setAccountStatus("disabled");
            userAccount.setAccountBalance(0.00);
            userAccount.setUpdateDateTime(new Date());
        }

    }

    public Account getAccountByAccountNo(String accountNo) {
        return accountRepository.findByAccountNo(accountNo);
    }

    public void updateAccount(Account account) {
        accountRepository.save(account);
    }
}
