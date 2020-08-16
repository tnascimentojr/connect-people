package org.caxineirosdegema.connectingpeople.services;

import org.caxineirosdegema.connectingpeople.model.User;

import java.util.Set;

public interface ApplicationService {

    void registUser(User user);

    Set<User> getUserSet();

    boolean authenticateUser(String email, String password);

}
