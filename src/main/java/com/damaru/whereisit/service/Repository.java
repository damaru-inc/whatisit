package com.damaru.whereisit.service;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import com.damaru.whereisit.model.Container;
import com.damaru.whereisit.model.Item;
import com.damaru.whereisit.model.Room;
import com.damaru.whereisit.model.Saveable;

@Stateless
public class Repository {
    
    private static final String PROPERTY_NAME = "name";
    private static final String PROPERTY_ROOM = "room";

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
    
    public List<Container> findAllContainers() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Container> criteria = cb.createQuery(Container.class);
        Root<Container> container = criteria.from(Container.class);
        criteria.select(container).orderBy(cb.asc(container.get(PROPERTY_NAME)));
        return em.createQuery(criteria).getResultList();
    }
    
    public List<Container> findContainersByRoom(Room room) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Container> criteria = cb.createQuery(Container.class);
        Root<Container> container = criteria.from(Container.class);
        criteria.select(container)
            .where(cb.equal(container.get(PROPERTY_ROOM), room))
            .orderBy(cb.asc(container.get(PROPERTY_NAME)));
        return em.createQuery(criteria).getResultList();
    }
    
    public List<Item> findAllItems() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Item> criteria = cb.createQuery(Item.class);
        Root<Item> container = criteria.from(Item.class);
        criteria.select(container).orderBy(cb.asc(container.get(PROPERTY_NAME)));
        return em.createQuery(criteria).getResultList();
    }
    
    public List<Room> findAllRooms() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Room> criteria = cb.createQuery(Room.class);
        Root<Room> room = criteria.from(Room.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        //criteria.select(member).orderBy(cb.asc(member.get(Room.name)));
        criteria.select(room).orderBy(cb.asc(room.get(PROPERTY_NAME)));
        return em.createQuery(criteria).getResultList();
    }
    
    public List<Item> searchItems(String searchString) {
        String search = "%" + searchString.toLowerCase() + "%";
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Item> criteria = cb.createQuery(Item.class);
        Root<Item> root = criteria.from(Item.class);
        Path<String> namePath = root.get(PROPERTY_NAME);
        Expression<String> upperName = cb.lower(namePath);
        criteria.select(root)
            .where(cb.like(upperName, search))
            .orderBy(cb.asc(root.get(PROPERTY_NAME)));
        return em.createQuery(criteria).getResultList();        
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
