package dbs.bankingsystem.backend.controller;

import dbs.bankingsystem.backend.config.JwtTokenUtil;
import dbs.bankingsystem.backend.dto.JwtRequest;
import dbs.bankingsystem.backend.entity.User;
import dbs.bankingsystem.backend.exception.BadRequestException;
import dbs.bankingsystem.backend.exception.UserNotFound;
import dbs.bankingsystem.backend.repositories.UserRepository;
import dbs.bankingsystem.backend.security.auth.myUserDetailsService;
import dbs.bankingsystem.backend.service.JwtUserDetailService;
import dbs.bankingsystem.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@CrossOrigin(origins = "http://localhost:8087")
@RequestMapping("/api")
public class AuthenticationController {

    @Autowired
    @Resource(name="authenticationManager")
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    /*@Autowired
    private JwtTokenUtil jwtTokenUtil;*/

    @Autowired
    private JwtUserDetailService userDetailsService;

    @Autowired
    private myUserDetailsService userSecurityService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

    @GetMapping("/")
    public String home() {

        SecurityContext sc = SecurityContextHolder.getContext();
        String loggedUser =  sc.getAuthentication().getName();

        return "index";

    }

    @GetMapping(value="/login")
    public ModelAndView login() {
        ModelAndView mv = new ModelAndView("login/login");
        return mv;

    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest,
                                                       @RequestParam("email") String email, @RequestParam("password") String password, HttpServletRequest request) throws Exception {
                                                        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        /**  Only if using jwt token for authentication -- temporarily in development stage **/

        /*final String token = jwtTokenUtil.generateTokenForLogin(userDetails);
        User user = userService.getUserFromToken(token);
        ModelMap map = new ModelMap();
        map.addAttribute("token", token);
        map.addAttribute("user", user); */

        User currentUser = userRepository.findByEmail(email);
        //User currentUser = userRepository.findUserByEmail(email);
        System.out.println("email"+currentUser);
        LOGGER.info("{} Inside login()",email);
        ModelMap map = new ModelMap();
        if (currentUser == null)  {

            map.addAttribute("loginStatus", "Missing or empty valid. Please try again");
            LOGGER.error("Email not found: "+email);
            throw new UserNotFound("Username not found: "+ currentUser.getUsername());

        }

        Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(currentUser.getEmail());

        if (!matcher.matches()) {
            throw new BadRequestException("Invalid email type");
        }
        boolean loginResponse = userSecurityService.login(email, password);
        if(loginResponse) {

            LOGGER.info("login successful",email,password);
            LOGGER.info("login Status", loginResponse);
            UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(currentUser.getUsername(), password);
            Authentication auth = authenticationManager.authenticate(authReq);
            SecurityContext sc = SecurityContextHolder.getContext();
            sc.setAuthentication(auth);
            map.addAttribute("userAuthenticated", auth.isAuthenticated());
           /* modelmap.addAttribute("attr-username", username);
            String authUserName = _userSecurityService.getCurrentUser().getName();
            modelmap.addAttribute("authUserName", authUserName);*/
            // modelmap.addAttribute("loginStatus", "login successful");

            HttpSession session = request.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", sc);


        }
        else  {
            LOGGER.info("User entered Invalid credentials email:{} and password:{}",email,password);
            map.addAttribute("msg","Invalid username or password");

        }
        //LOGGER.info("login Status", loginResponse);
        return ResponseEntity.ok(map);
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

}
