package dbs.bankingsystem.backend.security;

import dbs.bankingsystem.backend.entity.*;
import dbs.bankingsystem.backend.repositories.UserRepository;
import java.util.ArrayList;
import java.util.List;

public class ActiveUserStore {
    private UserRepository _userRepo;
    public List<User> users = new ArrayList();

    public ActiveUserStore() {
    }

    public List<User> getUsers(String email) {
        return users = _userRepo.findAllUsersByEmail(email);
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
