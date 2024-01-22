package com.damaru.whereisit.web;

import java.io.Serializable;
import java.util.List;

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
import com.damaru.whereisit.model.Item;
import com.damaru.whereisit.service.Repository;

@Named
@SessionScoped
public class ItemController extends Controller implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Inject
    private Logger log;

    @Inject
    private Repository repository;
    
    @Inject
    private FacesContext facesContext;

    private Item item = new Item();
    
    private List<Item> searchResults;
    
    private String searchString;
    
    private Container selectedContainer = new Container();
    
    /*
    public String create() {
        log.infof("Saving item %s with containerId %d", item, selectedContainerId);
        try {
            Container container = repository.findContainerById(selectedContainerId);
            item.setContainer(container);
            repository.save(item);
            String message = "A new item with id " + item.getId() + " has been created successfully";
            facesContext.addMessage(null, new FacesMessage(message));
            item = new Item();
        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error occurred while creating the item.", e.getMessage()));
        }
        return "item";
    }
    */
    
    public void newListener(ActionEvent e) {
        
        if (getEditAction() == EditAction.edit) {
            return;
        }
        
        setMessage("Enter new item:");
        item = new Item();
        setEditAction(EditAction.create);
    }
    
    public void saveListener(ActionEvent event) {

        if (getEditAction() == EditAction.none) {
            return;
        }

        try {
            log.infof("Saving item %s container %s", item, selectedContainer);
            
            if (selectedContainer == null) {
                setMessage("No container selected.");
                return;
            }
            
            item.setContainer(selectedContainer);
            repository.save(item);
            setMessage("Saved.");
            setEditAction(EditAction.none);
        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error occurred while saving the item.", e.getMessage()));
        }
    }

    public void cancelListener(ActionEvent e) {

        if (getEditAction() == EditAction.none) {
            setMessage("");
            return;
        }

        setMessage("Cancelled.");
        setEditAction(EditAction.none);
        
        if (item != null && item.isPersisted()) {
            item = repository.findItemById(item.getId());
        } else {
            item = new Item();
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
        
        log.debugf("deleteListener: %s", getEditAction());

        if (getEditAction() != EditAction.none || !item.isPersisted()) {
            return;
        }
        
        try {
            String name = item.getName();
            repository.delete(item);
            item = new Item();
            setEditAction(EditAction.none);
            setMessage("Deleted " + name);
        } catch (Exception e) {
            e.printStackTrace();
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error occurred while deleting the item.", e.getMessage()));
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
        item = (Item) e.getNewValue();
        selectedContainer = item.getContainer();
    }
    
    public void search() {
        searchResults = repository.searchItems(searchString);
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item newItem) {
        log.tracef("setItem: %s", newItem);
        // Don't overwrite this from the listbox if we're editing.
        if (newItem != null && !getEditable()) {
            this.item = newItem;
        }
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

    public Container getSelectedContainer() {
        return selectedContainer;
    }

    public void setSelectedContainer(Container selectedContainer) {
        this.selectedContainer = selectedContainer;
    }

}
