package dbs.bankingsystem.backend.entity;


import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author: ANG KUO SHENG CLEMENT
 * @date: 9-Sep-2023
 */

@Entity
@Table( name = "bank")
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String branchName;
    private Integer branchCode;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "bank" )
    private Address branchAddress;
    private Integer routingNumber;

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getBranchName() {
        return this.branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public Integer getBranchCode() {
        return this.branchCode;
    }

    public void setBranchCode(Integer branchCode) {
        this.branchCode = branchCode;
    }

    public Address getBranchAddress() {
        return this.branchAddress;
    }

    public void setBranchAddress(Address branchAddress) {
        this.branchAddress = branchAddress;
    }

    public Integer getRoutingNumber() {
        return this.routingNumber;
    }

    public void setRoutingNumber(Integer routingNumber) {
        this.routingNumber = routingNumber;
    }

}
