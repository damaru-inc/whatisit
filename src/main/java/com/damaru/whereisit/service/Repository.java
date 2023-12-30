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
import javax.persistence.criteria.Root;

import com.damaru.whereisit.model.Container;
import com.damaru.whereisit.model.Room;

@Stateless
public class Repository {
    
    private static final String COLUMN_NAME = "name";

    private static final String TABLE_CONTAINER = "container";
    private static final String TABLE_ROOM = "room";

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;
    
    @Inject
    private Event<Room> roomEvent;

    @Inject
    private Event<Container> containerEvent;

    public void save(Container container) {
        em.persist(container);
        em.flush();
        containerEvent.fire(container);
        log.info("Saved " + container);
    }
    
    public void save(Room room) {
        em.persist(room);
        em.flush();
        roomEvent.fire(room);
        log.info("Saved " + room);
    }

    public List<Container> findAllContainers() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Container> criteria = cb.createQuery(Container.class);
        Root<Container> container = criteria.from(Container.class);
        criteria.select(container).orderBy(cb.asc(container.get(COLUMN_NAME)));
        return em.createQuery(criteria).getResultList();
    }
    
    public List<Container> findContainersByRoom(Room room) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Container> criteria = cb.createQuery(Container.class);
        Root<Container> container = criteria.from(Container.class);
        criteria.select(container)
            .where(cb.equal(container.get("room"), room))
            .orderBy(cb.asc(container.get(COLUMN_NAME)));
        return em.createQuery(criteria).getResultList();
    }
    
    public List<Room> findAllRooms() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Room> criteria = cb.createQuery(Room.class);
        Root<Room> room = criteria.from(Room.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        //criteria.select(member).orderBy(cb.asc(member.get(Room.name)));
        criteria.select(room).orderBy(cb.asc(room.get(COLUMN_NAME)));
        return em.createQuery(criteria).getResultList();
    }
    
    public void cleanDb() {
        deleteTable(TABLE_CONTAINER);
        deleteTable(TABLE_ROOM);
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

}
