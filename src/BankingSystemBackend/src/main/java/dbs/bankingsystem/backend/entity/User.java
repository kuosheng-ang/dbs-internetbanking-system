package dbs.bankingsystem.backend.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import java.util.ArrayList;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Transient;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author: ANG KUO SHENG CLEMENT
 * @date: 9-Sep-2023
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")

public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "username", updatable = true)
    public String username;

    @Column(name = "firstName", updatable = true)
    private String firstName;

    @Column(name = "lastName", updatable = true)
    private String lastName;
    @Column(name = "email", nullable = false,updatable = true)
    private String email;
    @Column( nullable = false)
    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;
    @Transient
    private String confirmPassword;
    private Date dob;
    @Column(name = "age",updatable = true)
    private int age;
    private String status;
    @Column(name = "active")
    public boolean enabled = true;
    @ManyToOne
    @PrimaryKeyJoinColumn(name = "address_id")
    private Address customerAddress;
    @OneToOne( cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn( name = "contact_id")
    private Contact contactDetails;

    @OneToOne(cascade = CascadeType.ALL )
    @PrimaryKeyJoinColumn(name = "account_id")
    private Account accountDetails;
    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name = "users_roles",joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id",  referencedColumnName = "id"))
    private Collection<Role> roles = new ArrayList<>();

    public User(String username, String firstName, String lastName, String encodePassword, String email, Collection<Role> roles, boolean active, String status) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
        this.password = encodePassword;
        this.email = email;
        this.enabled = active;
        this.status = status;
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID customerNumber) {
        this.id = customerNumber;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getConfirmPassword() {
        return this.confirmPassword;
    }

    public Collection<Role> getRoles() {
        return this.roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Date getDob() {
        return this.dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Address getCustomerAddress() {
        return this.customerAddress;
    }

    public void setCustomerAddress(Address customerAddress) {
        this.customerAddress = customerAddress;
    }

    public Contact getContactDetails() {
        return this.contactDetails;
    }

    public void setContactDetails(Contact contactDetails) {
        this.contactDetails = contactDetails;
    }

    public Account getAccountDetails() {
        return this.accountDetails;
    }

    public void setAccountDetails(Account accountDetails) {
        this.accountDetails = accountDetails;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }
}