package org.caxineirosdegema.connectingpeople.services;

import org.caxineirosdegema.connectingpeople.model.Event;
import org.caxineirosdegema.connectingpeople.model.User;

public interface EventService {

    boolean addEvent(Event event, Integer uid);

    Event get(Integer uid, Integer eid);

    boolean saveOrUpdate(Event event, Integer uid);

    boolean delete(Integer uid, Integer eid);

}
