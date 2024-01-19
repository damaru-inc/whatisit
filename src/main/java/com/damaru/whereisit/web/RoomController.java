package com.damaru.whereisit.web;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.logging.Logger;

import com.damaru.whereisit.model.EditAction;
import com.damaru.whereisit.model.Room;
import com.damaru.whereisit.service.Repository;

@Named
@SessionScoped
public class RoomController extends Controller implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Logger log;
 
    @Inject
    private Repository repository;
    
    @Inject
    private FacesContext facesContext;
    
    private Room room = new Room();
    
    public void newListener(ActionEvent e) {
        
        if (getEditAction() == EditAction.edit) {
            return;
        }
        
        setMessage("Enter new room:");
        room = new Room();
        setEditAction(EditAction.create);
    }
    
    public void saveListener(ActionEvent event) {

        if (getEditAction() == EditAction.none) {
            return;
        }

        try {
            log.infof("Saving %s", room);
            boolean isPersisted = room.isPersisted();
            repository.save(room);
            String msg = isPersisted ? 
                    "Updated the room." : "A new room with id " + room.getId() + " has been created successfully";
            setMessage(msg);
            select(room);
            setEditAction(EditAction.none);
        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error occurred while creating the room.", e.getMessage()));
        }
    }

    private void select(Room room) {
        // do we need this?
    }
    
    public void cancelListener(ActionEvent e) {

        if (getEditAction() == EditAction.none) {
            setMessage("");
            return;
        }

        setMessage("Cancelled.");
        setEditAction(EditAction.none);
        
        if (room.isPersisted()) {
            room = repository.findRoomById(room.getId());
        } else {
            room = new Room();
        }

    }
    
    public void editListener(ActionEvent e) {

        if (getEditAction() == EditAction.create) {
            return;
        }

        setMessage("Editing: ");
        setEditAction(EditAction.edit);
    }

    public void deleteListener(ActionEvent ev) {
        
        log.info("deleteListener: " + getEditAction());

        if (getEditAction() != EditAction.none || !room.isPersisted()) {
            return;
        }
        
        try {
            String name = room.getName();
            repository.delete(room);
            room = new Room();
            setEditAction(EditAction.none);
            setMessage("Deleted " + name);
        } catch (Exception e) {
            e.printStackTrace();
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error occurred while deleting the room.", e.getMessage()));
        }
    }


    public void valueChanged(ValueChangeEvent e) {
        Object newValue = e.getNewValue();
        log.tracef("Value changed: %s", newValue);
        
        if (newValue == null) {
            return;
        }
        
        setEditAction(EditAction.none);
        setMessage("");
        room = (Room) e.getNewValue();
        
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        log.debugf("setRoom: %s", room);
        
        // Don't overwrite this from the listbox if we're editing.
        if (room != null && !getEditable()) {
            this.room = room;
        }
    }
}
