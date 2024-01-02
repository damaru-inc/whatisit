package com.damaru.whereisit.web;

import java.util.logging.Logger;

import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.damaru.whereisit.model.Container;
import com.damaru.whereisit.model.Item;
import com.damaru.whereisit.service.Repository;

@Model
public class ItemController {
    
    @Inject
    private Logger log;

    @Inject
    private Repository repository;
    
    @Inject
    private FacesContext facesContext;

    private Item newItem = new Item();
    
    private Long selectedContainerId = 0L;
    
    private Long selectedRoomId = 0L;
    
    public String create() {
        log.info("Saving item " + newItem + " with containerId " + selectedContainerId);
        Container container = repository.findContainerById(selectedContainerId);
        newItem.setContainer(container);
        repository.save(newItem);
        String message = "A new item with id " + newItem.getId() + " has been created successfully";
        facesContext.addMessage(null, new FacesMessage(message));
        newItem = new Item();
        return "item";
    }

    public Item getNewItem() {
        return newItem;
    }

    public void setNewItem(Item newItem) {
        this.newItem = newItem;
    }

    public Long getSelectedContainerId() {
        return selectedContainerId;
    }

    public void setSelectedContainerId(Long selectedContainerId) {
        this.selectedContainerId = selectedContainerId;
    }

    public Long getSelectedRoomId() {
        return selectedRoomId;
    }

    public void setSelectedRoomId(Long selectedRoomId) {
        this.selectedRoomId = selectedRoomId;
    }
}
