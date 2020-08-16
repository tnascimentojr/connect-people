package org.caxineirosdegema.connectingpeople.controller.rest;

import org.caxineirosdegema.connectingpeople.model.Event;
import org.caxineirosdegema.connectingpeople.model.User;
import org.caxineirosdegema.connectingpeople.services.ApplicationService;
import org.caxineirosdegema.connectingpeople.services.EventService;
import org.caxineirosdegema.connectingpeople.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.print.DocFlavor;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RestAPIController {

    private ApplicationService applicationService;
    private UserService userService;
    private EventService eventService;


    @RequestMapping(method = RequestMethod.POST, path= "/login")
    public ResponseEntity<User> login(@Valid @RequestBody User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(applicationService.authenticateUser(user.getEmail(), user.getPassword())) {
            Set<User> userSet = applicationService.getUserSet();

            for (User u: userSet) {
                if (u.getEmail().equals(user.getEmail())) {
                    return new ResponseEntity<User>(u, HttpStatus.OK);
                }
            }

        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @RequestMapping(method = RequestMethod.GET, path = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUser(@PathVariable Integer id) {


        User user = userService.get(id);

        User userDTO = user;
        userDTO.removeComplexObjects();

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.POST, path = "/create-user")
    public ResponseEntity createUser(@Valid @RequestBody User user, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


        user.setId(userService.getNextId());
        user.setFriendsList(new LinkedList<User>());
        user.setEventSet(new HashSet<Event>());

        if(userService.addUser(user)) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }

    }

    @RequestMapping(method = RequestMethod.POST, path = "/edit-user/{id}")
    public ResponseEntity editUser(@Valid @RequestBody User newUser, BindingResult bindingResult, @PathVariable Integer id) {

        if(bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User oldUser = userService.get(id);

        newUser.setId(id);
        newUser.setEmail(oldUser.getEmail());
        newUser.setFriendsList(oldUser.getFriendsList());


        if(userService.saveOrUpdate(oldUser, newUser)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, path="/delete-user/{id}")
    public ResponseEntity deleteUser(@PathVariable Integer id) {
        if(userService.delete(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.POST, path="/user/{id}/add-friend")
    public ResponseEntity<?> addFriend(@Valid @RequestBody User user, BindingResult bindingResult, @PathVariable Integer id) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User u = userService.get(id);

        Set<User> userSet = applicationService.getUserSet();

        for (User toFind : userSet) {
            if (user.getEmail().equals(toFind.getEmail())) {
                u.getFriendsList().add(toFind);
                Set<Event> eventSet = toFind.getEventSet();

                for (Event event: eventSet) {
                    u.getEventSet().add(event);
                }

                eventSet.addAll(u.getEventSet());

                return new ResponseEntity<>(HttpStatus.OK);
            }

        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    @RequestMapping(method = RequestMethod.GET, path = "/user/{id}/event-set", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<Event>> getUserEvents(@PathVariable Integer id) {

        User user = userService.get(id);

        Set<Event> eventSet = user.getEventSet();

        Set<Event> eventSetDTO = new HashSet<>();

        eventSetDTO.addAll(eventSet);

        return new ResponseEntity<>(eventSetDTO, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/user/{id}/friends", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getUserFriends(@PathVariable Integer id) {



        User user = userService.get(id);

        List<User> userList = user.getFriendsList();

        List<User> userListDTO = new LinkedList<>();

        for (User newUser: userList) {
            newUser.removeComplexObjects();

            userListDTO.add(newUser);

        }

        return new ResponseEntity<>(userListDTO, HttpStatus.OK);
    }



    @RequestMapping(method = RequestMethod.GET, path = "/user/{uid}/event/{eid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Event> getEvent(@PathVariable Integer uid, @PathVariable Integer eid) {


        Event event = eventService.get(uid, eid);


        Event eventDTO = event;
        eventDTO.removeComplexObjects();
        eventDTO.setOwnerName(userService.get(uid).getName());


        return new ResponseEntity<>(eventDTO, HttpStatus.OK);



    }

    @RequestMapping(method = RequestMethod.POST, path = "/user/{uid}/create-event")
    public ResponseEntity<User> createEvent(@Valid @RequestBody Event event, BindingResult bindingResult, @PathVariable Integer uid) {

        if(bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(eventService.addEvent(event, uid)) {

            User u = userService.get(uid);

            return new ResponseEntity<User>(u, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }

    }

    @RequestMapping(method = RequestMethod.POST, path = "/user/{uid}/edit-event/{eid}")
    private ResponseEntity editEvent(@PathVariable Integer uid, @PathVariable Integer eid, @Valid @RequestBody Event event, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        event.setOwner(userService.get(uid));
        event.setId(eid);

        if(eventService.saveOrUpdate(event, uid)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @RequestMapping(method = RequestMethod.DELETE, path="/user/{uid}/delete-event/{eid}")
    public ResponseEntity deleteEvent(@PathVariable Integer uid, @PathVariable Integer eid) {

        if(eventService.delete(uid, eid)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }












































    public ApplicationService getApplicationService() {
        return applicationService;
    }

    @Autowired
    public void setApplicationService(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    public UserService getUserService() {
        return userService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public EventService getEventService() {
        return eventService;
    }

    @Autowired
    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }
}
