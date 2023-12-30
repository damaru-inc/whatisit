package com.damaru.whereisit.web;

import java.util.logging.Logger;

import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.damaru.whereisit.model.Container;
import com.damaru.whereisit.model.Room;
import com.damaru.whereisit.service.Repository;

@Model
public class ContainerController {
    @Inject
    private Logger log;

    @Inject
    private Repository repository;
    
    @Inject
    private FacesContext facesContext;

    private Container newContainer = new Container();
    
    private Long selectedRoomId = 0L;
        
    public String create() {
        try {
            log.info("Saving " + newContainer + " with selectedRoomId " + selectedRoomId);
            Room room = repository.findRoomById(selectedRoomId);
            newContainer.setRoom(room);
            repository.save(newContainer);
            String message = "A new container with id " + newContainer.getId() + " has been created successfully";
            facesContext.addMessage(null, new FacesMessage(message));
            newContainer = new Container();
        } catch (Exception e) {
            String message = "An error has occured while creating the container (see log for details)";
            facesContext.addMessage(null, new FacesMessage(message));
        }
        return "container";
    }

    public Container getNewContainer() {
        return newContainer;
    }

    public void setNewContainer(Container newContainer) {
        this.newContainer = newContainer;
    }

    public Long getSelectedRoomId() {
        return selectedRoomId;
    }


    public void setSelectedRoomId(Long selectedRoomId) {
        this.selectedRoomId = selectedRoomId;
    }
}
