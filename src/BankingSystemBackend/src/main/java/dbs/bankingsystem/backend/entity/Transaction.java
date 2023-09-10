package dbs.bankingsystem.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Date;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String transferFrom;
    private String transferTo;
    private Date txDateTime;
    private String txType;
    private Double txAmount;
    @ManyToOne(cascade = CascadeType.ALL)
    private Account account;

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTransferFrom() {
        return this.transferFrom;
    }

    public void setTransferFrom(String transferFrom) {
        this.transferFrom = transferFrom;
    }

    public String getTransferTo() {
        return this.transferTo;
    }

    public void setTransferTo(String transferTo) {
        this.transferTo = transferTo;
    }


    public Account getAccount() {
        return this.account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Date getTxDateTime() {
        return this.txDateTime;
    }

    public void setTxDateTime(Date txDateTime) {
        this.txDateTime = txDateTime;
    }

    public String getTxType() {
        return this.txType;
    }

    public void setTxType(String txType) {
        this.txType = txType;
    }

    public Double getTxAmount() {
        return this.txAmount;
    }

    public void setTxAmount(Double txAmount) {
        this.txAmount = txAmount;
    }

    public Transaction() {
    }
}
