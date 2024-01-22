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

import com.damaru.whereisit.model.Container;
import com.damaru.whereisit.model.EditAction;
import com.damaru.whereisit.model.Room;
import com.damaru.whereisit.service.Repository;

@Named
@SessionScoped
public class ContainerController extends Controller implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Inject
    private Logger log;

    @Inject
    private Repository repository;
    
    @Inject
    private FacesContext facesContext;

    private Container container = new Container();
    
    private Room selectedRoom;
        
    /*
    public String create() {
        try {
            log.infof("Saving %s with selectedRoomId %d", newContainer, selectedRoomId);
            Room room = repository.findRoomById(selectedRoomId);
            newContainer.setRoom(room);
            repository.save(newContainer);
            String message = "A new container with id " + newContainer.getId() + " has been created successfully";
            facesContext.addMessage(null, new FacesMessage(message));
            newContainer = new Container();
        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error occurred while creating the container.", e.getMessage()));
        }
        return "container";
    }
    */
    
    public void newListener(ActionEvent e) {
        
        if (getEditAction() == EditAction.edit) {
            return;
        }
        
        setMessage("Enter new container:");
        container = new Container();
        setEditAction(EditAction.create);
    }
    
    public void saveListener(ActionEvent event) {

        if (getEditAction() == EditAction.none) {
            return;
        }

        try {
            log.infof("Saving container %s room %s", container, selectedRoom);
            
            if (selectedRoom == null) {
                setMessage("No room selected.");
                return;
            }
            
            container.setRoom(selectedRoom);
            boolean isPersisted = container.isPersisted();        
            repository.save(container);
            setMessage("Saved.");
            setEditAction(EditAction.none);
        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error occurred while saving the container.", e.getMessage()));
        }
    }

    public void cancelListener(ActionEvent e) {

        if (getEditAction() == EditAction.none) {
            setMessage("");
            return;
        }

        setMessage("Cancelled.");
        setEditAction(EditAction.none);
        
        if (container != null && container.isPersisted()) {
            container = repository.findContainerById(container.getId());
        } else {
            container = new Container();
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

        if (getEditAction() != EditAction.none || !container.isPersisted()) {
            return;
        }
        
        try {
            String name = container.getName();
            repository.delete(container);
            container = new Container();
            setEditAction(EditAction.none);
            setMessage("Deleted " + name);
        } catch (Exception e) {
            e.printStackTrace();
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error occurred while deleting the container.", e.getMessage()));
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
        container = (Container) e.getNewValue();
        selectedRoom = container.getRoom();
    }



    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        log.infof("setContainer: %s", container);
        // Don't overwrite this from the listbox if we're editing.
        if (container != null && !getEditable()) {
            this.container = container;
        }
    }

    public Room getSelectedRoom() {
        return selectedRoom;
    }

    public void setSelectedRoom(Room selectedRoom) {
        this.selectedRoom = selectedRoom;
    }

}
