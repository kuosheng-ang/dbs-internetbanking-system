package dbs.bankingsystem.backend.service;

import dbs.bankingsystem.backend.config.JwtTokenUtil;
import dbs.bankingsystem.backend.entity.Contact;
import dbs.bankingsystem.backend.repositories.RoleRepository;
import dbs.bankingsystem.backend.repositories.UserRepository;
import dbs.bankingsystem.backend.dto.Constants;
import dbs.bankingsystem.backend.entity.User;
import dbs.bankingsystem.backend.exception.BadRequestException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Years;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * @author: ANG KUO SHENG CLEMENT
 * @date: 9-Sep-2023
 */

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private final Logger logger = LogManager.getLogger(getClass());
    @Autowired
    private RoleRepository roleRepository;

    public String createUser(User user, String accountType) {
        if (userRepository.findByEmail(user.getEmail()) != null || userRepository.findByUsername(user.getUsername()) != null) {
            logger.info("user already exist with email : {} , username: {}", user.getEmail(), user.getUsername());
            throw new BadRequestException(Constants.USER_EXIST);
        }
        User newUser = new User();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        newUser.setPassword(encoder.encode(user.getPassword()));
        Date now = new Date();
        newUser.setAge(getAgeBetween(user.getDob(), now));
        newUser.setEmail(user.getEmail());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setDob(user.getDob());
        newUser.setRoles(Arrays.asList(roleRepository.findByRole("USER")));
        newUser.setStatus("active");
        newUser = userRepository.save(newUser);
        if (user.getContactDetails() != null){
            Contact newUserContact = new Contact();
            newUserContact.setUser(newUser);
            newUserContact.setHomePhone(user.getContactDetails().getHomePhone());
            newUserContact.setMobilePhone(user.getContactDetails().getMobilePhone());
            newUserContact.setWorkPhone(user.getContactDetails().getWorkPhone());
        }
        if (user.getAccountDetails() != null && accountService.checkAccountByUsernameOrEmail(user.getUsername(), user.getEmail())){
            return accountService.createAccount(newUser, accountType);
        }
        else{
            throw new BadRequestException(Constants.ACCOUNT_ALREADY_EXISTED);
        }


        //return accountService.createAccount(newUser);
    }

    private int getAgeBetween(Date dob, Date now) {
        DateTime dobjoda = new DateTime(dob);
        DateTime nowjoda = new DateTime(now);
        return Years.yearsBetween(dobjoda, nowjoda).getYears();
    }

    //public User getUserFromToken(String token) {
    //String contactNo = jwtTokenUtil.getUsernameFromToken(token);
    public User getUserFromEmail(String email) {

        return userRepository.findByEmail(email);
    }

    public String updateUser(User user) {
        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser==null){
            logger.info("User not found with contact no : {}",user.getContactDetails().getMobilePhone());
            throw new BadRequestException("User not found with contact no : "+user.getContactDetails().getMobilePhone());
        }
        user.setId(existingUser.getId());
        user.setContactDetails(existingUser.getContactDetails());
        user.setPassword(existingUser.getPassword());
        user.setAge(getAgeBetween(user.getDob(),new Date()));
        userRepository.save(user);
        return Constants.USER_UPDATED;
    }

    public String getUsernameByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return user.getUsername();
    }

    public String getUsernameByAccountNo(String accountNo) {
        User user = userRepository.findUserByAccountNum(accountNo);
        return user.getUsername();
    }
}
