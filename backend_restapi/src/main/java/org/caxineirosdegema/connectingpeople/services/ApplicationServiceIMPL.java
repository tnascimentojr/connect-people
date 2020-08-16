package org.caxineirosdegema.connectingpeople.services;

import org.caxineirosdegema.connectingpeople.model.User;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ApplicationServiceIMPL implements ApplicationService {

    private Set<User> userSet = new HashSet<>();

    @Override
    public void registUser(User user) {

        userSet.add(user);
    }

    @Override
    public Set<User> getUserSet() {
        return userSet;
    }

    @Override
    public boolean authenticateUser(String email, String password) {

        for(User user : userSet) {
            if(user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
}
