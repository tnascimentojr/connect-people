package org.caxineirosdegema.connectingpeople.services;

import org.caxineirosdegema.connectingpeople.model.User;

public interface UserService {

    boolean addUser(User user);

    User get(Integer id);

    boolean saveOrUpdate(User oldUser, User newUser);

    boolean delete(Integer id);

    Integer getNextId();

}
