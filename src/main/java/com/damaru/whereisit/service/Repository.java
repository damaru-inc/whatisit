package com.damaru.whereisit.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.damaru.whereisit.model.Container;
import com.damaru.whereisit.model.Item;
import com.damaru.whereisit.model.Room;
import com.damaru.whereisit.model.Saveable;

import org.jboss.logging.Logger;

@Stateless
public class Repository {
    
    private static final String ENTITY_CONTAINER = "Container";
    private static final String ENTITY_ITEM = "Item";
    private static final String ENTITY_ROOM = "Room";

    
    @Inject
    private Logger log;
    
    @Inject
    private EntityManager em;
    
    @Inject
    private Event<Saveable> saveableEvent;

    public void cleanDb() {
        deleteTable(ENTITY_ITEM);
        deleteTable(ENTITY_CONTAINER);
        deleteTable(ENTITY_ROOM);
    }
    
    public void delete(Saveable saveable) {
        log.debugf("Deleting %s", saveable);
        saveable = em.merge(saveable);
        em.remove(saveable);
        saveableEvent.fire(saveable);
    }
    
    private void deleteTable(String tableName) {
        Query query = em.createQuery("delete from " + tableName);
        int deleted = query.executeUpdate();
        log.infof("Deleted %d rows from %s", deleted, tableName);
    }
    
    @SuppressWarnings("unchecked")
    public List<Container> findAllContainers() {
        String jpql = "select c from Container c order by name";
        return em.createQuery(jpql).getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<Item> findAllItems() {
        String jpql = "select i from Item i order by name";
        return em.createQuery(jpql).getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<Room> findAllRooms() {
        String jpql = "select r from Room r order by name";
        return em.createQuery(jpql).getResultList();
    }
    
    public Container findContainerById(Long selectedContainerId) {
        Container container = em.find(Container.class, selectedContainerId);
        return container;
    }
    
    public Container findContainerByName(String name) {
        String jpql = "select c from Container c where name = :name";
        return (Container) em.createQuery(jpql)
                .setParameter("name", name)
                .getSingleResult();
    }

    
    @SuppressWarnings("unchecked")
    public List<Container> findContainersByRoom(Room room) {
        String jpql = "select c from Container c where c.room = :room order by name";
        return em.createQuery(jpql)
                .setParameter("room", room)
                .getResultList();
    }
    
    public Room findRoomById(Long selectedRoomId) {
        Room room = em.find(Room.class, selectedRoomId);
        return room;
    }

    public Room findRoomByName(String name) {
        String jpql = "select r from Room r where name = :name";
        return (Room) em.createQuery(jpql)
                .setParameter("name", name)
                .getSingleResult();
    }

    public void flush() {
        em.flush();
    }

    public void save(Saveable saveable) {
        
        log.infof("Saving %s", saveable);
        if (saveable.isPersisted()) {
            log.debugf("Merging %s", saveable);
            em.merge(saveable);
        } else {
            log.debugf("Persisting %s", saveable);
            em.persist(saveable);
        }
        
        saveableEvent.fire(saveable);
        log.infof("Saved %s", saveable);
    }

    @SuppressWarnings("unchecked")
    public List<Item> searchItems(String searchString) {
        log.debugf("searchString: %s", searchString);
        String search = "%" + searchString.toLowerCase() + "%";
        String jpql = "select i from Item i where lower(name) like :search order by name";
        return em.createQuery(jpql)
                .setParameter("search", search)
                .getResultList();
    }
}
