package com.damaru.whereisit.service;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.damaru.whereisit.model.Room;

@Stateless
public class DAO {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;
    
    @Inject
    private Event<Room> roomEvent;

    public void save(Room room) {
        em.persist(room);
        em.flush();
        roomEvent.fire(room);
        log.info("Saved " + room);
    }
    
    public List<Room> findAllRooms() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Room> criteria = cb.createQuery(Room.class);
        Root<Room> room = criteria.from(Room.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        //criteria.select(member).orderBy(cb.asc(member.get(Room.name)));
        criteria.select(room).orderBy(cb.asc(room.get("name")));
        return em.createQuery(criteria).getResultList();
    }

}
