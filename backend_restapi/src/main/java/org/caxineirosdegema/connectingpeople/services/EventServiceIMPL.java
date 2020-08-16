package org.caxineirosdegema.connectingpeople.services;

import org.caxineirosdegema.connectingpeople.model.Event;
import org.caxineirosdegema.connectingpeople.model.User;
import org.caxineirosdegema.connectingpeople.services.UserService;
import org.caxineirosdegema.connectingpeople.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class EventServiceIMPL implements EventService {

    private UserService userService;

    @Override
    public boolean addEvent(Event event, Integer uid) {

        User owner = userService.get(uid);

        event.setOwnerName(owner.getName());
        owner.getEventSet().add(event);

        List<User> userList = owner.getFriendsList();

        for (User user : userList) {
            user.getEventSet().add(event);
        }

        return true;
    }

    @Override
    public Event get(Integer uid, Integer eid) {

        User owner = userService.get(uid);
        Set<Event> eventSet = owner.getEventSet();

        for(Event event : eventSet) {
            if(event.getId().equals(eid)) {
                return event;
            }
        }
        return null;
    }

    @Override
    public boolean saveOrUpdate(Event event, Integer uid) {

        User owner = userService.get(uid);
        List<User> userList = owner.getFriendsList();
        Set<Event> eventSet = owner.getEventSet();

        Event toDelete = null;

        for(Event oldEvent : eventSet) {
            if(event.getId().equals(oldEvent.getId())) {

                toDelete = oldEvent;

                eventSet.remove(oldEvent);
                eventSet.add(event);
            }
        }

        for(User user: userList) {
            user.getEventSet().remove(toDelete);
            user.getEventSet().add(event);
        }


        return true;
    }

    @Override
    public boolean delete(Integer uid, Integer eid) {

        User owner = userService.get(uid);
        Event toDelete = get(uid, eid);


        Set<Event> ownerEventSet = owner.getEventSet();

        for (Event eventToDelete : ownerEventSet) {
            if(eventToDelete.getId().equals(eid)) {
                ownerEventSet.remove(toDelete);
            }
        }

        List<User> userList = owner.getFriendsList();

        for (User user: userList) {
           user.getEventSet().remove(toDelete);
        }

        return true;
    }

    public UserService getUserService() {
        return userService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
