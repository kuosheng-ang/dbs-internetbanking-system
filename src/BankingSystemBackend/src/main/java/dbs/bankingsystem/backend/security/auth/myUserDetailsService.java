package dbs.bankingsystem.backend.security.auth;

import dbs.bankingsystem.backend.entity.*;
import dbs.bankingsystem.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;

@Service("userDetailsService")
@Transactional
public class myUserDetailsService implements UserDetailsService {
    //public class UserRepositoryUserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private WebApplicationContext applicationContext;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email);

        if (user == null) {

            throw new UsernameNotFoundException("No user found with username: " + user.getUsername());
            /*return new org.springframework.security.core.userdetails.User(
                    "admin", "p9731260x", true, true, true, true,
                    getAuthorities(Arrays.asList(
                            roleRepo.findByRole("ROLE_ADMIN"))));

            return new org.springframework.security.core.userdetails.User(
                    " ", " ", true, true, true, true,
                    getAuthorities(Arrays.asList(
                            roleRepo.findRoleByName("USER"))));*/
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), user.isEnabled(), true, true,
                true, getAuthorities(user.getRoles()));

    }

    public boolean login(String email, String password) {

        User user = userRepository.findByEmail(email);

        if (user.getEmail().equalsIgnoreCase(email) && user.getPassword().equals(password)) {

            return true;

        } else

            return false;

    }

    /*public boolean login(String username, String password) throws UsernameNotFoundException {

        User user = userRepo.findByUsername(username);
        try {
            if (user.getUsername().equalsIgnoreCase(username) && user.getPassword().equals(password)) {

                return true;


            *//*return new org.springframework.security.core.userdetails.User(
                    "admin", "p9731260x", true, true, true, true,
                    getAuthorities(Arrays.asList(
                            roleRepo.findByRole("ROLE_ADMIN"))));*//*
            } else
                throw new UsernameNotFoundException("No user found with username: " + username);
        } catch (UsernameNotFoundException e) {
             return false;
        }

    }*/
            /*if (user.getUsername().equalsIgnoreCase("admin")) {

                return new org.springframework.security.core.userdetails.User(
                        " ",  " ", true, true, true, true,
                        getAuthorities(Arrays.asList(
                                roleRepo.findByRole("ROLE_ADMIN"))));
                //return admin;
            } else if (user == null) {

                return new org.springframework.security.core.userdetails.User(
                        " ", " ", true, true, true, true,
                        getAuthorities(Arrays.asList(
                                roleRepo.findByRole("ROLE_USER"))));
                //return user;
            } else {

                return new org.springframework.security.core.userdetails.User(
                        user.getUsername(), user.getPassword(), user.isEnabled(), true, true,
                        true, getAuthorities(user.getRoles()));

            } */

            /*return new org.springframework.security.core.userdetails.User(
                    user.getUsername(), user.getPassword(), user.isEnabled(), true, true,
                    true, getAuthorities(user.getRoles()));*/
    // throw new UsernameNotFoundException("User: " + username + " not found!");

    @PostConstruct
    public void completeSetup() {
        userRepository = applicationContext.getBean(UserRepository.class);
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {

        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(Collection<Role> roles) {

        List<String> privileges = new ArrayList<>();
        List<Privilege> collection = new ArrayList<>();
        for (Role role : roles) {
            privileges.add(role.getName());
            collection.addAll(role.getPrivileges());
        }
        for (Privilege item : collection) {
            privileges.add(item.getPrivilege());
        }
        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }

    private String getClientIP() {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null) {
            return xfHeader.split(",")[0];
        }
        return request.getRemoteAddr();
    }

    public Authentication getCurrentUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();


        return authentication;
    }

}
