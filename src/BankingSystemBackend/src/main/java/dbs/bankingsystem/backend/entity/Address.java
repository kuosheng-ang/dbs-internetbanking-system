package dbs.bankingsystem.backend.entity;

import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.util.Collection;
import java.util.UUID;

/**
 * @author: ANG KUO SHENG CLEMENT
 * @date: 9-Sep-2023
 */
@AllArgsConstructor
@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO)
    private UUID id;

    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    @OneToOne(
            cascade = {CascadeType.ALL}
    )
    @PrimaryKeyJoinColumn(
            name = "bank_id"
    )
    private Bank bank;
    @OneToMany(
            mappedBy = "customerAddress",
            cascade = {CascadeType.ALL}
    )
    private Collection<User> userList;

    public Address() {
    }

    public Address(String city, String state, String country, String zipCode) {
        this.city = city;
        this.state = state;
        this.country = country;
        this.zipCode = zipCode;
    }

    public String getAddress1() {
        return this.address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return this.address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public UUID getAddressId() {
        return this.id;
    }

    public void setAddressId(UUID addressId) {
        this.id = addressId;
    }

    public Collection<User> getUserList() {
        return this.userList;
    }

    public void setUserList(Collection<User> userList) {
        this.userList = userList;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getZipCode() {
        return this.zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Bank getBank() {
        return this.bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

}
