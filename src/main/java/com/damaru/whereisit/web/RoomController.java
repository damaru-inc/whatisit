package com.damaru.whereisit.web;

import java.util.logging.Logger;

import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.damaru.whereisit.model.Room;
import com.damaru.whereisit.service.Repository;

@Model
public class RoomController {

    @Inject
    private Logger log;

    @Inject
    private Repository repository;
    
    @Inject
    private FacesContext facesContext;

    @Named
    @Produces
    private Room newRoom = new Room();
    
    public String create() {
        try {
            log.info("Saving " + newRoom);
            repository.save(newRoom);
            String message = "A new room with id " + newRoom.getId() + " has been created successfully";
            facesContext.addMessage(null, new FacesMessage(message));
            newRoom = new Room();
        } catch (Exception e) {
            String message = "An error has occured while creating the room (see log for details)";
            facesContext.addMessage(null, new FacesMessage(message));
        }
        return "room";
    }
}
