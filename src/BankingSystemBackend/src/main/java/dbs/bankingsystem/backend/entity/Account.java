package dbs.bankingsystem.backend.entity;

import lombok.AllArgsConstructor;

import javax.persistence.*;
//import org.springframework.data.mongodb.core.mapping.Document;
import javax.persistence.Id;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import javax.persistence.OneToOne;
import javax.persistence.Table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import javax.persistence.*;

/**
 * @author: ANG KUO SHENG CLEMENT
 * @date: 9-Sep-2023
 */

@AllArgsConstructor
@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String accountNo;
    private String accountStatus;
    private String accountType;
    private Double accountBalance;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "accountDetails" )
    private User user;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "account")
    private Collection<Transaction> transactions = new ArrayList();
    @Temporal(TemporalType.TIME)
    private Date createDateTime;
    @Temporal(TemporalType.TIME)
    private Date updateDateTime;

    public Account(User username, String accountType, String accountNum, String accountStatus, Double accountBalance) {
        this.user = username;
        this.accountNo = accountNum;
        this.accountType = accountType;
        this.accountStatus = accountStatus;
        this.accountBalance = accountBalance;
    }

    public UUID getAccountId() {
        return this.id;
    }

    public void setAccountId(UUID accountId) {
        this.id = accountId;
    }

    public Date getCreateDateTime() {
        return this.createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }

    public Date getUpdateDateTime() {
        return this.updateDateTime;
    }

    public void setUpdateDateTime(Date updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    public String getAccountNo() {
        return this.accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountStatus() {
        return this.accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getAccountType() {
        return this.accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Double getAccountBalance() {
        return this.accountBalance;
    }

    public void setAccountBalance(Double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public Date getAccountCreated() {
        return this.createDateTime;
    }

    public void setAccountCreated(Date accountCreated) {
        this.createDateTime = accountCreated;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Collection<Transaction> getTransactions() {
        return this.transactions;
    }

    public void setTransactions(Collection<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Account() {
    }

}
