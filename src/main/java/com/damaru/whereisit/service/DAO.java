package com.damaru.whereisit.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.damaru.whereisit.model.Room;

@Stateless
public class DAO {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    public void save(Room room) {
        em.persist(room);
        log.info("Saved " + room);
    }
}
