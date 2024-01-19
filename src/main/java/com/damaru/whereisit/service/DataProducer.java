package com.damaru.whereisit.service;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.logging.Logger;

import com.damaru.whereisit.model.Container;
import com.damaru.whereisit.model.Item;
import com.damaru.whereisit.model.Room;
import com.damaru.whereisit.model.Saveable;

@RequestScoped
public class DataProducer {

    @Inject
    private Logger log;

    @Inject
    private Repository repository;
    
    @Inject
    private FacesContext facesContext;
    
    private List<Container> containers;
    private List<Item> items;
    private List<Room> rooms;
    
    @PostConstruct
    public void init() {
        log.debug("Called init.");
        try {
            containers = repository.findAllContainers();
            items = repository.findAllItems();
            rooms = repository.findAllRooms();
        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error occurred while fetching data.", e.getMessage()));
        }
    }
    
    public void afterSaveableUpdate(@Observes Saveable saveable) {
        log.debug("afterSaveableUpdate " + saveable);
        init();
    }
    
    @Produces
    @Named
    public List<Container> getContainers() {
        return containers;
    }

    @Produces
    @Named
    public List<Item> getItems() {
        return items;
    }

    @Produces
    @Named
    public List<Room> getRooms() {
        return rooms;
    }
    
}
