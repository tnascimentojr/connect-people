package org.caxineirosdegema.connectingpeople.services;

import org.caxineirosdegema.connectingpeople.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserServiceIMPL implements UserService{

    private ApplicationService applicationService;
    private Integer userID = 0;


    @Override
    public boolean addUser(User user) {

        Set<User> userSet = applicationService.getUserSet();

        if(!userSet.contains(user)) {
            applicationService.registUser(user);
            return true;
        }
        return false;
    }

    @Override
    public User get(Integer id) {

        Set<User> userSet = applicationService.getUserSet();

        for (User user : userSet) {
            if(user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public boolean saveOrUpdate(User oldUser, User newUser) {

        List<User> userList = newUser.getFriendsList();

        for (User user1 : userList) {
            user1.getFriendsList().remove(oldUser);
            user1.getFriendsList().add(newUser);
        }

        return true;
    }

    @Override
    public boolean delete(Integer id) {

        User toDelete = get(id);

        List<User> userList = toDelete.getFriendsList();
        Set<User> userSet = applicationService.getUserSet();

        for(User user : userList) {
            user.getFriendsList().remove(toDelete);
        }

        for(User user : userSet) {

            if(user.getId().equals(id)) {
                userSet.remove(toDelete);
                return true;
            }
        }


        return false;
    }

    @Override
    public Integer getNextId() {
        Integer toReturn = ++userID;
        this.userID = userID++;

        return toReturn;
    }

    public ApplicationService getApplicationService() {
        return applicationService;
    }


    @Autowired
    public void setApplicationService(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }
}
