package com.damaru.whereisit.service;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.damaru.whereisit.model.Container;
import com.damaru.whereisit.model.Item;
import com.damaru.whereisit.model.Room;
import com.damaru.whereisit.model.Saveable;

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
    private Event<Container> containerEvent;

    @Inject
    private Event<Item> itemEvent;

    @Inject
    private Event<Room> roomEvent;

    
    public void save(Saveable saveable) {
        em.persist(saveable);
        em.flush();

        if (saveable instanceof Container) {
            containerEvent.fire((Container) saveable);
        } else if (saveable instanceof Item) {
            itemEvent.fire((Item) saveable);
        } else if (saveable instanceof Room) {
            roomEvent.fire((Room) saveable);
        }
        
        log.info("Saved " + saveable);
    }
    
    public void flush() {
        em.flush();
    }
    
    @SuppressWarnings("unchecked")
    public List<Container> findAllContainers() {
        String jpql = "select c from Container c order by name";
        return em.createQuery(jpql).getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<Container> findContainersByRoom(Room room) {
        String jpql = "select c from Container c where c.room = :room order by name";
        return em.createQuery(jpql)
                .setParameter("room", room)
                .getResultList();
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
    
    @SuppressWarnings("unchecked")
    public List<Item> searchItems(String searchString) {
        String search = "%" + searchString.toLowerCase() + "%";
        String jpql = "select i from Item i where lower(name) like :search order by name";
        return em.createQuery(jpql)
                .setParameter("search", search)
                .getResultList();
    }
    
    public void cleanDb() {
        deleteTable(ENTITY_ITEM);
        deleteTable(ENTITY_CONTAINER);
        deleteTable(ENTITY_ROOM);
    }

    private void deleteTable(String tableName) {
        Query query = em.createQuery("delete from " + tableName);
        int deleted = query.executeUpdate();
        log.info(String.format("Deleted %d rows from %s", deleted, tableName));
    }

    public Room findRoomById(Long selectedRoomId) {
        Room room = em.find(Room.class, selectedRoomId);
        return room;
    }

    public Container findContainerById(Long selectedContainerId) {
        Container container = em.find(Container.class, selectedContainerId);
        return container;
    }

}
