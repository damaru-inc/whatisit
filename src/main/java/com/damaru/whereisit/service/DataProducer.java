package com.damaru.whereisit.service;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import com.damaru.whereisit.model.Room;

@RequestScoped
public class DataProducer {

    @Inject
    private Logger log;

    @Inject
    private Repository repository;
    
    private List<Room> rooms;
    
    @PostConstruct
    public void init() {
        log.info("Called init.");
        rooms = repository.findAllRooms();
    }
    
    public void afterRoomUpdate(@Observes Room room) {
        log.info("afterRoomUpdate " + room);
        init();
    }
    
    @Produces
    @Named
    public List<Room> getRooms() {
        //log.info("Called getRooms.");
        return rooms;
    }

    
}
