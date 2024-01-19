package com.damaru.whereisit.web;

import java.util.List;

import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.jboss.logging.Logger;

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
    
    private List<Item> searchResults;
    
    private String searchString;
    
    public String create() {
        log.infof("Saving item %s with containerId %d", newItem, selectedContainerId);
        try {
            Container container = repository.findContainerById(selectedContainerId);
            newItem.setContainer(container);
            repository.save(newItem);
            String message = "A new item with id " + newItem.getId() + " has been created successfully";
            facesContext.addMessage(null, new FacesMessage(message));
            newItem = new Item();
        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error occurred while creating the item.", e.getMessage()));
        }
        return "item";
    }
    
    public void search() {
        searchResults = repository.searchItems(searchString);
    }

    public Item getNewItem() {
        return newItem;
    }

    public void setNewItem(Item newItem) {
        this.newItem = newItem;
    }

    public List<Item> getSearchResults() {
        return searchResults;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public Long getSelectedContainerId() {
        return selectedContainerId;
    }

    public void setSelectedContainerId(Long selectedContainerId) {
        this.selectedContainerId = selectedContainerId;
    }

}
