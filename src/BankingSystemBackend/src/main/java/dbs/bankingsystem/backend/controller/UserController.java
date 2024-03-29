package dbs.bankingsystem.backend.controller;

import dbs.bankingsystem.backend.dto.Constants;
import dbs.bankingsystem.backend.dto.Response;
import dbs.bankingsystem.backend.entity.User;
import dbs.bankingsystem.backend.exception.BadRequestException;
import dbs.bankingsystem.backend.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin(origins = "http://localhost:8087")
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    private final Logger logger = LogManager.getLogger(getClass());

    @PostMapping("/register")
    public ResponseEntity<Response> createUser(@RequestBody User user, @RequestParam("accountType") String accountType) {
        try {
            return ResponseEntity.ok(new Response(Constants.SUCCESS, userService.createUser(user, accountType)));
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(Constants.ERROR, e.getMessage()));
        } catch (Exception e) {
            logger.error("error produced during creating user : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response(Constants.ERROR, e.getMessage()));
        }
    }

    @GetMapping("/user")
    public ResponseEntity<User> getUserFromToken(HttpServletResponse response, HttpServletRequest request) {
        try {
            //return ResponseEntity.ok(userService.getUserFromToken(request.getHeader("token")));
            return ResponseEntity.ok(userService.getUserFromEmail(request.getParameter("email")));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/user")
    public ResponseEntity<Response> updateUser(@RequestBody User user) {
        try {
            return ResponseEntity.ok(new Response(Constants.SUCCESS, userService.updateUser(user)));
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(Constants.ERROR, e.getMessage()));
        } catch (Exception e) {
            logger.info("error produced during updating user : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response(Constants.ERROR, e.getMessage()));
        }
    }
}
