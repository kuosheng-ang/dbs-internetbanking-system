package dbs.bankingsystem.backend.util;


import dbs.bankingsystem.backend.entity.*;
import dbs.bankingsystem.backend.repositories.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    boolean alreadySetup = false;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PrivilegeRepository privilegeRepository;
    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public SetupDataLoader() {
    }

    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!this.alreadySetup) {
            Privilege readPrivilege = this.createPrivilegeIfNotFound("READ_PRIVILEGE");
            Privilege writePrivilege = this.createPrivilegeIfNotFound("WRITE_PRIVILEGE");
            Privilege passwordPrivilege = this.createPrivilegeIfNotFound("CHANGE_PASSWORD_PRIVILEGE");
            List<Privilege> adminPrivileges = Arrays.asList(readPrivilege, writePrivilege, passwordPrivilege);
            List<Privilege> userPrivileges = Arrays.asList(readPrivilege, passwordPrivilege);
            this.createRoleIfNotFound("ADMIN", adminPrivileges);
            this.createRoleIfNotFound("USER", userPrivileges);
            Role adminRole = this.roleRepository.findByRole("ADMIN");
            this.createUserIfNotFound("admin", "Test", "Test", "test$123", "admin@dbs-banking.com.sg", Arrays.asList(adminRole), "active");
            Role userRole = this.roleRepository.findByRole("USER");



            createUserIfNotFound("kristy.yang", "kristy", "yang", "p97312601z", "kristy.yang@baidu.com.cn", Arrays.asList(userRole), "active");
            createUserIfNotFound("janet.lee", "janet", "lee", "p9731260z", "janet.lee@yahoo.com.tw", Arrays.asList(userRole), "active");
            createUserIfNotFound("bill.gates", "bill", "gates", "p97260z", "bill.gates@msn.com", Arrays.asList(userRole), "active");

            createContactIfNotFound("kristy.yang", "419-800-6759", "605-794-4895", "931-313-9635");
            createContactIfNotFound("janet.lee", "715-662-6764", "212-934-5167", "201-709-6245");
            createContactIfNotFound("bill.gates", "414-377-2880", "212-753-2740", "913-413-4604");

            createAccountIfNotFound("kristy.yang", "SAVINGS", "952479-2375", "ACTIVE", 3508.05);
            createAccountIfNotFound("janet.lee", "TERM DEPOSIT", "215794-4519", "ACTIVE", 52612.48);
            createAccountIfNotFound("bill.gates", "INTERNET BANKING", "803681-3678", "ACTIVE", 159677.58);

            alreadySetup = true;
        }
    }

    @Transactional
    User createUserIfNotFound(final String username, final String firstName, final String lastName, final String password, final String email, final Collection<Role> roles, final String status) {
        User initialUser = this.userRepository.findByUsername(username);
        if (initialUser == null) {
            initialUser = new User(username, firstName, lastName, this.passwordEncoder.encode(password), email, roles, true, status);
        }

        initialUser = (User)this.userRepository.save(initialUser);
        return initialUser;
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {
        Privilege privilege = this.privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            this.privilegeRepository.save(privilege);
        }

        return privilege;
    }

    @Transactional
    Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {
        Role role = this.roleRepository.findByRole(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            this.roleRepository.saveAndFlush(role);
        }

        return role;
    }

    @Transactional
    Contact createContactIfNotFound(final String username, final String homePhone, final String workPhone, final String mobilePhone) {
        Contact contact = this.contactRepository.findByUsername(username);
        User user = this.userRepository.findByUsername(username);
        if (contact == null) {
            contact = new Contact(user, homePhone, workPhone, mobilePhone);
        }

        contact = contactRepository.saveAndFlush(contact);
        return contact;
    }

    @Transactional
    Account createAccountIfNotFound(final String username, final String accountType, final String accountNum, final String accountStatus, final Double accountBalance) {
        User user = this.userRepository.findByUsername(username);
        Account userAccount = this.accountRepository.findAccountByUsernameOrEmail(username, user.getEmail());

        if (userAccount == null) {
            userAccount = new Account(user, accountType, accountNum, accountStatus, accountBalance);
        }

        userAccount = accountRepository.saveAndFlush(userAccount);
        return userAccount;
    }
}
